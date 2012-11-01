package Server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Server Application Class
 * @author thomas, teo
 *
 */
public class ServerApplication implements Runnable {

	// Sentence chosen by the server for the client
    private String clientSentence;
    // Sentence actually guessed by the client
    private StringBuilder actualSentence;
    // Score of the client
    private int clientScore;
    // Number of tries left
    private int clientTries;
    // Socket of the client and communication in and out
    private Socket socketClient;
    private BufferedReader in;
    private DataOutputStream out;
    // Number of total tries
    private final int nbTentatives = 10;
    // Initial score
    private final int initialScore = 0;
    // Table which contains the letters already entered by the client
    private List<Character> letterAlreadyEntered;
    // Dictionnary of words
    private final List<String> dictionnary;
    // Generator of number to choose a word in the dictionnary
    private Random generator;
    
    private boolean error = false;
    private boolean quit = false;

    /**
     * Constructor of ServerApplication
     *
     * @param socket Client socket
     * @param dictionnary List of words
     */
    public ServerApplication(Socket socket, List<String> dictionnary) {
        this.socketClient = socket;
        this.dictionnary = new ArrayList<String>(dictionnary);
        this.letterAlreadyEntered = new ArrayList<Character>();

        this.generator = new Random();
    }

    /**
     * Receive a new message from the client
     * @return String Message that has been read
     * @throws IOException
     */
    public String receiveMessage() throws IOException {
        String message = this.in.readLine();
        return message;
    }

    /**
     * Send message to the client
     *
     * @param message String that has to be sent
     */
    public void sendMessage(String message) {
        try {
        	//System.out.println("Write : " + message);
            out.writeBytes(message);
            out.flush();
        } catch (IOException e) {
            System.out.println("Can't write the message for sending to the client");
            e.printStackTrace();
        }
    }

    /**
     * Retrieve a new word from the dictionnary
     *
     * @return String word chosen
     */
    public String retrieveNewWord() {

        int number = generator.nextInt(this.dictionnary.size() - 1);
        System.out.println(this.dictionnary.get(number).toLowerCase());
        return this.dictionnary.get(number).toLowerCase();
    }

    /**
     * Process a received message
     * @param message Message that has been received by the server
     */
    public void processReceivedMessage(String message) {
        System.out.println("Message received: " + message);
        Pattern patt = Pattern.compile("|", Pattern.LITERAL);
        String result[] = patt.split(message);
        if (result.length > 0) {
            String head = result[0];
            
            if (head.equals("START")) {
               this.processStartMessage();
            } else if (head.equals("LETTER")) {
            	this.processLetterMessage(result);
            } else if (head.equals("QUIT")) {
                this.processQuitMessage();
            } else {
                error = true;
                System.out.println("Head Incorrect : " + result[0]);
            }
        }

    }
    
    /**
     * Process a START Message
     * Begin a new game
     */
    public void processStartMessage() {
    	 this.clientSentence = this.retrieveNewWord();

         this.actualSentence = new StringBuilder("");
         // We create the dashed word - Actual version of the client
         for (int i = 0; i < this.clientSentence.length(); i++) {
             this.actualSentence.append("-");
         }

         // We reset the tries
         this.clientTries = this.nbTentatives;
         // We reset the score
         clientScore = initialScore;
         // We reset the table which contains the letter already typed by the client
         this.letterAlreadyEntered = new ArrayList<Character>();
         // We send a message with the number of tentatives and the encoded word
         this.sendMessage("STATUS|" + this.clientTries + "|" + this.actualSentence + "|" + this.clientScore + "\n");
    }
    
    
    /**
     * Process a LETTER message
     * @param result Message that has been received by the server
     */
    public void processLetterMessage(String[] result) {
    	if (clientSentence.equals(result[1])) {
            this.sendMessage("CONGRATULATION|" + this.clientTries + "|" + this.clientSentence + "|" + this.clientSentence.length() * this.clientSentence.length() * 10 + "\n");
        } else {
            char letter = result[1].charAt(0);
            
            // We check if the client already typed this letter
            boolean alreadyEntered = false;
            if (this.letterAlreadyEntered.contains(letter)) {
            	alreadyEntered = true;
            } else {
            	this.letterAlreadyEntered.add(letter);
            }
            
            boolean found = false;

            // We refresh the actual word of the client
            for (int i = 0; i < this.clientSentence.length(); i++) {
                if (this.clientSentence.charAt(i) == letter) {
                    found = true;
                    this.actualSentence.setCharAt(i, letter);
                    
                    if (!alreadyEntered) {clientScore += 10 * clientSentence.length();}
                }
            }

            // We decrement the score if the letter is not in the word
            if (found == false) {
                this.clientTries--;
            }

            if (this.clientSentence.equals(this.actualSentence.toString())) {
                // The player wins the game
                this.sendMessage("CONGRATULATION|" + this.clientTries + "|" + this.clientSentence + "|" + this.clientScore + "\n");
            } else if (this.clientTries == 0) {
                // The player lost the game
                this.sendMessage("LOOSE|" + this.clientSentence + "|" + this.clientTries + "\n");
            } else {
                // The player is still playing. We send him a STATUS message
                this.sendMessage("STATUS|" + this.clientTries + "|" + this.actualSentence + "|" + this.clientScore + "\n");
            }
        }
    }
    
    /**
     * Process a QUIT message
     * Close the communication socket for the client
     */
    public void processQuitMessage () {
    	try {
            this.in.close();
            this.out.close();
            this.socketClient.close();
            quit = true;
        } catch (IOException e) {
            System.out.println("Error when closing Thread variables");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Client connected");

        // Create communication in and out
        try {
            this.in = new BufferedReader(new InputStreamReader(this.socketClient.getInputStream()));
            this.out = new DataOutputStream(this.socketClient.getOutputStream());
        } catch (IOException e1) {
            System.out.println("Error when initializing the in and out communication with the client");
            e1.printStackTrace();
        }

        while (!error && !quit) {
            String newMessage = "";
            try {
                newMessage = this.receiveMessage();
            } catch (IOException e) {
                System.out.println("Error when receiving a new message");
                e.printStackTrace();
            }
            this.processReceivedMessage(newMessage);
        }

        if (error) {
            try {
                this.in.close();
                this.out.close();
                this.socketClient.close();
            } catch (IOException e) {
                System.out.println("Error when closing the client communication");
                e.printStackTrace();
            }
        }



    }
}
