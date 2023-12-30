package cz.cvut.fit.tjv.paymentgate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CurrenciesDoNotMatchException extends ResponseStatusException {
    public CurrenciesDoNotMatchException() {
        super(HttpStatus.FORBIDDEN, "Can't commit the operation: currencies don't match");
    }
}
