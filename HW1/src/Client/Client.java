package Client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import Client.MyGUI;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

/**
 *
 * @author teo
 */
public class Client {

    //Connection variables
    BufferedReader in = null;
    Socket clientSocket = null;
    DataOutputStream out = null;
    MyGUI gui = null;
    //Game Parameters
    String letter = "";
    String word = "";
    int retries = 0;

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
        try {
            //while(true){
            String line1 = "";
            System.out.println("Waiting to receive");
            line = in.readLine();
            System.out.println("Received line:" + line + ".");
            if (line != null) {
                if (line1.startsWith("START")) {
                    System.out.println("Line:" + line);
                }
                if (line1.startsWith("TERMINATE")) {
                    terminate();

                }
            }
            //}
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
        String sentence;
        String modifiedSentence;
        Client client = new Client("127.0.0.1", 9000);
        client.sendMessage("START\n");
        client.connectGUI();
        client.recvMessage();

        //sentence = inFromUser.readLine();
        //out.writeBytes(sentence + '\n');

    }
}