package HW4.model;

/**
 *
 * @author tommi
 */
public interface CurrencyDTO {
    /**
     * Gets the name of the currency
     * 
     * @return the name of the currency
     */
    String getName();
    
    /**
     * Gets the rate of the currency in function of USD Rate
     * 
     * @return the rate of the currency
     */
    Double getRate();
}
