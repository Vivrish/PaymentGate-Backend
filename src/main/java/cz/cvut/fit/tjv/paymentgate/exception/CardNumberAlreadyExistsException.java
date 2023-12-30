package cz.cvut.fit.tjv.paymentgate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CardNumberAlreadyExistsException extends ResponseStatusException {
    public CardNumberAlreadyExistsException() {
        super(HttpStatus.BAD_REQUEST, "Card number that you provided already exists");
    }
}
