package marketplace;

import java.rmi.Remote;
import java.rmi.RemoteException;
import trader.Trader;

/**
 * Remote interface for MarketImpl.
 * Void functions return the result by using the callback
 * functions of the Trader Remote Interface.
 * @author teo
 */
public interface Market extends Remote {
    // Returns true/false on success/failure
    public String register(Trader trader) throws RemoteException;
    // Returns true/false on success/failure
    public String unregister(Trader trader) throws RemoteException;
    
    public void sell(String traderName, Item item) throws RemoteException;
    
    public void wish(String traderName, Wish wish) throws RemoteException;
    // If item is not available, display message to make a wish instead
    public void buy(String traderName, Item item) throws RemoteException;
    
    public void listItems(String traderName, boolean allItems) throws RemoteException;
}
