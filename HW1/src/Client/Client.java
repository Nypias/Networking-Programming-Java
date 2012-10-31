package Client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

public class Client implements Runnable {

    //Connection variables
    private BufferedReader in = null;
    private Socket clientSocket = null;
    private DataOutputStream out = null;
    private MyGUI gui = null;
    //Game Parameters
    private String word = "";
    private String tries = "";
    private String score = "";
    
    static String host = "127.0.0.1";
    static int port = 9000;
    private boolean serverStopped = false;

    public void setServerStopped(boolean serverStopped) {
        this.serverStopped = serverStopped;
    }

    public boolean isServerStopped() {
        return serverStopped;
    }

    /**
     * Constructor of Client class
     *
     * @param host Host (IP) of the server
     * @param port Port of the server listener socket
     */
    public Client(String host, int port) {
        try {
            // Initialization of the client socket and input and output communication
            clientSocket = new Socket(host, port);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new DataOutputStream(clientSocket.getOutputStream());
        } catch (UnknownHostException ex) {
            System.out.println("Host unknown");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Communication input/output not created");
            ex.printStackTrace();
        }
    }

    /**
     * ConnectGUI Display the graphical user interface
     */
    void connectGUI() {
        gui = new MyGUI(this);
        Thread guiThread = new Thread(gui);
        guiThread.start();
    }

    /**
     * recvMessage Receive a new message
     */
    protected void recvMessage() {
        //Read from in stream
        
        String line;
        Pattern patt = Pattern.compile("|", Pattern.LITERAL);
        try {
            while (true) {
                System.out.println("Waiting to receive");
                line = in.readLine();
                System.out.println("Received line:" + line);
                if (line != null) {
                    if (line.startsWith("STATUS")) {
                        System.out.println(patt.split(line)[0]);
                        System.out.println(patt.split(line)[1]);
                        System.out.println(patt.split(line)[2]);
                        tries = patt.split(line)[1];
                        word = patt.split(line)[2];
                        score = patt.split(line)[3];
                        gui.getTriesLabel().setText("Tries: " + tries);
                        gui.getWordLabel().setText(word);
                        gui.getScoreLabel().setText("Score: " + score);
                    }
                    if (line.startsWith("CONGRATULATION")) {
                        tries = patt.split(line)[1];
                        word = patt.split(line)[2];
                        score = patt.split(line)[3];
                        gui.getTriesLabel().setText("Tries: " + tries);
                        gui.getWordLabel().setText(word);
                        gui.getScoreLabel().setText("Score: " + score);
                        JOptionPane.showMessageDialog(gui.getF(), "You Won! Start a new Game", "Hangman Result", JOptionPane.PLAIN_MESSAGE);

                        //terminate();
                        //break;

                    }
                    if (line.startsWith("LOOSE")) {
                        word = patt.split(line)[1];
                        tries = patt.split(line)[2];
                        gui.getWordLabel().setText(word);
                        gui.getTriesLabel().setText("Tries: " + tries);
                        JOptionPane.showMessageDialog(gui.getF(), "You Lost! Start a new Game", "Hangman Result", JOptionPane.PLAIN_MESSAGE);
                        //terminate();
                        //break;
                    }
                } else {
                    
                    System.out.println("Server must have crashed, exiting..");
                    terminate();
                    serverStopped=true;
                    JOptionPane.showMessageDialog(gui.getF(), "Lost connection to server, try 'New Game'!", "Hangman Result", JOptionPane.WARNING_MESSAGE);
                    break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * SendMessage
     *
     * @param message
     */
    protected void sendMessage(String message) {
        try {
            System.out.println("Client.sendMessage :: Sending message");
            out.writeBytes(message);
            out.flush();
            System.out.println("Client.sendMessage :: Message was sent");
        } catch (IOException ex) {
            System.out.println("Sending of the message " + message + " failed");
        }

    }

    protected void terminate() {
        try {
            //TODO: maybe close streams
            in = null;
            out = null;
            clientSocket.close();
            clientSocket = null;
        } catch (IOException ex) {
        }
    }
    
    @Override
    public void run() {
        System.out.println("Starting thread");
        init();
        recvMessage();
    }

    private void init(){
        try {
            clientSocket = new Socket(host, port);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new DataOutputStream(clientSocket.getOutputStream());
            System.out.println("Init completed");
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
       
    }
    public static void main(String argv[]) throws Exception {
        // Capture port and server ip from arguments
        try {
            if (argv.length > 1) {
                Client.port = Integer.parseInt(argv[1]);
            }
            if (argv.length > 0) {
                 Client.host = argv[0];
            }
        } catch (NumberFormatException e) {
            System.out.println("USAGE: java Client [ip] [port]");
            System.exit(1);
        }

        // Begin the client communication
        Client client = new Client(Client.host, Client.port);
        client.connectGUI();
        Thread clientThread = new Thread(client);
        clientThread.start();

    }
}