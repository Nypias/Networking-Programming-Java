package marketplace;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

/**
 * Initiates a market remote class.
 * @author teo
 */
public class Server {
   
    public Server (String marketName){
        try {
            Market marketObj = new MarketImpl(marketName);
            java.rmi.Naming.rebind("rmi:/localhost:1099/"+marketName, marketObj);
            System.out.println("Market server is ready");
        } catch (MalformedURLException  ex) {
            System.out.println("Server.constructor() :: Error occured while rebinding ...");
            ex.printStackTrace();
        } catch (RemoteException e) {
        	System.out.println("Server.constructor() :: Error occured while rebinding ...");
			e.printStackTrace();
		}
    }
    
    public static void main(String[] args){
        //System.out.println(args[1]);
        String marketName = "MyMarket";
        new Server(marketName);
        
    }
    
    
}
