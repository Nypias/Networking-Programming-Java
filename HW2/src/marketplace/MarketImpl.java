package marketplace;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import trader.Trader;

/**
 *
 * @author teo
 */
public class MarketImpl extends UnicastRemoteObject implements Market {

    private String marketName;
    // Stores all items of the market
    private List<Item> items = null;
    // Stores <TraderID,Trader remote reference>
    private Map<String, Trader> traders = null;

    public MarketImpl(String marketName) throws RemoteException {
        super();

        this.marketName = marketName;
        items = new ArrayList<Item>();
        traders = Collections.synchronizedMap(new HashMap<String, Trader>());
    }

    /**
     * Checks if trader exists with that name and if not registers the new
     * trader.
     *
     * @param trader
     * @return
     * @throws RemoteException
     */
    @Override
    public synchronized String register(Trader trader) throws RemoteException {
        String traderName = trader.getName();
        if (!traders.containsKey(traderName)) {
            traders.put(traderName, trader);
            System.out.println("MarketImpl.register() :: register=" + traders.keySet());
            return "SUCCESS";
        } else {
            return "TRADER WITH THAT NAME ALREADY EXISTS";
        }

    }

    /**
     * Unregisters the given trader, if one exists with that name in the traders
     * Map.
     *
     * @param trader
     * @return
     * @throws RemoteException
     */
    @Override
    public synchronized String unregister(Trader trader) throws RemoteException {
        String traderName = trader.getName();
        if (traders.containsKey(traderName)) {
            traders.remove(traderName);
            System.out.println("MarketImpl.unregister() :: Traders=" + traders);
            return "SUCCESS";
        } else {
            return "TRADER NOT FOUND";
        }
    }

    /**
     *
     * @param trader
     * @param item
     * @throws RemoteException
     */
    @Override
    public synchronized void sell(String traderName, Item item) throws RemoteException {
        //Check if trader is registered
        if (!traders.containsKey(traderName)) {
            //traders.get(traderName).sendNotification("Register first!");
            System.out.println("Received request from unregistered trader!");
        } //SELL
        else {
            //Check if new Item corresponds to a wish. 
            //Wished items have a price of '-1'.
            boolean addToItems = true;
           
            for (Item it : items) {
                System.out.println("it:" + it+"============");
                System.out.println("it2:" + item);
                //If new item to be sold matches a wish, send callback
                if (it.getName().equals(item.getName()) && it.getPrice() < 0 && it.getWishedPrice() >= item.getPrice()) {
                    traders.get(it.getRequesterName()).sendNotification("Your wished item has arrived!");
                    item.setRequesterName(it.getRequesterName());
                    
                    addToItems = false;
                    break;
                }
            }
            System.out.println("Trying to add item for sale");
            if (addToItems) {
                if (!checkItemExists(item, true)) {
                    items.add(item);
                    traders.get(traderName).sendNotification("Item: " + item.getName() + "-" + item.getPrice() + " added for sale!");
                } else {
                    traders.get(traderName).sendNotification("Item: " + item.getName() + "-" + item.getPrice() + " already exists!");
                }
            } else {
                System.out.println("sell() :: Calling buy");
                buy(traders.get(item.getRequesterName()), item);
            }
        }
    }

    /**
     * Places a wish in the items Map. If an item with the same name and wished
     * price already exists, the wish can not be registered (whoever makes the
     * wish first, wins).
     *
     * @param trader
     * @param item
     * @throws RemoteException
     */
    @Override
    public synchronized void wish(String traderName, Item item) throws RemoteException {
        //Check if trader is registered
        if (!traders.containsKey(traderName)) {
            //traders.get(traderName).sendNotification("Register first!");
            System.out.println("Received request from unregistered trader!");
        } else {
            //TODO: Check if wish matches item
            boolean registerWish = true;
            for (Item it : items) {
                System.out.println("it:" + it);
                System.out.println("it2:" + it);
                if (it.getName().equals(item.getName()) && it.getWishedPrice() < 0 && it.getPrice() <= item.getWishedPrice()) {

                    traders.get(traderName).sendNotification("Your wish can be served");

                    registerWish = false;
                    break;
                }
            }
            if (registerWish) {
                if (!checkItemExists(item, false)) {

                    items.add(item);
                    traders.get(traderName).sendNotification("Your wish has been registered !");
                } else {
                    traders.get(traderName).sendNotification("That wished item already exists !");
                }
            } // Buy() is called here to prevent concurrent modification
            else {
                buy(traders.get(traderName), item);
            }
        }
    }

    /**
     *
     * @param trader
     * @param item
     * @throws RemoteException
     */
    @Override
    public synchronized void buy(Trader trader, Item item) throws RemoteException {
        System.out.println("buy() :: "+item);
        String traderName = trader.getName();
        if (!traders.containsKey(traderName)) {
            //trader.sendNotification("Register first!");
            System.out.println("Received request from unregistered trader!");
        } else {

            //Notify seller
            String sellerName = null;
            for (Item it : items) {
                if (it.getName().equals(item.getName()) && it.getPrice() == item.getPrice()) {
                    sellerName = it.getSeller();
                    //Remove item from list
                    items.remove(it);
                    break;
                }
            }
            trader.sendNotification("Congratulations you bought the product!");
            traders.get(item.getSeller()).sendNotification("Your item was sold");
        }
    }

    /**
     *
     * @param trader
     * @throws RemoteException
     */
    @Override
    public synchronized void listItems(Trader trader) throws RemoteException {
        String traderName = trader.getName();
        if (!traders.containsKey(traderName)) {
            //trader.sendNotification("Register first!");
            System.out.println("Received request from unregistered trader!");
        } else {
            StringBuilder itemsStr = new StringBuilder();
            for (Item i : items) {
                itemsStr.append(i.toString()).append("\n==========\n");
            }
            trader.sendNotification("Items at market:\n" + itemsStr.toString());
        }
    }

    /**
     * Boolean indicates if we search for a wished item or an actual one.
     *
     * @param item
     * @param wished
     * @return
     */
    private synchronized boolean checkItemExists(Item item, boolean toSell) {
        for (Item it : items) {
            if (!toSell) {
                if (it.getName().equals(item.getName()) && it.getWishedPrice() == item.getWishedPrice()) {
                    return true;
                }
            } else {
                if (it.getName().equals(item.getName()) && it.getPrice() == item.getPrice()) {
                    return true;
                }
            }
        }
        return false;
    }
}
