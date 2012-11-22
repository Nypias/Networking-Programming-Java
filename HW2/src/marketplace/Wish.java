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
 *
 * @author teo
 */
@NamedQueries({
    @NamedQuery(
		name = "getWishWithNamePrice",
    query = "SELECT mywish FROM Wish mywish WHERE mywish.name LIKE :fName AND mywish.price LIKE :fPrice")
})
@Entity
public class Wish implements Serializable {

    private static final long serialVersionUID = 717463913441944169L;
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private int price;
    private String requester;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date addedDate;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fulfilledDate;
    

    public Wish() {
    }

    public Date getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }

    public Date getFulfilledDate() {
        return fulfilledDate;
    }

    public void setFulfilledDate(Date fulfilledDate) {
        this.fulfilledDate = fulfilledDate;
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

    @Override
    public String toString() {
        return "Name:" + name + "\n"
                + "Price: " + price + "\n"
                + "Requester: " + requester + "\n";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
