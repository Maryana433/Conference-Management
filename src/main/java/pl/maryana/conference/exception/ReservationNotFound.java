package pl.maryana.conference.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ReservationNotFound extends RuntimeException{

    public ReservationNotFound(String message) {
        super(message);
    }
}
