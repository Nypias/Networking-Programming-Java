package marketplace;

import bank.Bank;
import bank.BankImpl;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import javax.swing.JOptionPane;

/**
 * Initiates a market remote class.
 * @author teo
 */
public class Server {
   
    public Server (String marketName, String bankName){
        try {
            
            Bank bankobj = new BankImpl(bankName);
            java.rmi.Naming.rebind("rmi:/localhost:1099/"+bankName, bankobj);
	    System.out.println(bankName + " server is ready.");
            
            Market marketObj = new MarketImpl(marketName, bankName);
            java.rmi.Naming.rebind("rmi:/localhost:1099/"+marketName, marketObj);
            System.out.println(marketName+" server is ready");
                        
        } catch (MalformedURLException | RemoteException ex) {
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
