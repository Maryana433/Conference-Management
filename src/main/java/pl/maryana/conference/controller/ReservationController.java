package pl.maryana.conference.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.maryana.conference.dto.ApiExceptionDto;
import pl.maryana.conference.dto.LectureDto;
import pl.maryana.conference.dto.LoginEmailDto;
import pl.maryana.conference.dto.ReservationDto;
import pl.maryana.conference.model.Reservation;
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

    @GetMapping("{id}")
    @Operation(summary="return reservation by id ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservation info", content = @Content(
                    schema = @Schema(implementation = ReservationDto.class))),

            @ApiResponse(responseCode = "404", description = "Reservation with id not found", content = @Content(schema =
                    @Schema(implementation = ApiExceptionDto.class)))
    })
    public ResponseEntity<ReservationDto> getReservation(@PathVariable long id){
        Reservation reservation = reservationService.findById(id);
        return ResponseEntity.ok().body(new ReservationDto(reservation));
    }

    @GetMapping
    @Operation(summary="return reservation by user login", description = "Użytkownik po podaniu swojego loginu może obejrzeć prelekcje na które się zapisał")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users reservations info", content = @Content(array =
                @ArraySchema(schema = @Schema(implementation = ReservationDto.class)))),

            @ApiResponse(responseCode = "404", description = "User with login not found", content = @Content(schema =
                @Schema(implementation = ApiExceptionDto.class)))
    })
    public ResponseEntity<List<ReservationDto>> getReservationByLogin(@RequestParam("login") String login){
        List<Reservation> reservationList = reservationService.findAllByLogin(login);
        return ResponseEntity.ok().body(reservationList.stream().map(ReservationDto::new).collect(Collectors.toList()));
    }

    @PostMapping("/{lectureId}")
    @Operation(summary="return created reservation info", description = "Jeżeli prelekcja ma jeszcze wolne miejsca, użytkownik ma możliwość dokonania rezerwacji.\n" +
            "Podczas dokonywania rezerwacji powinien podać swój login oraz adres e-mail.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created reservation info with url to new resource in Location header", content = @Content(array =
            @ArraySchema(schema = @Schema(implementation = ReservationDto.class)))),

            @ApiResponse(responseCode = "409", description = "Limit of reservation for this lecture was reached or User with this login has already made the reservation ",
                    content = @Content(schema =
                            @Schema(implementation = ApiExceptionDto.class)))
    })
    public ResponseEntity<ReservationDto> createReservation(@RequestBody LoginEmailDto loginEmailDto, @PathVariable long lectureId){

        Reservation reservation = reservationService.reserve(loginEmailDto.getLogin(), loginEmailDto.getEmail(), lectureId);
        long reservationId = reservation.getId();

        return ResponseEntity.status(HttpStatus.CREATED).header("Location","http://localhost:8080/api/reservations/"+reservationId)
                .body(new ReservationDto(reservation));
    }

    @DeleteMapping("/{id}")
    @Operation(summary="Cancel (delete) reservation", description = "Użytkownik może anulować rezerwację. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created reservation info with url to new resource in Location header", content = @Content(array =
            @ArraySchema(schema = @Schema(implementation = ReservationDto.class)))),

            @ApiResponse(responseCode = "404", description = "Reservation with id not found",
                    content = @Content(schema =
                    @Schema(implementation = ApiExceptionDto.class)))
    })
    public ResponseEntity cancelReservation(@PathVariable long id){
        reservationService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }



}
