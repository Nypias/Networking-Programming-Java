package marketplace;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author teo
 */
@NamedQueries({
    @NamedQuery(
		name = "findTraderWithName",
    query = "SELECT trader FROM Traderlocal trader WHERE trader.username LIKE :userName")
})
@Entity(name = "Traderlocal")
public class Traderlocal implements Serializable {

    private static final long serialVersionUID = -4302632166699642391L;
    @Id
    @Column(name = "username", nullable = false)
    private String username;
    private String password;
    

    public Traderlocal() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
