package cz.cvut.fit.tjv.paymentgate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Class represents a user without any confidential information.
 * It is used to send to client data that anyone can see
 */
public class AnonymousBankCardDTO {
    @Size(min = 1000, max = 9999)
    @NotNull
    private int cardNumber;
    @Size(min = 3, max = 3)
    @NotBlank
    private String currency;
    @NotNull
    private Double balance;
    @NotNull
    private Boolean active;
    private Collection<AnonymousUserDTO> holders = new ArrayList<>();

    public AnonymousBankCardDTO(int cardNumber, String currency, Double balance, Boolean active) {
        this.cardNumber = cardNumber;
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

    public Collection<AnonymousUserDTO> getHolders() {
        return holders;
    }

    public void setHolder(Collection<AnonymousUserDTO> holders) {
        this.holders = holders;
    }

    public void addHolder(AnonymousUserDTO userDto) {
        holders.add(userDto);
    }
}
