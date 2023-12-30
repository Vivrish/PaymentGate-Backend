package cz.cvut.fit.tjv.paymentgate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class LoginAlreadyExistsException extends ResponseStatusException {
    public LoginAlreadyExistsException() {
        super(HttpStatus.BAD_REQUEST, "Login that you provided already exists");
    }
}
