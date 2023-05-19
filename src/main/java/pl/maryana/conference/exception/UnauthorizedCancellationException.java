package pl.maryana.conference.exception;

public class UnauthorizedCancellationException extends RuntimeException {
    public UnauthorizedCancellationException(String message) {
        super(message);
    }
}
