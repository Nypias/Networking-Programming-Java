package HW4.view;

import HW4.controller.CurrencyController;
import HW4.controller.Utilities;
import javax.inject.Named;
import javax.enterprise.context.ConversationScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.inject.Inject;

/**
 *
 * @author tommi
 */
@Named(value = "currencyManager")
@ConversationScoped
public class CurrencyManager implements Serializable {
    private static final long serialVersionUID = 16247164405L;
    @EJB
    private CurrencyController currencyController;
    private Exception transactionFailure;
    private Double amount;
    private String firstCurrencyName;
    private String secondCurrencyName;
    @Inject
    private Conversation conversation;
    
    private String errorAmount;
    private Double currentAmountConverted;


    private void startConversation() {
        if (conversation.isTransient()) {
            conversation.begin();
        }
    }
    
    private void stopConversation() {
        if (!conversation.isTransient()) {
            conversation.end();
        }
    }
    
    private void handleException(Exception e) {
        stopConversation();
        e.printStackTrace(System.err);
        transactionFailure = e;
    }
    
    public Exception getException() {
        return transactionFailure;
    }
    
    public void convert() {
        try {
            startConversation();
            transactionFailure = null;
            if (this.amount > 0) {
                this.currentAmountConverted = currencyController.convert(this.amount, this.firstCurrencyName, this.secondCurrencyName);
                this.currentAmountConverted = Utilities.reduceDouble(this.currentAmountConverted);
            } else {
                this.errorAmount = "Please enter a positif amount of money";
            }
        } catch (Exception e) {
            handleException(e);
        }
    }
    
    public List<String> getListCurrency() {
        List<String> currencies = new ArrayList<String>();
        try {
            startConversation();
            transactionFailure = null;
            currencies.addAll(currencyController.getListCurrency());
        } catch (Exception e) {
            handleException(e);
        } finally {
            return currencies;
        }
    }

    public Double getAmount() {
        return amount;
    }

    public String getFirstCurrency() {
        return firstCurrencyName;
    }

    public String getSecondCurrency() {
        return secondCurrencyName;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setFirstCurrency(String firstCurrency) {
        this.firstCurrencyName = firstCurrency;
    }

    public void setSecondCurrency(String secondCurrency) {
        this.secondCurrencyName = secondCurrency;
    }
    
    public Double getCurrentAmountConverted() {
        return currentAmountConverted;
    }
    
    public void setCurrentAmountConverted(Double currentAmountConverted) {
        this.currentAmountConverted = currentAmountConverted;
    }
    
    public String getErrorAmount() {
        return errorAmount;
    }

    public void setErrorAmount(String errorAmount) {
        this.errorAmount = errorAmount;
    }
    
    
}
