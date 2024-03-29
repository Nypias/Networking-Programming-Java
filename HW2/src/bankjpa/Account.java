package bankjpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.LockModeType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Version;

@NamedQueries({
    @NamedQuery(
		name = "deleteAccountWithName",
    query = "DELETE FROM Account acct WHERE acct.owner.name LIKE :ownerName"),
    @NamedQuery(
		name = "findAccountWithName",
    query = "SELECT acct FROM Account acct WHERE acct.owner.name LIKE :ownerName",
    lockMode = LockModeType.OPTIMISTIC)
})
@Entity(name = "Account")
public class Account implements Serializable {

    private static final long serialVersionUID = -4302632166699642491L;
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long accountId;
    @Column(name = "balance", nullable = false)
    private float balance;
    @OneToOne//(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner", nullable = false)
    private Owner owner;
    @Version
    @Column(name = "OPTLOCK")
    private int versionNum;

    public Account() {
        this(null, 0);
    }

    public Account(Owner owner, float balance) {
        this.owner = owner;
        this.balance = balance;
    }

    public float getBalance() {
        return balance;
    }

    public void deposit(float value) throws RejectedException {
        System.out.println("Check2");
        if (value < 0) {
            throw new RejectedException("Rejected: Account " + owner.getName()
                    + ": Illegal value: " + value);
        }

        balance += value;
        System.out.println("Transaction: Account " + owner.getName() + ": deposit: $"
                + value + ", balance: $" + balance);
    }

    public void withdraw(float value) throws RejectedException {
        if (value < 0) {
            throw new RejectedException("Rejected: Account " + owner.getName()
                    + ": Illegal value: " + value);
        }

        if ((balance - value) < 0) {
            throw new RejectedException("Rejected: Account " + owner.getName()
                    + ": Negative balance on withdraw: " + (balance - value));
        }

        balance -= value;
        System.out.println("Transaction: Account " + owner.getName() + ": deposit: $"
                + value + ", balance: $" + balance);
    }

    @Override
    public String toString() {
        return "Account for " + owner.getName() + " has balance $" + balance;
    }
}