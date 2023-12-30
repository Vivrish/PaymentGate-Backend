package cz.cvut.fit.tjv.paymentgate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class OperationOnDeactivatedCardException extends ResponseStatusException {

    public OperationOnDeactivatedCardException() {
        super(HttpStatus.FORBIDDEN, "Any operation on deactivated card is forbidden");
    }
}
