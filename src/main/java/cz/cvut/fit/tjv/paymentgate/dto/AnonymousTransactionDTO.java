package cz.cvut.fit.tjv.paymentgate.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;
/**
 * Class represents a transaction without any confidential information.
 * It is used to send to client data that anyone can see
 */
public class AnonymousTransactionDTO {
    @NotNull
    private final Date date;
    @Size(min = 3, max = 3)
    @NotNull
    private final String currency;
    @NotNull
    private final Double amount;
    @NotNull

    private final AnonymousUserDTO sender;
    @NotNull

    private final AnonymousUserDTO receiver;

    /**
     * Variable is always set in service layer
     */
    private final Boolean successful;




    public AnonymousTransactionDTO(Date date, String currency, Double amount, AnonymousUserDTO sender, AnonymousUserDTO receiver, Boolean successful) {
        this.date = date;
        this.currency = currency;
        this.amount = amount;
        this.sender = sender;
        this.receiver = receiver;
        this.successful = successful;
    }

    public Date getDate() {
        return date;
    }


    public String getCurrency() {
        return currency;
    }



    public Double getAmount() {
        return amount;
    }

    public String getSenderLogin() {
        return sender.getLogin();
    }

    public String getReceiverLogin() {
        return receiver.getLogin();
    }

    public Boolean getSuccessful() {
        return successful;
    }


}
