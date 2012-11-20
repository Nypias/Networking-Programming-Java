package marketplace;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;

/**
 * Represents the Item of the marketplace.
 *
 * @author teo
 */
@NamedQueries({
    @NamedQuery(
		name = "getItemWithNamePrice",
    query = "SELECT myitem FROM Item myitem WHERE myitem.name LIKE :fName AND myitem.price LIKE :fPrice")
})
@Entity
public class Item implements Serializable {

    private static final long serialVersionUID = 730717577711231086L;
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private int price;
    private String seller;
    private String buyer;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date sellDate;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date soldDate;

    public Item() {
    }

    public Date getSoldDate() {
        return soldDate;
    }

    public void setSoldDate(Date soldDate) {
        this.soldDate = soldDate;
    }

    public Date getSellDate() {
        return sellDate;
    }

    public void setSellDate(Date sellDate) {
        this.sellDate = sellDate;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
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

    public Item(String name, int price, String seller) {
        this.name = name;
        this.price = price;
        this.seller = seller;
    }

    @Override
    public String toString() {
        return "Name:" + name + "\n"
                + "Price: " + price + "\n"
                + "Seller: " + seller + "\n";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
