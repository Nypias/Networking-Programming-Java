package Server;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainServer {

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		int port = 9000;
		int poolSize = 3;
		ServerSocket serverSock = null;
		String urlDictionnary = "/british-english";
		List<String> dictionnary = new ArrayList<String>();

		// Capture port and poolSize from arguments
		try {
			if (args.length > 1)
				poolSize = Integer.parseInt(args[1]);
			if (args.length > 0)
				port = Integer.parseInt(args[0]);

		} catch (NumberFormatException e) {
			System.out.println("USAGE: java MainServer [port] [poolSize]");
			System.exit(1);
		}

		// Creation of the dictionnary
		Scanner scanDict = null;
		boolean initialization = false;
		try {
			String pathFile = System.getProperty("user.dir")+urlDictionnary;
			scanDict = new Scanner(new FileReader(pathFile));
			initialization = true;
			while (scanDict.hasNext()) {
				String word = scanDict.nextLine();
				if (word.matches("\\w+")) {
					dictionnary.add(word);
					//System.out.println("Word : " + word);
				}
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (initialization) {
			// Creation of a server socket
			try {
				serverSock = new ServerSocket(port);
			} catch (IOException e) {
				System.out.println("Error - Can't connect to the port" + port);
				e.printStackTrace();
			}

			// Creation of a pool of threads
			ExecutorService executor = Executors.newFixedThreadPool(poolSize);
			System.out.println("---- SERVER STARTED ----");
			while (true) {
				Socket socket;
				try {
					// We accept each connexion from the client and we
					socket = serverSock.accept();
					ServerApplication newServerAppli = new ServerApplication(socket, dictionnary);
					executor.execute(newServerAppli);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			System.out.println("Dict has not been created");
		}
	}
}
