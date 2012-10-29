package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author teo
 */
public class MainServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	int port = 9000;  
    	int poolSize = 3;
        ServerSocket serverSock = null;
    	
        // Capture port and poolSize from arguments
        try {
    		if (args.length >1) poolSize = Integer.parseInt(args[1]);
    		if (args.length >0) port = Integer.parseInt(args[0]);

    	} catch (NumberFormatException e) {
    		System.out.println("USAGE: java MainServer [poolSize] [port]");
    		System.exit(1);
    	}
        
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
				executor.execute(new ServerApplication(socket));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
}
