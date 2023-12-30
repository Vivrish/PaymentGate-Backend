package cz.cvut.fit.tjv.paymentgate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;
/**
 * Class represents a user with their transactions without any confidential information.
 * It is used to send to client data that anyone can see
 */
public class AnonymousUserWithTransactionsDTO {
    @Size(min = 2, max = 50)
    @NotBlank
    private String name;
    @Size(min = 2, max = 50)
    @NotBlank
    private String login;
    private List<AnonymousTransactionDTO> transactions;

    public AnonymousUserWithTransactionsDTO(String name, String login, List<AnonymousTransactionDTO> transactions) {
        this.name = name;
        this.login = login;
        this.transactions = transactions;
    }

    public AnonymousUserWithTransactionsDTO() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public List<AnonymousTransactionDTO> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<AnonymousTransactionDTO> transactions) {
        this.transactions = transactions;
    }
}
