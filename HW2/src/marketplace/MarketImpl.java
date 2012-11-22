package marketplace;


import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;

import tools.Utilities;
import trader.Trader;
import bankjpa.Bank;
import bankjpa.RejectedException;

/**
 *
 * @author teo
 */
public class MarketImpl extends UnicastRemoteObject implements Market {

    private static final long serialVersionUID = 3454174232092028530L;
    // Stores all items of the market
    private List<Item> items = null;
    // Stores <TraderID,Trader remote reference>
    private Map<String, Trader> tradersRemote = null;
    private List<Wish> wishes = null;
    private Bank bankObj = null;
    private EntityManagerFactory emFactory;

    /**
     *
     * @param marketName
     * @param marketName
     * @throws RemoteException
     */
    public MarketImpl(String marketName, String bankName) throws RemoteException {
        super();
        emFactory = Persistence.createEntityManagerFactory(Utilities.datasource);
        //Load all items in List of Items
        EntityManager em = null;
        try {
            em = beginTransaction();
            this.items = em.createNamedQuery("getItemWithNamePrice").
                    setParameter("fName", "%%").setParameter("fPrice", "%%").getResultList();
            this.wishes = em.createNamedQuery("getWishWithNamePrice").
                    setParameter("fName", "%%").setParameter("fPrice", "%%").getResultList();
            System.out.println("Wishes:"+wishes.toString());
        } catch (Exception ex) {
        	System.out.println(ex.getMessage());
        } finally {
            commitTransaction(em);
        }


        this.tradersRemote = new HashMap<>();
        
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
    public synchronized String register(Trader trader, String password) throws RemoteException {
        if (!isPasswordValid(password, Utilities.PASSWORD_LENGTH)) {
            trader.sendNotification(Utilities.PASSWORD_LENGTH_INVALID, "Invalid password length");
        } else {
            EntityManager em = null;
            try {
                em = beginTransaction();
                String traderName = trader.getName();
                List tradersToAdd = null;


                tradersToAdd = em.createNamedQuery("findTraderWithName").
                        setParameter("userName", traderName).getResultList();

                if (!tradersToAdd.isEmpty()) {
                    trader.sendNotification(Utilities.USER_ALREADY_REGISTERED, "User already registered");
                } else {
                    System.out.println("register() :: registering trader - " + traderName);
                    // create account.
                    Traderlocal newTrader = new Traderlocal();
                    newTrader.setUsername(traderName);
                    newTrader.setPassword(password);
                    em.persist(newTrader);
                    tradersRemote.put(traderName, trader);
                    //tradersLocal.put(traderName, newTrader);
                    System.out.println("register() :: register=" + tradersRemote.keySet());
                    trader.sendNotification(Utilities.REGISTRATION_SUCCESSFUL, "Successful registration");
                    return "Successful registration";
                }
            } catch (Exception ex) {
                ex.printStackTrace();

            } finally {
                commitTransaction(em);

            }
        }
        return "balabla";

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
    public synchronized String unregister(String traderName) throws RemoteException {
        EntityManager em = null;
        try {
            em = beginTransaction();
            System.out.println("unregister() :: unregistering trader - " + traderName);
            Traderlocal traderToRemove = null;
            try {
                traderToRemove = (Traderlocal) em.createNamedQuery("findTraderWithName").
                        setParameter("userName", traderName).getSingleResult();
            } catch (NoResultException ex) {
            }
            if (traderToRemove != null) {
                System.out.println("unregister() :: Trying to unregister user - " + traderName);
                tradersRemote.remove(traderName);
                em.remove(traderToRemove);
                return "Successful registration";
            }

        } catch (Exception ex) {
            ex.printStackTrace();

        } finally {
            commitTransaction(em);
        }
        return "ERROR while unregistering";

    }

    /**
     *
     */
    @Override
    public boolean login(Trader trader, String password) throws RemoteException {
        EntityManager em = null;
        try {
            em = beginTransaction();
            String traderName = trader.getName();
            Traderlocal traderFound = null;
            try {
                traderFound = (Traderlocal) em.createNamedQuery("findTraderWithName").
                        setParameter("userName", traderName).getSingleResult();
            } catch (NoResultException ex) {
            }
            if (traderFound == null) {
                trader.sendNotification(Utilities.LOGIN_INVALID, "Invalid username or password");
                return false;
            } else if (!traderFound.getPassword().equals(password)) {
                trader.sendNotification(Utilities.LOGIN_INVALID, "Invalid username or password");
                return false;
            } else {
                tradersRemote.put(traderName, trader);
                trader.sendNotification(Utilities.LOGIN_SUCCESSFUL, "You have successfully logged in!");
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();

        } finally {
            commitTransaction(em);
        }

        return false;
    }

    /**
     * Notify traders to refresh their list.
     */
    private synchronized void notifyTraders() {
        List<Item> itemsToReturn = new ArrayList<>();
        //Return all items currently for sale by all traders
        for (Item item : items) {
            if (item.getSoldDate() == null) {
                itemsToReturn.add(item);
            }
        }
        for (String traderName : tradersRemote.keySet()) {
            try {
                System.out.println("Sending notification to :" + traderName);
                tradersRemote.get(traderName).sendNotification(Utilities.ALL_PRODUCTS_FROM_MARKET, itemsToReturn);
            } catch (RemoteException ex) {
                ex.printStackTrace();
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
    public synchronized void sell(String traderName, Item item) throws RemoteException {
        //Check if trader is registered
        if (!tradersRemote.containsKey(traderName)) {
            System.out.println("sell() :: Received request from unregistered trader!");
        } else {
            if (!checkItemExists(item)) {
                EntityManager em = null;
                System.out.println("sell() :: Selling item: " + item);

                try {
                    em = beginTransaction();
                    item.setSellDate(new Date());
                    items.add(item);
                    em.persist(item);
                    tradersRemote.get(traderName).sendNotification(Utilities.ITEM_ADDED_SALE, "Item: " + item.getName() + "-" + item.getPrice() + " added for sale!");
                    notifyTraders();
                    List<Wish> wishesToFulFill = new ArrayList<>();
                    //Check if new Item corresponds to a wish. 
                    for (Wish wish : wishes) {
                        //If new item to be sold matches a wish, send callback
                        if (wish.getFulfilledDate() == null && wish.getName().equalsIgnoreCase(item.getName()) && wish.getPrice() >= item.getPrice()) {
                            //Send to requester notification
                            wishesToFulFill.add(wish);
                            System.out.println("Sending wish: "+wish+" to trader:"+traderName);
                            tradersRemote.get(wish.getRequester()).sendNotification(Utilities.ITEM_WISHED_RECEIVED, "Your wished item has arrived!");
                        }
                    }

                    //Update wishes
                    for (Wish wishToFulFill : wishesToFulFill) {
                        System.out.println("sell() :: Updating wish :" + wishToFulFill);
                        wishToFulFill.setFulfilledDate(new Date());
                        Wish wishToUpdate = em.find(Wish.class, wishToFulFill.getId());
                        wishToUpdate.setFulfilledDate(wishToFulFill.getFulfilledDate());

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    commitTransaction(em);
                }

            } // If item to be sold already exists in list, send appropriate notification
            else {
                tradersRemote.get(traderName).sendNotification(Utilities.ITEM_ALREADY_EXISTS, "Item: " + item.getName() + "-" + item.getPrice() + " already exists!");
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
        if (!tradersRemote.containsKey(traderName)) {
            System.out.println("wish() :: Received request from unregistered trader!");
        } else {
            System.out.println("wish() :: wish:" + wish);
            //Search wishes and see if there is a wish with the same properties
            boolean sameWishFound = false;
            for (Wish currentWish : wishes) {
                if (currentWish.getFulfilledDate() != null && currentWish.getName().equalsIgnoreCase(wish.getName()) && currentWish.getPrice() == wish.getPrice() && currentWish.getRequester().equalsIgnoreCase(wish.getRequester())) {
                    sameWishFound = true;
                }
            }
            if (sameWishFound) {
                tradersRemote.get(traderName).sendNotification(Utilities.WISH_ALREADY_REGISTERED, "Wish already registered");
            } else {
                //Check if wish matches item
                boolean wishedItemFound = false;
                for (Item it : items) {
                    System.out.println("wish() :: it = " + it);
                    if (it.getBuyer() == null && it.getName().equalsIgnoreCase(wish.getName()) && it.getPrice() <= wish.getPrice()) {
                        tradersRemote.get(traderName).sendNotification(Utilities.WISH_CAN_BE_SERVED, "Your wish can be served");
                        wishedItemFound = true;
                        break;
                    }
                }
                if (!wishedItemFound) {
                    EntityManager em = null;
                    try {
                        em = beginTransaction();
                        wish.setAddedDate(new Date());
                        wishes.add(wish);
                        em.persist(wish);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } finally {
                        commitTransaction(em);
                    }
                    tradersRemote.get(traderName).sendNotification(Utilities.WISH_REGISTERED, "Your wish has been registered");
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

        if (!tradersRemote.containsKey(traderName)) {
            System.out.println("buy() :: Received request from unregistered trader!");
        } else {
            Item itemToBuy = null;
            //Find item to be bought and remove it from list of items
            for (Item currentItem : items) {
                System.out.println("buy() :: currentItem:" + currentItem);
                //TODO: fix clause to consider buying a wished item.
                if (currentItem.getBuyer() == null && currentItem.getName().equalsIgnoreCase(item.getName()) && currentItem.getPrice() == item.getPrice()) {
                    //Check if buyer has enough account balance for the Item
                    float traderBalance = bankObj.findAccount(traderName).getBalance();
                    if (traderBalance >= currentItem.getPrice()) {
                        //Remove item from list
                        itemToBuy = currentItem;
                    } else {
                        tradersRemote.get(traderName).sendNotification(Utilities.BALANCE_INSUFFICIENT, "Balance Insufficient : You don't have enough money for that item");
                    }
                    break;
                }
            }
            System.out.println("buy() :: Buying item - " + itemToBuy);
            if (itemToBuy != null) {
                EntityManager em = null;
                try {
                    em = beginTransaction();
                    //Fetch itemtobuy from DB and update it
                    Item itemToBuyDB = em.find(Item.class, itemToBuy.getId());
                    //Withdraw money from buyer
                    bankObj.withdraw(traderName, (float) itemToBuy.getPrice());
                    //Deposit money to seller
                    bankObj.deposit(itemToBuy.getSeller(), (float) itemToBuy.getPrice());
                    //Mark item as bought
                    itemToBuyDB.setBuyer(traderName);
                    itemToBuyDB.setSoldDate(new Date());
                    itemToBuy.setBuyer(traderName);
                    itemToBuy.setSoldDate(new Date());
                    tradersRemote.get(traderName).sendNotification(Utilities.PRODUCT_BOUGHT, "Congratulations you bought the product!");
                    tradersRemote.get(itemToBuy.getSeller()).sendNotification(Utilities.PRODUCT_SOLD, "Your item was sold");
                    notifyTraders();
                } catch (RemoteException | RejectedException ex) {
                    ex.printStackTrace();
                } finally {
                    commitTransaction(em);
                }
            } else {
                tradersRemote.get(traderName).sendNotification(Utilities.ITEM_NOT_FOUND, "The requested item was not found!");

            }
        }
    }

    /**
     *
     * @param trader
     * @throws RemoteException
     */
    @Override
    public synchronized void listItems(String traderName, int type) throws RemoteException {
        //List of items to send to trader
        List<Item> itemsToReturn = new ArrayList<>();
        switch (type) {
            //Return all items currently for sale by all traders
            case Utilities.ALL_PRODUCTS_FROM_MARKET:
                for (Item item : items) {
                    if (item.getSoldDate() == null) {
                        itemsToReturn.add(item);
                    }
                }
                tradersRemote.get(traderName).sendNotification(Utilities.ALL_PRODUCTS_FROM_MARKET, itemsToReturn);
                break;
            //Return all items currently for sale by calling trader
            case Utilities.LISTITEMS_ON_SALE_TRADER:
                for (Item item : items) {
                    if (item.getSoldDate() == null && item.getSeller().equals(traderName)) {
                        itemsToReturn.add(item);
                    }
                }
                tradersRemote.get(traderName).sendNotification(Utilities.LISTITEMS_ON_SALE_TRADER, itemsToReturn);
                break;
            //Return all items bought by calling trader
            case Utilities.LISTITEMS_BOUGHT_TRADER:
                for (Item item : items) {
                    if (item.getSoldDate() != null && item.getBuyer().equals(traderName)) {
                        itemsToReturn.add(item);
                    }
                }
                tradersRemote.get(traderName).sendNotification(Utilities.LISTITEMS_BOUGHT_TRADER, itemsToReturn);
                break;
            //Return all items wished by calling trader
            case Utilities.LISTITEMS_WISHED_TRADER:
                List<Wish> wishesToReturn = new ArrayList<>();
                for (Wish wish : wishes) {
                    if (wish.getFulfilledDate() == null && wish.getRequester().equals(traderName)) {
                        wishesToReturn.add(wish);
                    }
                }
                tradersRemote.get(traderName).sendNotification(Utilities.LISTITEMS_WISHED_TRADER, wishesToReturn);
                break;
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
            if (it.getBuyer() == null && it.getName().equalsIgnoreCase(item.getName()) && it.getPrice() == item.getPrice() && it.getBuyer() == null) {
                return true;
            }
        }
        return false;
    }

    private EntityManager beginTransaction() {
        EntityManager em = emFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        return em;
    }

    private void commitTransaction(EntityManager em) {
        em.getTransaction().commit();
        em.close();
    }

    /**
     * Checks password length against parameter.
     *
     * @param password
     * @param length
     * @return
     */
    private boolean isPasswordValid(String password, int length) {
        if (password != null || password.length() >= length) {
            return true;
        }
        System.out.println("isPasswordValid() :: Invalid Password");
        return false;
    }

    @Override
    public void logout(String traderName) throws RemoteException {
        tradersRemote.get(traderName).sendNotification(Utilities.USER_SUCCESSFUL_LOGOUT, "You have logged out!");
        tradersRemote.remove(traderName);
    }
}
