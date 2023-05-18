package pl.maryana.conference.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class LectureReservationExpiredException extends RuntimeException {
    public LectureReservationExpiredException(String message) {
        super(message);
    }
}
