package cz.cvut.fit.tjv.paymentgate.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * ORM class represents a bank card in database
 */
@Entity
@Table(name = "bank_card")
public class BankCard implements EntityWithId<Long>{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Long id;
    /**
     * Exactly four digits (1000-9999)
     */
    @Column(nullable = false, unique = true)
    private int cardNumber;
    /**
     * Exactly three digits (100-999)
     */
    @Column(nullable = false)
    private int cvv;
    @Column(nullable = false)
    private Date expirationDate;
    /**
     * CZK, USD or EUR
     */
    @Column(nullable = false)
    private String currency;
    @Column(nullable = false)
    private Double balance;
    @Column(nullable = false)
    private Boolean active;

    @ManyToMany
    @JoinTable(
            name = "ownedBy",
            joinColumns = @JoinColumn(name = "owns"),
            inverseJoinColumns = @JoinColumn(name = "ownedBy")
    )
    private Collection<User> holders;

    public BankCard() {}

    public BankCard(int cardNumber, int cvv, Date expirationDate, String currency, Double balance, Boolean active) {
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expirationDate = expirationDate;
        this.currency = currency;
        this.balance = balance;
        this.active = active;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @JsonIgnore
    public Collection<User> getHolders() {
        return holders;
    }
    @JsonIgnore
    public void addHolder(User holder) {
        if (holders == null) {
            holders = new ArrayList<>();
        }
        holders.add(holder);
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.valueOf(this.cardNumber);
    }
}
