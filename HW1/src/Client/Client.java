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

public class Client {

    //Connection variables
    private BufferedReader in = null;
    private Socket clientSocket = null;
    private DataOutputStream out = null;
    private MyGUI gui = null;
    
    //Game Parameters
    private String letter = "";
    private String word = "";
    private String tries = "";

    public Client(String host, int port) {
        try {

            clientSocket = new Socket(host, port);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new DataOutputStream(clientSocket.getOutputStream());
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    void connectGUI() {
        gui = MyGUI.showGUI(this);
    }

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
                        gui.getTriesLabel().setText(tries);
                        gui.getWordLabel().setText(word);
                    }
                    if (line.startsWith("CONGRATULATION")) {
                        tries = patt.split(line)[1];
                        word = patt.split(line)[2];
                        gui.getTriesLabel().setText(tries);
                        gui.getWordLabel().setText(word);
                        JOptionPane.showMessageDialog(gui.getF(),"You Won! Start a new Game", "Hangman Result", JOptionPane.PLAIN_MESSAGE);

                        //terminate();
                        //break;

                    }
                    if (line.startsWith("LOOSE")) {
                        word = patt.split(line)[1];
                        gui.getWordLabel().setText(word);
                        JOptionPane.showMessageDialog(gui.getF(),"You Lost! Start a new Game", "Hangman Result", JOptionPane.PLAIN_MESSAGE);
                        //terminate();
                        //break;

                    }

                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }

    /**
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
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    protected void terminate() {
        try {
            in.close();
            out.flush();
            out.close();
            clientSocket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }

    public static void main(String argv[]) throws Exception {
    	    	
        Client client = new Client("127.0.0.1", 9000);
        //client.sendMessage("START\n");
        client.connectGUI();
        client.recvMessage();

        //sentence = inFromUser.readLine();
        //out.writeBytes(sentence + '\n');

    }
}