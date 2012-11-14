package marketplace;

import bank.Bank;
import bank.BankImpl;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

/**
 * Initiates a market remote class.
 * @author teo
 */
public class Server {
   
    public Server (String marketName, String bankName){
        try {
            
            Market marketObj = new MarketImpl(marketName);
            java.rmi.Naming.rebind("rmi:/127.0.0.1:1099/"+marketName, marketObj);
            System.out.println("Market server is ready");
            
            Bank bankobj = new BankImpl(bankName);
            java.rmi.Naming.rebind("rmi:/127.0.0.1:1099/"+bankName, bankobj);
	    System.out.println(bankobj + " is ready.");
                        
        } catch (MalformedURLException | RemoteException ex) {
            System.out.println("Server.constructor() :: Error occured while rebinding ...");
            ex.printStackTrace();
        }
    }
    
    public static void main(String[] args){
        //System.out.println(args[1]);
        String marketName = "MyMarket";
        String bankName = "Nordea";
        new Server(marketName, bankName);
        
    }
    
    
}
