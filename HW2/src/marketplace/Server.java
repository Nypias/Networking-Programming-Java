package marketplace;

import bank.Bank;
import bank.BankImpl;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import tools.Utilities;

/**
 * Initiates a market remote class.
 * @author teo
 */
public class Server {
   
    public Server (String marketName, String bankName){
        try {
            String ip =  Utilities.getCurrentEnvironmentNetworkIp().toString();
            System.out.println("Server listening on IP:"+ip);
            Bank bankobj = new BankImpl(bankName);
         
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
        String marketName = JOptionPane.showInputDialog("Enter market name");
        String bankName = JOptionPane.showInputDialog("Enter bank name");
        new Server(marketName, bankName);
        
    }
    
    
}
