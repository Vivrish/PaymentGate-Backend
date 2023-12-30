package cz.cvut.fit.tjv.paymentgate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class WrongFormatException extends ResponseStatusException {
    public WrongFormatException(String reason) {
        super(HttpStatus.BAD_REQUEST, reason);
    }
}
