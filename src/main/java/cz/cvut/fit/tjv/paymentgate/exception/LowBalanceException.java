package cz.cvut.fit.tjv.paymentgate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class LowBalanceException extends ResponseStatusException {
    public LowBalanceException() {
        super(HttpStatus.PAYMENT_REQUIRED, "Can't commit the operation: balance is too low");
    }
}
