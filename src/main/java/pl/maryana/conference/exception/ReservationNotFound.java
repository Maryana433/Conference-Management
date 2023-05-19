package pl.maryana.conference.exception;

public class ReservationNotFound extends RuntimeException{

    public ReservationNotFound(String message) {
        super(message);
    }
}
