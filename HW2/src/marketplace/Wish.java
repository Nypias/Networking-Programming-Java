package marketplace;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;

/**
 *
 * @author teo
 */
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
    private Date fullfilledDate;
    

    public Wish() {
    }

    public Date getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }

    public Date getFullfilledDate() {
        return fullfilledDate;
    }

    public void setFullfilledDate(Date fullfilledDate) {
        this.fullfilledDate = fullfilledDate;
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
