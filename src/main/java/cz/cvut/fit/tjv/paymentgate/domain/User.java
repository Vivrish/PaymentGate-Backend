package cz.cvut.fit.tjv.paymentgate.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.cvut.fit.tjv.paymentgate.exception.CardNumberDoesNotExistException;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
/**
 * ORM class represents a user in database
 */
@Entity
@Table(name = "user_database")
public class User implements EntityWithId<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String surname;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false, unique = true)
    private String login;
    /**
     * Must be complex: at least 7 symbols, one uppercase letter and one digit
     */
    @Column(nullable = false)
    private String password;

    @ManyToMany(mappedBy = "holders", cascade = CascadeType.ALL)
   private Collection<BankCard> bankCards;

    /**
     * This card will be user for all the operations
     */
    @JsonIgnore
    private int preferredCardNumber;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private Collection<Transaction> transactionsSent;
    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private Collection<Transaction> transactionsReceived;

    public User(String name, String surname, String email, String login, String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.login = login;
        this.password = password;
    }
    public User() {}




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<BankCard> getBankCards() {
        return bankCards;
    }

    public void setBankCards(Collection<BankCard> bankCards) {
        this.bankCards = bankCards;
    }

    @JsonIgnore
    public Collection<Transaction> getTransactionsSent() {
        return transactionsSent;
    }

    @JsonIgnore
    public Collection<Transaction> getTransactionsReceived() {
        return transactionsReceived;
    }

    public void addOutgoingTransaction(Transaction transaction) {
        if (transactionsSent == null) {
            transactionsSent = new ArrayList<>();
        }
        transactionsSent.add(transaction);
    }

    public void addIngoingTransaction(Transaction transaction) {
        if (transactionsReceived == null) {
            transactionsReceived = new ArrayList<>();
        }
        transactionsReceived.add(transaction);
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public int getPreferredCardNumber() {
        return preferredCardNumber;
    }

    public void setPreferredCardNumber(int preferredCardNumber) {
        if (!hasBankCard(preferredCardNumber)) {
            throw new CardNumberDoesNotExistException();
        }
        this.preferredCardNumber = preferredCardNumber;
    }

    public void setTransactionsSent(Collection<Transaction> transactionsSent) {
        this.transactionsSent = transactionsSent;
    }

    public void setTransactionsReceived(Collection<Transaction> transactionsReceived) {
        this.transactionsReceived = transactionsReceived;
    }

    @JsonIgnore
    public void addBankCard(BankCard bankCard) {
        if (bankCards == null) {
            bankCards = new ArrayList<>();
        }
        bankCards.add(bankCard); }


    @JsonIgnore
    @Override
    public String toString() {
        return this.name;
    }



    public Optional<BankCard> getPreferred() {
        if (bankCards == null) {
            return Optional.empty();
        }
        for (BankCard bankCard: this.bankCards) {
            if (bankCard.getCardNumber() == this.preferredCardNumber) {
                return Optional.of(bankCard);
            }
        }
        return Optional.empty();
    }

    private boolean hasBankCard(int number) {
        if (bankCards == null) {
            return false;
        }
        for (BankCard bankCard: this.bankCards) {
            if (bankCard.getCardNumber() == number) {
                return true;
            }
        }
        return false;
    }

}
