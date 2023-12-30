package cz.cvut.fit.tjv.paymentgate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NoPreferredCardException extends ResponseStatusException {
    public NoPreferredCardException() {
        super(HttpStatus.BAD_REQUEST, "User doesn't have a preferred card to make an operation");
    }
}
