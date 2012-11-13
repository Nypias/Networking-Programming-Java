package marketplace;

/**
 *
 * @author teo
 */
class Wish {
    
    private String name;
    private int price;
    private String requester;

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

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public Wish(String name, int price, String requester) {
        this.name = name;
        this.price = price;
        this.requester = requester;
    }
    
}