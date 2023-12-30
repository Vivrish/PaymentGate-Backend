package cz.cvut.fit.tjv.paymentgate.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
/**
 * Class represents a user with full information about them.
 * It is used to send to client data that not anyone can see
 */
public class FullUserDto {
    @Size(min = 2, max = 50)
    @NotEmpty
    private String name;
    @Size(min = 2, max = 50)
    @NotEmpty
    private String surname;
    @Size(min = 2, max = 50)
    @NotEmpty
    private String login;
    @Size(min = 2, max = 50)
    @NotEmpty
    private String email;
    @Size(min = 2, max = 50)
    @NotEmpty
    private String password;

    public FullUserDto(String name, String surname, String login, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.email = email;
        this.password = password;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
