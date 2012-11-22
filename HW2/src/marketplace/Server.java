package marketplace;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;

import tools.Utilities;
import bankjpa.Bank;
import bankjpa.BankImpl;

/**
 * Initiates a market remote class.
 * @author teo
 */
public class Server {
   
    public Server (String marketName, String bankName){
        try {
            String ip =  Utilities.getCurrentEnvironmentNetworkIp().toString();
            System.out.println("Server listening on IP:"+ip);
            Bank bankobj = new BankImpl();
         
            java.rmi.Naming.rebind(/*"rmi:/"+ip+":"+Utilities.port+"/"+*/bankName, bankobj);
	    System.out.println(bankName + " server is ready.");
            
            Market marketObj = new MarketImpl(marketName, bankName);
            java.rmi.Naming.rebind(/*"rmi:/"+ip+":"+Utilities.port+"/"+*/marketName, marketObj);
            System.out.println(marketName+" server is ready");
                        
        } catch ( UnknownHostException|  MalformedURLException | RemoteException ex) {
            System.out.println("Server.constructor() :: Error occured while rebinding ...");
            ex.printStackTrace();
        }
    }
    
    public static void main(String[] args){
        //System.out.println(args[1]);
        String marketName = "m1";//JOptionPane.showInputDialog("Enter market name");
        String bankName = "b1";//JOptionPane.showInputDialog("Enter bank name");
        new Server(marketName, bankName);
        
    }
    
    
}
