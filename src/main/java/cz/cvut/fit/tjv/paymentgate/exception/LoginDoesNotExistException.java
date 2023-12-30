package cz.cvut.fit.tjv.paymentgate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class LoginDoesNotExistException extends ResponseStatusException {
    public LoginDoesNotExistException() {
        super(HttpStatus.NOT_FOUND, "Login that you provided doesn't exist in the database");
    }
}
