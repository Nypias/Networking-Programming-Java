/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package HW4.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author tommi
 */
@Entity
public class Currency implements Serializable, CurrencyDTO {
    private static final long serialVersionUID = 1L;
    @Id
    private String name;
    private Double rate;

    public Currency() {
    }
    
    public Currency (String name, Double rate) {
        this.name = name;
        this.rate = rate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (name != null ? name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Currency)) {
            return false;
        }
        Currency other = (Currency) object;
        if ((this.name == null && other.name != null) || (this.name != null && !this.name.equals(other.name))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "HW4.model.Currency[ id=" + name + " ]";
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Double getRate() {
        return this.rate;
    }
}
