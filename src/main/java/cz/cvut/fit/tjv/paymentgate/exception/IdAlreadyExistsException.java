package cz.cvut.fit.tjv.paymentgate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class IdAlreadyExistsException extends ResponseStatusException {
    public IdAlreadyExistsException() {
        super(HttpStatus.BAD_REQUEST, "Id that you provided already exists");
    }
}
