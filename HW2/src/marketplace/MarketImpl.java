package marketplace;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import trader.Trader;

import tools.Utilities;

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
    private List<Wish> wishes = null;
    
    public MarketImpl(String marketName) throws RemoteException {
        super();
        this.marketName = marketName;
        this.items = new ArrayList<>();
        this.traders = new HashMap<>();
        this.wishes = new ArrayList<>();
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
        System.out.println("Trying to add item for sale");
        //Check if trader is registered
        if (!traders.containsKey(traderName)) {
            //traders.get(traderName).sendNotification("Register first!");
            System.out.println("Received request from unregistered trader!");
        } else {
            System.out.println("Selling item: " + item);
            if (!checkItemExists(item)) {
                items.add(item);
                traders.get(traderName).sendNotification(Utilities.ITEM_ADDED_SALE,"Item: " + item.getName() + "-" + item.getPrice() + " added for sale!");

                //Check if new Item corresponds to a wish. 
                for (Wish wish : wishes) {
                    System.out.println("Wish in loop:" + wish);
                    //If new item to be sold matches a wish, send callback
                    if (wish.getName().equals(item.getName()) && wish.getPrice() >= item.getPrice()) {
                        //Send to requester notification
                        traders.get(wish.getRequester()).sendNotification(Utilities.ITEM_WISHED_RECEIVED, "Your wished item has arrived!");
                        break;
                    }
                }
            } // If item to be sold already exists in list, send appropriate notification
            else {
                traders.get(traderName).sendNotification(Utilities.ITEM_ALREADY_EXISTS, "Item: " + item.getName() + "-" + item.getPrice() + " already exists!");
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
                if (it.getName().equals(item.getName()) && it.getPrice() < 0 && it.getPrice() <= item.getPrice()) {
                    
                    traders.get(traderName).sendNotification(Utilities.ITEM_WISHED_AVAILABLE, "Your wish can be served");
                    registerWish = false;
                    break;
                }
            }
            if (registerWish) {
                if (!checkItemExists(item)) {
                    items.add(item);
                    traders.get(traderName).sendNotification(Utilities.WISH_REGISTERED,"Your wish has been registered !");
                } else {
                    traders.get(traderName).sendNotification(Utilities.ITEM_WISHED_ALREADY_EXISTS, "That wished item already exists !");
                }
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
    public synchronized void buy(String traderName, Wish item) throws RemoteException {
        System.out.println("buy() :: " + item);
        
        if (!traders.containsKey(traderName)) {
            //trader.sendNotification("Register first!");
            System.out.println("Received request from unregistered trader!");
        } else {
            Item itemToRemove = null;
            //Find item to be bought and remove it from list of items
            for (Item currentItem : items) {
                System.out.println("currentItem:" + currentItem);
                //TODO: fix clause to consider buying a wished item.
                if (currentItem.getName().equals(item.getName()) && currentItem.getPrice() == item.getPrice()) {
                    //Remove item from list
                    itemToRemove = currentItem;
                    System.out.println("Found item to remove:"+itemToRemove);
                    break;
                }
            }
            System.out.println("buy() :: Removing item: " + itemToRemove);
            System.out.println("Size of items before: " + items.size());
            items.remove(itemToRemove);
            System.out.println("Size of items after: " + items.size());
            traders.get(traderName).sendNotification(Utilities.PRODUCT_BOUGHT, "Congratulations you bought the product!");
            traders.get(itemToRemove.getSeller()).sendNotification(Utilities.PRODUCT_SOLD, "Your item was sold");
        }
    }

    /**
     *
     * @param trader
     * @throws RemoteException
     */
    @Override
    public synchronized void listItems(String traderName) throws RemoteException {
        
        if (!traders.containsKey(traderName)) {
            //trader.sendNotification("Register first!");
            System.out.println("Received request from unregistered trader!");
        } else {
            StringBuilder itemsStr = new StringBuilder();
            for (Item i : items) {
                itemsStr.append(i.toString()).append("\n==========\n");
            }
            traders.get(traderName).sendNotification(Utilities.ALL_PRODUCTS_FROM_MARKET, items);
        }
    }

    /**
     * Search if given item exists in list of items in marketplace.
     *
     * @param item
     * @param wished
     * @return
     */
    private synchronized boolean checkItemExists(Item item) {
        for (Item it : items) {
            if (it.getName().equals(item.getName()) && it.getPrice() == item.getPrice()) {
                return true;
            }
        }
        return false;
    }
}
