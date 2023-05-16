package pl.maryana.conference.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class LoginIsAlreadyTaken extends RuntimeException{

    public LoginIsAlreadyTaken(String message) {
        super(message);
    }
}
