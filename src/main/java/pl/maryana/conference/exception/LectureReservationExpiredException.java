package pl.maryana.conference.exception;

public class LectureReservationExpiredException extends RuntimeException {
    public LectureReservationExpiredException(String message) {
        super(message);
    }
}
