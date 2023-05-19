package pl.maryana.conference.exception;

public class LectureNotFound extends RuntimeException{
    public LectureNotFound(String message) {
        super(message);
    }
}
