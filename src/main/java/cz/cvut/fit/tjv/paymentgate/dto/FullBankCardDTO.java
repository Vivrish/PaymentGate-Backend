package cz.cvut.fit.tjv.paymentgate.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;
/**
 * Class represents a bankcard with full information about it.
 * It is used to send to client data that not anyone can see
 */
public class FullBankCardDTO {
    @Size(min = 1000, max = 9999)
    @NotNull
    private int cardNumber;
    @Size(min = 100, max = 999)
    @NotNull
    private int cvv;
    @NotNull
    private Date expirationDate;
    @Size(min = 3, max = 3)
    @NotBlank
    private String currency;
    @NotNull
    private Double balance;
    @NotNull
    private Boolean active;


    public FullBankCardDTO(int cardNumber, int cvv, Date expirationDate, String currency, Double balance, Boolean active) {
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
