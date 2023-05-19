package pl.maryana.conference.exception;

public class LoginIsAlreadyTaken extends RuntimeException{

    public LoginIsAlreadyTaken(String message) {
        super(message);
    }
}
