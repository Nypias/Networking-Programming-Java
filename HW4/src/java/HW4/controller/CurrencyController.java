package HW4.controller;

import HW4.model.Currency;
import HW4.model.CurrencyDTO;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author tommi
 */
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Stateless
public class CurrencyController {
    @PersistenceContext(unitName = "CurrencyPU")
    private EntityManager em;
    private final String REQUEST_GET_ALL_NAME_CURRENCIES = "SELECT name FROM Currency";
    
    public CurrencyDTO createCurrency(String name, Double rate) {
        Currency newCurrency = new Currency(name, rate);
        em.persist(newCurrency);
        return newCurrency;
    }
    
    public Double convert (Double amount, String firstCurrencyName, String secondCurrencyName) {
        Currency firstCurrency = em.find(Currency.class, firstCurrencyName);
        Currency secondCurrency = em.find(Currency.class, secondCurrencyName);
        Double result = -1.0;
        if (firstCurrency != null && secondCurrency != null) {
            result = (amount / firstCurrency.getRate())*secondCurrency.getRate();
        } else if (firstCurrency == null) {
            throw new EntityNotFoundException("No currency with name" + firstCurrencyName);
        } else if (secondCurrency == null) {
            throw new EntityNotFoundException("No currency with name" + secondCurrencyName);
        }
        return result;
    }
    
    public List<String> getListCurrency () {
        return em.createNativeQuery(this.REQUEST_GET_ALL_NAME_CURRENCIES).getResultList();
    }
}
