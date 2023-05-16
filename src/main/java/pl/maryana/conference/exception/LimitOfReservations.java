package pl.maryana.conference.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class LimitOfReservations extends RuntimeException{

    public LimitOfReservations(String message) {
        super(message);
    }
}
