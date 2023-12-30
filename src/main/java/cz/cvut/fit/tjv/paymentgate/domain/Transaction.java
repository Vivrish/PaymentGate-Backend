package cz.cvut.fit.tjv.paymentgate.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.Date;
/**
 * ORM class represents a transaction in database
 */
@Entity
@Table(name = "transactions")
public class Transaction implements EntityWithId<Long>{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private Date date;
    /**
     * CZK, EUR or USD
     */
    @Column(nullable = false)
    private String currency;
    @Column(nullable = false)
    private Double amount;
    /**
     * Is set dynamically in business layer
     */
    @Column
    private Boolean successful = true;

    public Boolean getSuccessful() {
        return successful;
    }

    public void setSuccessful(Boolean successful) {
        this.successful = successful;
    }

    @ManyToOne
    @JoinColumn(name = "senderId")
    private User sender;
    @ManyToOne
    @JoinColumn(name = "receiverId")
    private User receiver;

    @JsonIgnore
    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    @JsonIgnore
    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }



    public Transaction(Date date, String currency, Double amount) {
        this.date = date;
        this.currency = currency;
        this.amount = amount;
    }
    public Transaction() {}


    public Date getDate() {
        return date;
    }

    public String getCurrency() {
        return currency;
    }



    public Double getAmount() {
        return amount;
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
        return this.date.toString();
    }

    public void setAllToDefault() {
        if (date == null) {
            date = new Date();
        }
        if (currency == null) {
            currency = "No data";
        }
        if (amount == null) {
            amount = 0.0;
        }
    }
}
