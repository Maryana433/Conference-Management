package pl.maryana.conference.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.maryana.conference.dto.ApiExceptionDto;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(LimitOfReservations.class)
    public ResponseEntity<ApiExceptionDto> reservationLimit(LimitOfReservations e) {
        log.debug("CONFLICT - " + e.getMessage());
        return buildResponseEntity(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(LoginIsAlreadyTaken.class)
    public ResponseEntity<ApiExceptionDto> loginIsAlreadyTaken(LoginIsAlreadyTaken e) {
        log.debug("CONFLICT - " + e.getMessage());
        return buildResponseEntity(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(LectureNotFound.class)
    public ResponseEntity<ApiExceptionDto> lectureNotFound(LectureNotFound e) {
        log.debug("NOT_FOUND - " + e.getMessage());
        return buildResponseEntity(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ReservationNotFound.class)
    public ResponseEntity<ApiExceptionDto> reservationNotFound(ReservationNotFound e) {
        log.debug("NOT_FOUND - " + e.getMessage());
        return buildResponseEntity(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<ApiExceptionDto> reservationNotFound(UserNotFound e) {
        log.debug("NOT_FOUND - " + e.getMessage());
        return buildResponseEntity(e, HttpStatus.NOT_FOUND);
    }



    private static ResponseEntity<ApiExceptionDto> buildResponseEntity(Exception e, HttpStatus status) {
        ApiExceptionDto apiException = new ApiExceptionDto(
                LocalDateTime.now(),
                status,
                e.getMessage()
        );
        return new ResponseEntity<>(apiException, apiException.getStatus());
    }
}
