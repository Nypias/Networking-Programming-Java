package marketplace;

import java.io.Serializable;

/**
 * Represents the Item of the marketplace.
 * If the item is not on sale but a trader has wished it
 * then the price is zero and only the wishedPrice has an actual value.
 * Two or more clients can wish for an Item with the same name but with 
 * DIFFERENT wished price.
 * @author teo
 */
public class Item implements Serializable {
    
    private String name;
    private int price;
    private int wishedPrice;
    private String requesterName;
    private String seller;

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }
    private String buyer;

    public int getWishedPrice() {
        return wishedPrice;
    }

    public void setWishedPrice(int wishedPrice) {
        this.wishedPrice = wishedPrice;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }
    
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Item(String name, int price) {
        this.name = name;
        this.price = price;
        this.wishedPrice = -1;
    }
    
    @Override
    public String toString(){
        return  "Name:"+name + "\n" +
                "Price: "+price + "\n" +
                "Seller: "+seller + "\n" +
                "Requester: "+ requesterName + "\n" +
                "Wished Price: "+wishedPrice;
        
    }
    
}
