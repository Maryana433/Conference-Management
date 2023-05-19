package pl.maryana.conference.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class OutputStreamException extends RuntimeException{
    public OutputStreamException(String message) {
        super(message);
    }
}
