package pl.maryana.conference.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class UnauthorizedCancellationException extends RuntimeException {
    public UnauthorizedCancellationException(String message) {
        super(message);
    }
}
