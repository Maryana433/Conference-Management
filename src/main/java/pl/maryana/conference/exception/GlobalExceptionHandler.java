package pl.maryana.conference.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.maryana.conference.dto.response.ApiExceptionDto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) ->{
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

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

    @ExceptionHandler(UnauthorizedCancellationException.class)
    public ResponseEntity<ApiExceptionDto> unauthorizeCancellation(UnauthorizedCancellationException e) {
        log.debug("FORBIDDEN - " + e.getMessage());
        return buildResponseEntity(e, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DuplicateReservationException.class)
    public ResponseEntity<ApiExceptionDto> duplicateReservation(DuplicateReservationException e) {
        log.debug("FORBIDDEN - " + e.getMessage());
        return buildResponseEntity(e, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(LectureReservationExpiredException.class)
    public ResponseEntity<ApiExceptionDto> lectureReservation(LectureReservationExpiredException e) {
        log.debug("BAD_REQUEST - " + e.getMessage());
        return buildResponseEntity(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OutputStreamException.class)
    public ResponseEntity<ApiExceptionDto> lectureReservation(OutputStreamException e) {
        log.debug("INTERNAL_SERVER_ERROR - " + e.getMessage());
        return buildResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
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
