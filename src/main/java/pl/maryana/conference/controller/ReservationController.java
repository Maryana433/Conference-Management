package pl.maryana.conference.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.maryana.conference.dto.ApiExceptionDto;
import pl.maryana.conference.dto.ReservationDto;
import pl.maryana.conference.model.Reservation;
import pl.maryana.conference.security.UserDetailsImpl;
import pl.maryana.conference.service.ReservationService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/reservations/")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    @Operation(summary="Return reservations of User", description = "Użytkownik po podaniu swojego loginu może obejrzeć prelekcje na które się zapisał")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users reservations info", content = @Content(array =
                @ArraySchema(schema = @Schema(implementation = ReservationDto.class))))
    })
    public ResponseEntity<List<ReservationDto>> getReservationByLogin(){

        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Reservation> reservationList = reservationService.findAllByLogin(login);

        return ResponseEntity.ok().body(reservationList.stream().map(ReservationDto::new).collect(Collectors.toList()));
    }

    @PostMapping("/{lecture_id}")
    @Operation(summary="return created reservation info", description = "Jeżeli prelekcja ma jeszcze wolne miejsca, użytkownik ma możliwość dokonania rezerwacji.\n" +
            "Podczas dokonywania rezerwacji powinien podać swój login oraz adres e-mail.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created reservation info", content = @Content(array =
                    @ArraySchema(schema = @Schema(implementation = ReservationDto.class)))),
            @ApiResponse(responseCode = "400", description = "User try to make reservation after lecture started", content = @Content(array =
                    @ArraySchema(schema = @Schema(implementation = ApiExceptionDto.class)))),
            @ApiResponse(responseCode = "404", description = "Lecture not found", content = @Content(array =
                @ArraySchema(schema = @Schema(implementation = ApiExceptionDto.class)))),
            @ApiResponse(responseCode = "409", description = "Limit of reservation for this lecture was reached or User with this login has already made the reservation ",
                    content = @Content(schema =
                            @Schema(implementation = ApiExceptionDto.class)))
    })
    public ResponseEntity<ReservationDto> createReservation(@PathVariable("lecture_id") long lectureId){

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Reservation reservation = reservationService.reserve(userDetails.getLogin(), userDetails.getEmail(), lectureId);

        return new ResponseEntity<>(new ReservationDto(reservation), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary="Cancel (delete) reservation", description = "Użytkownik może anulować rezerwację. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reservation deleted", content = @Content(array =
                @ArraySchema(schema = @Schema(implementation = ReservationDto.class)))),
            @ApiResponse(responseCode = "403", description = "User try to cancel not his reservation", content = @Content(array =
                @ArraySchema(schema = @Schema(implementation = ApiExceptionDto.class)))),
            @ApiResponse(responseCode = "404", description = "Reservation with id not found",
                    content = @Content(schema =
                    @Schema(implementation = ApiExceptionDto.class)))
    })
    public ResponseEntity cancelReservation(@PathVariable long id){

        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        reservationService.cancelReservation(id, login);

        return ResponseEntity.noContent().build();
    }



}
