package pl.maryana.conference.exception;

public class UserNotFound extends RuntimeException{

    public UserNotFound(String message) {
        super(message);
    }
}
