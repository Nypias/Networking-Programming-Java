package Server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.regex.Pattern;

public class ServerApplication extends Thread {
	
	private String clientSentence;
	private StringBuilder actualSentence;
	private int clientScore;
	
	private Socket socketClient;
	
	private BufferedReader in;
	private DataOutputStream out;
	
	private final int nbTentatives = 10;
	private final String urlWords = "../british-english";

	/**
	 * Constructor of ServerApplication
	 * @param socket Client socket
	 * @throws IOException 
	 */
	public ServerApplication(Socket socket) throws IOException {
		
		this.socketClient = socket;
		
		System.out.println("Client connected");
		
		// Create communication in and out
		this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new DataOutputStream(socket.getOutputStream());
        
        while(true) {
        	String newMessage = this.receiveMessage();
        	this.processReceivedMessage(newMessage);
        }
	}
	

	public String receiveMessage () throws IOException {
		String message = this.in.readLine();
		return message;
	}
	
	/**
	 * Send message to out
	 * @param message String that has to be sent
	 * @throws IOException 
	 */
	public void sendMessage(String message) {
		try {
			out.writeBytes(message);
			out.flush();
		} catch (IOException e) {
			System.out.println("Can't write the message for sending to the client");
			e.printStackTrace();
		}
	}
	
	/**
	 * Retrieve a new word from /usr/dict
	 * @return String word chosen
	 */
	public String retrieveNewWord() {
		/*File file = new File(this.urlWords);
		Scanner sc = null;
		try {
			sc = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.println("The file can not be found");
			e.printStackTrace();
		}*/
		
	
		
		return "essai";
	}
	
	public void processReceivedMessage (String message) {
		System.out.println("Message received: " + message);
		Pattern patt = Pattern.compile("|", Pattern.LITERAL);
		String result[] =  patt.split(message);
		if (result.length > 0) {
			String head = result[0];
			// If the client wants to start a new game
			if (head.equals("START")) {
				this.clientSentence = this.retrieveNewWord();
				
				this.actualSentence = new StringBuilder("");
				// We create the dashed word - Actual version of the client
				for (int i=0; i < this.clientSentence.length(); i++) {
					this.actualSentence.append("-");
				}
				
				// We reset the score
				this.clientScore = this.nbTentatives;
				
				// We send a message with the number of tentatives and the encoded word
				this.sendMessage("STATUS|" + this.clientScore + "|" + this.actualSentence + "\n");
				
			} else if (head.equals("LETTER")) {
				char letter = result[1].charAt(0);
				boolean found = false;

				// We refresh the actual word of the client
				for (int i=0; i < this.clientSentence.length(); i++) {
					if (this.clientSentence.charAt(i) == letter) {
						found = true;
						this.actualSentence.setCharAt(i, letter);
					}
				}
				
				// We decrement the score if the letter is not in the word
				if (found == false) {
					this.clientScore--;
				}
				
				if (this.clientSentence.equals(this.actualSentence.toString())) {
					// The player wins the game
					this.sendMessage("CONGRATULATION|" + this.clientSentence + "|" + this.clientScore + "\n");
				} else if (this.clientScore == 0) {
					// The player lost the game
					this.sendMessage("LOOSE|" + this.clientSentence + "\n");
				} else {
					// The player is still playing. We send him a STATUS message
					this.sendMessage("STATUS|" + this.clientScore + "|" + this.actualSentence + "\n");
				}
			} else if (head.equals("QUIT")) {
				try {
					this.in.close();
					this.out.close();
					this.socketClient.close();
				} catch (IOException e) {
					System.out.println("Error when closing Thread variables");
					e.printStackTrace();
				}
			} else {
				System.out.println("Head Incorrect : " + result[0]);
			}
		}
		
	}

}
