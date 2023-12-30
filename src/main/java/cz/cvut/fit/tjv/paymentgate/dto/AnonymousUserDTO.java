package cz.cvut.fit.tjv.paymentgate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
/**
 * Class represents a user without any confidential information.
 * It is used to send to client data that anyone can see
 */
public class AnonymousUserDTO {
    @Size(min = 2, max = 50)
    @NotBlank
    private String name;
    @Size(min = 2, max = 50)
    @NotBlank
    private String surname;
    @Size(min = 2, max = 50)
    @NotBlank
    private String login;
    @Size(min = 1000, max = 9999)
    @NotNull
    private int preferredCardNumber;



    public AnonymousUserDTO(String name, String surname, String login, int preferredCardNumber) {
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.preferredCardNumber = preferredCardNumber;
    }

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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
    public int getPreferredCardNumber() {
        return preferredCardNumber;
    }

    public void setPreferredCardNumber(int preferredCardNumber) {
        this.preferredCardNumber = preferredCardNumber;
    }
}
