package pl.maryana.conference.exception;

public class LimitOfReservations extends RuntimeException{

    public LimitOfReservations(String message) {
        super(message);
    }
}
