package trader;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import marketplace.Item;
import marketplace.Market;
import tools.Utilities;
import bank.Bank;
import javax.swing.JOptionPane;

/**
 *
 * @author teo
 */
public class TraderImpl extends UnicastRemoteObject implements Trader {
	private static final long serialVersionUID = -8577687714557513311L;
	
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
            marketObj = (Market) Naming.lookup(marketName);
            bankObj = (Bank) Naming.lookup(bankName);
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

    @SuppressWarnings("unchecked")
	@Override
    public void sendNotification(Integer typeMessage, Object message) throws RemoteException {
		System.out.println("Message received by the client - " + typeMessage);
		switch (typeMessage) {
		case Utilities.ITEM_ADDED_SALE:
			this.gui.addLog((String) message);
			break;
		case Utilities.ITEM_WISHED_RECEIVED:
			this.gui.addLog((String) message);
			break;
		case Utilities.ITEM_ALREADY_EXISTS:
			this.gui.addLog((String) message);
			break;
		case Utilities.ITEM_WISHED_AVAILABLE:
			this.gui.addLog((String) message);
			break;
		case Utilities.ITEM_WISHED_ALREADY_EXISTS:
			this.gui.addLog((String) message);
			break;
		case Utilities.WISH_REGISTERED:
			this.gui.addLog((String) message);
			break;
		case Utilities.WISH_CAN_BE_SERVED:
			this.gui.addLog((String) message);
			break;
		case Utilities.WISH_ALREADY_REGISTERED:
			this.gui.addLog((String) message);
			break;
		case Utilities.BALANCE_INSUFFICIENT:
			this.gui.addLog((String) message);
			break;
		case Utilities.ALL_PRODUCTS_FROM_MARKET:
			System.out.println("ALL PRODUCTS FROM MARKET : "
					+ ((List<Item>) message).size());
			this.gui.getListItemsModel().addAllItems((List<Item>) message);
			this.gui.getListItemsModel().fireTableDataChanged();

			this.gui.addLog("All products have been updated from the Market");
			break;
		case Utilities.PRODUCT_SOLD:
			this.gui.addLog("CurrentBalance:"
					+ bankObj.getAccount(name).getBalance());
			this.gui.setBalanceTrader(bankObj.getAccount(name).getBalance() + "");
			break;
		case Utilities.PRODUCT_BOUGHT:
			this.gui.addLog("CurrentBalance:"
					+ bankObj.getAccount(name).getBalance());
			this.gui.setBalanceTrader(bankObj.getAccount(name).getBalance() + "");
			break;
                
        }

        System.out.println("TraderImpl.setLabelText() :: " + typeMessage);
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
        String marketName = "m1"; 	//JOptionPane.showInputDialog("Enter market name");
        String bankName = "b1";	//JOptionPane.showInputDialog("Enter bank name");
        
        //System.setProperty("java.rmi.server.hostname", "192.168.1.15");

        
        
        try { 
            TraderImpl trader = new TraderImpl(traderName, marketName, bankName);
            trader.getMarketObj().register(trader);	// We register the trader
            
            trader.startGUI();
            
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
        
        
       
    }
}
