package trader;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import javax.swing.JOptionPane;

/**
 *
 * @author teo
 */
public class TraderClient {
    
     public static void main(String[] args) throws InterruptedException, NotBoundException, MalformedURLException {
       /* try {
         int choice = 1;
         if (choice == 1) {
         
         Item item1 = new Item("CAMERA", 400, "t1");
         Market marketObj = (Market) Naming.lookup("m1");
         marketObj.sell("t1", item1);
         System.out.println("checkpoint");}
         /*
         System.out.println(trader1.getMarketObj().register(trader1));
         trader1.getMarketObj().sell(trader1.getUsername(), item1);
         try {
         Account myAccount = trader1.getBankObj().newAccount(trader1.getUsername());
         myAccount.deposit(1000f);
         } catch (RejectedException ex) {
         ex.printStackTrace();
         }

         } else {   //TRADER 2 TEST
         TraderImpl trader2 = new TraderImpl("trader2");
         try {
         Account myAccount = trader2.getBankObj().newAccount(trader2.getUsername());
         myAccount.deposit(100f);
         } catch (RejectedException ex) {
         ex.printStackTrace();
         }

         //Wish wish1 = new Wish("CAMERA", 500, trader2.getUsername());

         System.out.println(trader2.getMarketObj().register(trader2));
         //marketObj.listItems(trader2.getUsername());
         Item buyItem = new Item("CAMERA", 400, null);
         trader2.getMarketObj().buy(trader2.getUsername(), buyItem);
         //marketObj.wish(trader2.getUsername(), wish1);

         }


         } catch (RemoteException ex) {
         ex.printStackTrace();
         }
*/
        String traderName = JOptionPane.showInputDialog("Enter trader name");
        String marketName = "m1"; 	//JOptionPane.showInputDialog("Enter market name");
        String bankName = "b1";	//JOptionPane.showInputDialog("Enter bank name");

        //System.setProperty("java.rmi.server.hostname", "192.168.1.15");



        try {
            TraderImpl trader = new TraderImpl(traderName, marketName, bankName);
           
            trader.getMarketObj().register(trader,"12345679");	// We register the trader
            //trader.getMarketObj().register(trader,"12345610111");	// We register the trader
           
            //trader.startGUI();

        } catch (RemoteException ex) {
            ex.printStackTrace();
        }



    }
}
