package marketplace;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tools.Utilities;
import trader.Trader;
import bank.Bank;
import bank.RejectedException;

/**
 *
 * @author teo
 */
public class MarketImpl extends UnicastRemoteObject implements Market {

    private static final long serialVersionUID = 3454174232092028530L;
    private String marketName;
    private String bankName;
    // Stores all items of the market
    private List<Item> items = null;
    // Stores <TraderID,Trader remote reference>
    private Map<String, Trader> traders = null;
    private List<Wish> wishes = null;
    private Bank bankObj = null;

    /**
     *
     * @param marketName
     * @param marketName
     * @throws RemoteException
     */
    public MarketImpl(String marketName, String bankName) throws RemoteException {
        super();
        this.marketName = marketName;
        this.bankName = bankName;
        this.items = new ArrayList<>();
        this.traders = new HashMap<>();
        this.wishes = new ArrayList<>();
        try {
            this.bankObj = (Bank) Naming.lookup(/*"rmi:/localhost:1099/" + */bankName);
        } catch (NotBoundException | MalformedURLException ex) {
            ex.printStackTrace();
        }
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
                traders.get(traderName).sendNotification(Utilities.ITEM_ADDED_SALE, "Item: " + item.getName() + "-" + item.getPrice() + " added for sale!");
                List<Wish> wishesToRemove = new ArrayList<>();
                //Check if new Item corresponds to a wish. 
                for (Wish wish : wishes) {
                    System.out.println("Wish in loop:" + wish);
                    //If new item to be sold matches a wish, send callback
                    if (wish.getName().equalsIgnoreCase(item.getName()) && wish.getPrice() >= item.getPrice()) {
                        //Send to requester notification
                        wishesToRemove.add(wish);
                        traders.get(wish.getRequester()).sendNotification(Utilities.ITEM_WISHED_RECEIVED, "Your wished item has arrived!");
                    }
                }

                //Remove wishes
                for (Wish wishToRem : wishesToRemove) {
                    System.out.println("sell() :: Removing wish :" + wishToRem);
                    wishes.remove(wishToRem);
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
     * wish first, wins). We add wishes with same properties and notify all
     * requesters (wishers). If wished item already exists, we do not record the
     * wish, instead we immediately send the notification to the requester.
     *
     * @param trader
     * @param item
     * @throws RemoteException
     */
    @Override
    public synchronized void wish(String traderName, Wish wish) throws RemoteException {
        //Check if trader is registered
        if (!traders.containsKey(traderName)) {
            //traders.get(traderName).sendNotification("Register first!");
            System.out.println("Received request from unregistered trader!");
        } else {
            System.out.println("wish() :: wish:" + wish);
            //Search wishes and see if there is a wish with the same properties
            boolean sameWishFound = false;
            for (Wish currentWish : wishes) {
                if (currentWish.getName().equalsIgnoreCase(wish.getName()) && currentWish.getPrice() == wish.getPrice() && currentWish.getRequester().equalsIgnoreCase(wish.getRequester())) {
                    sameWishFound = true;
                }
            }
            if (sameWishFound) {
                traders.get(traderName).sendNotification(Utilities.WISH_ALREADY_REGISTERED, "Wish already registered");
            } else {
                //Check if wish matches item
                boolean wishedItemFound = false;
                for (Item it : items) {
                    System.out.println("it:" + it);
                    if (it.getName().equalsIgnoreCase(wish.getName()) && it.getPrice() <= wish.getPrice()) {
                        traders.get(traderName).sendNotification(Utilities.WISH_CAN_BE_SERVED, "Your wish can be served");
                        wishedItemFound = true;
                        break;
                    }
                }
                if (!wishedItemFound) {
                    wishes.add(wish);
                    traders.get(traderName).sendNotification(Utilities.WISH_REGISTERED, "Your wish has been registered");
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
    public synchronized void buy(String traderName, Item item) throws RemoteException {
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
                if (currentItem.getName().equalsIgnoreCase(item.getName()) && currentItem.getPrice() == item.getPrice()) {
                    //Check if buyer has enough account balance for the Item
                    float traderBalance = bankObj.getAccount(traderName).getBalance();
                    if (traderBalance >= currentItem.getPrice()) {
                        //Remove item from list
                        itemToRemove = currentItem;
                        System.out.println("Found item to remove:" + itemToRemove);
                    } else {
                        traders.get(traderName).sendNotification(Utilities.BALANCE_INSUFFICIENT, "Not enough money for item");
                    }
                    break;
                }
            }
            System.out.println("buy() :: Removing item: " + itemToRemove);
            System.out.println("Size of items before: " + items.size());
            if (itemToRemove != null) {
                //Put transaction in try-catch for atomic transaction
                try {
                    //Withdraw money from buyer
                    bankObj.getAccount(traderName).withdraw((float) itemToRemove.getPrice());
                    //Deposit money to seller
                    bankObj.getAccount(itemToRemove.getSeller()).deposit((float) itemToRemove.getPrice());
                    //Remove item from list
                    items.remove(itemToRemove);
                    System.out.println("Size of items after: " + items.size());
                    traders.get(traderName).sendNotification(Utilities.PRODUCT_BOUGHT, "Congratulations you bought the product!");
                    traders.get(itemToRemove.getSeller()).sendNotification(Utilities.PRODUCT_SOLD, "Your item was sold");

                } catch (RemoteException | RejectedException ex) {
                    ex.printStackTrace();
                } finally {
                    //TODO: Rollback transactions
                }
            }
        }
    }

    /**
     *
     * @param trader
     * @throws RemoteException
     */
    @Override
    public synchronized void listItems(String traderName, boolean allItems) throws RemoteException {

        if (!traders.containsKey(traderName)) {

            System.out.println("Received request from unregistered trader!");
        } else {
            if (allItems) {
                traders.get(traderName).sendNotification(Utilities.ALL_PRODUCTS_FROM_MARKET, items);
            } else {
                List<Item> tradersItems = new ArrayList<>();
                for (Item item : items) {
                    if (item.getSeller() != null && item.getSeller().equalsIgnoreCase(traderName)) {
                        tradersItems.add(item);
                    }
                }
                traders.get(traderName).sendNotification(Utilities.ALL_PRODUCTS_FROM_MARKET, tradersItems);
            }
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
            if (it.getName().equalsIgnoreCase(item.getName()) && it.getPrice() == item.getPrice()) {
                return true;
            }
        }
        return false;
    }
}
