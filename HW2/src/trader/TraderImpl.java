package trader;

import bank.Account;
import bank.Bank;
import bank.RejectedException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import marketplace.Item;
import marketplace.Market;
import tools.Utilities;

/**
 *
 * @author teo
 */
public class TraderImpl extends UnicastRemoteObject implements Trader {

    //Unique identifier of Traider
    private String name;
    //Client should check if it has registered.
    private boolean registered;
    private TraderGUI gui;
    private Market marketObj;
    private Bank bankObj;
    
   

    public TraderImpl(String name, String marketName, String bankName) throws RemoteException {

        this.name = name;
        this.registered = false;
        
        try {
            marketObj = (Market) Naming.lookup("rmi:/localhost:1099/" + marketName);
            bankObj = (Bank) Naming.lookup("rmi:/localhost:1099/" + bankName);
        } catch (NotBoundException | MalformedURLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void startGUI(){
        this.gui = new TraderGUI(this);
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                gui.setVisible(true);
            }
        });
    }

    @Override
    public void wishAvailable() throws RemoteException {
        //TODO: Handle this callback function
        System.out.println("Your wish, our command!");
    }

    @Override
    public void sendNotification(Integer typeMessage, Object message) throws RemoteException {
        System.out.println("Message received by the client - " + typeMessage);
        switch (typeMessage) {
            case Utilities.ALL_PRODUCTS_FROM_MARKET:
                System.out.println("ALL PRODUCTS FROM MARKET");
                this.gui.createListItems(this.gui.transformTable((List<Item>) message));
                break;
            case Utilities.PRODUCT_SOLD:
                System.out.println("CurrentBalance:" + bankObj.getAccount(name).getBalance());
            case Utilities.PRODUCT_BOUGHT:
                System.out.println("CurrentBalance:" + bankObj.getAccount(name).getBalance());
        }

        //System.out.println("TraderImpl.setLabelText() :: " + text);
    }

     public Market getMarketObj() {
        return marketObj;
    }

    public Bank getBankObj() {
        return bankObj;
    }
   

    @Override
    public String getName() {
        System.out.println("Retrieved name");
        return name;
    }
    
    public static void main(String[] args) throws InterruptedException {
        /*try {
         int choice = 2;
         if (choice == 1) {
         TraderImpl trader1 = new TraderImpl("trader1");
         Item item1 = new Item("CAMERA", 400, trader1.getName());
         item1.setSeller(trader1.getName());

         System.out.println(trader1.getMarketObj().register(trader1));
         trader1.getMarketObj().sell(trader1.getName(), item1);
         try {
         Account myAccount = trader1.getBankObj().newAccount(trader1.getName());
         myAccount.deposit(1000f);
         } catch (RejectedException ex) {
         ex.printStackTrace();
         }

         } else {   //TRADER 2 TEST
         TraderImpl trader2 = new TraderImpl("trader2");
         try {
         Account myAccount = trader2.getBankObj().newAccount(trader2.getName());
         myAccount.deposit(100f);
         } catch (RejectedException ex) {
         ex.printStackTrace();
         }

         //Wish wish1 = new Wish("CAMERA", 500, trader2.getName());

         System.out.println(trader2.getMarketObj().register(trader2));
         //marketObj.listItems(trader2.getName());
         Item buyItem = new Item("CAMERA", 400, null);
         trader2.getMarketObj().buy(trader2.getName(), buyItem);
         //marketObj.wish(trader2.getName(), wish1);

         }


         } catch (RemoteException ex) {
         ex.printStackTrace();
         }*/

        String traderName = JOptionPane.showInputDialog("Enter trader name");
        String marketName = JOptionPane.showInputDialog("Enter market name");
        String bankName = JOptionPane.showInputDialog("Enter bank name");
        
        try { 
            TraderImpl trader = new TraderImpl(traderName, marketName, bankName);
            trader.startGUI();
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
        
        
       
    }
}
