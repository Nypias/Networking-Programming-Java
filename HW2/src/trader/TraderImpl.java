package trader;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.sun.xml.internal.bind.v2.model.core.ID;

import marketplace.Item;
import marketplace.Market;

/**
 *
 * @author teo
 */
public class TraderImpl extends UnicastRemoteObject implements Trader {

    //Unique identifier of Traider
    private String name;
    //Client should check if it has registered.
    private boolean registered;
    
    private static TraderGUI gui;

    @Override
    public String getName() {
        System.out.println("Retrieved name");
        return name;
    }

    public TraderImpl(String name) throws RemoteException {
        this.name = name;
        this.registered = false;
    }

    @Override
    public void wishAvailable() throws RemoteException {
        //TODO: Handle this callback function
        System.out.println("Your wish, our command!");
    }

    @Override
    public void sendNotification(String text) throws RemoteException {
        //TODO: Update GUI Label text
    	
        System.out.println("TraderImpl.setLabelText() :: " + text);
    }

    public static void main(String[] args) throws InterruptedException {
        /*try {
            String marketName = "MyMarket";
            int choice = 1;
            if (choice == 2) {
                Trader trader1 = new TraderImpl("trader1");
                Item item1 = new Item("CAMERA", 500);
                //Item item2 = new Item("BIKE",11500);
                item1.setSeller(trader1.getName());
                //Item item3 = new Item("PEN",10);
                //item1.setSeller(trader1.getName());

                Market marketObj = (Market) Naming.lookup("rmi:/localhost:1099/" + marketName);
                System.out.println(marketObj.register(trader1));
                marketObj.listItems(trader1);

                marketObj.sell(trader1.getName(), item1);
                //marketObj.sell(trader, item3);

                //System.out.println(marketObj.unregister(trader));

            } else {   //TRADER 2 TEST
                Trader trader2 = new TraderImpl("trader2");
                Item item1 = new Item("CAMERA", 600);
                item1.setWishedPrice(600);
                item1.setRequesterName(trader2.getName());
                Market marketObj = (Market) Naming.lookup("rmi:/localhost:1099/" + marketName);
                System.out.println(marketObj.register(trader2));
                marketObj.listItems(trader2);
                marketObj.buy(trader2, item1);
            }
    	

        } catch (NotBoundException ex) {
            ex.printStackTrace();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }*/
    	
    	String marketName = "MyMarket";
    	Market marketObj;
		try {
			marketObj = (Market) Naming.lookup("rmi:/localhost:1099/" + marketName);
			Trader traderTheo = new TraderImpl("Theo");
	    	marketObj.register(traderTheo);
	    	Item item1 = new Item("CAMERA", 600);
	    	marketObj.sell(traderTheo.getName(), item1);
	    	Item item2 = new Item("PEN",10);
	    	marketObj.sell(traderTheo.getName(), item2);
	    	
	    	marketObj.listItems(traderTheo);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
    	
    	gui = new TraderGUI();

    }
}
