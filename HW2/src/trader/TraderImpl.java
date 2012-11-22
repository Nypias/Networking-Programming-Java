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
import bankjpa.Bank;

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
    
    public TraderImpl() throws RemoteException {
    	this.registered = false;
    }

    public TraderImpl(String nameTrader) throws RemoteException {
        this.name = nameTrader;
        this.registered = false;
    }
    
    public void initRemoteObjects(String marketName, String bankName)  throws RemoteException {
    	try {
            marketObj = (Market) Naming.lookup(marketName);
            bankObj = (Bank) Naming.lookup(bankName);
        } catch (NotBoundException | MalformedURLException ex) {
            ex.printStackTrace();
        }
    }

    public void startGUI() {
        this.gui = new TraderGUI(this);
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                gui.setVisible(true);
            }
        });
    }
    
    public void setGUI(TraderGUI gui) {
    	this.gui = gui;
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

                this.gui.setStatistics("");
                this.gui.addLog("All products have been updated from the Market");
                break;
            case Utilities.LISTITEMS_ON_SALE_TRADER:
            	int numberSale = ((List<Item>) message).size();
            	this.gui.setStatistics("Number of on sale items : " + numberSale);
            	
            	this.gui.getListItemsModel().addAllItems((List<Item>) message);
                this.gui.getListItemsModel().fireTableDataChanged();
                this.gui.addLog("The list of on sale items is displayed");
            	break;
            case Utilities.LISTITEMS_BOUGHT_TRADER:
            	int numberBought = ((List<Item>) message).size();
            	this.gui.setStatistics("Number of bought items : " + numberBought);
            	
            	this.gui.getListItemsModel().addAllItems((List<Item>) message);
                this.gui.getListItemsModel().fireTableDataChanged();
                this.gui.addLog("The list of trader bought items is displayed");
            	break;
            case Utilities.PRODUCT_SOLD:
                this.gui.addLog((String) message);
                this.gui.addLog("CurrentBalance:"
                        + bankObj.findAccount(name).getBalance());
                this.gui.setBalanceTrader(bankObj.findAccount(name).getBalance() + "");
                break;
            case Utilities.PRODUCT_BOUGHT:
                this.gui.addLog((String) message);
                this.gui.addLog("CurrentBalance:"
                        + bankObj.findAccount(name).getBalance());
                this.gui.setBalanceTrader(bankObj.findAccount(name).getBalance() + "");
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
    
    public void setName(String name) {
    	this.name = name;
    }
    
}
