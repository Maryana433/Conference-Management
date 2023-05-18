package pl.maryana.conference.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.maryana.conference.dto.response.ApiExceptionDto;
import pl.maryana.conference.dto.request.EmailDto;
import pl.maryana.conference.dto.response.UserInfoDto;
import pl.maryana.conference.model.User;
import pl.maryana.conference.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users/")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PatchMapping("/email")
    @Operation(summary="Update email", description = "Użytkownik może zaktualizować swój adres e-mail.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reservation deleted", content = @Content(schema
                    = @Schema(implementation = UserInfoDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema =
                    @Schema(implementation = ApiExceptionDto.class)))
    })
    public ResponseEntity<UserInfoDto> updateEmail(@RequestBody @Valid EmailDto emailDto){
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.updateEmail(login, emailDto.getEmail());

        return new ResponseEntity<>(new UserInfoDto(user), HttpStatus.OK);
    }


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary="User with role ADMIN can get all user", description = "System umożliwia wyświetlenie listy zarejestrowanych użytkowników wraz z ich adresami e-mail")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users", content = @Content(schema
                    = @Schema(implementation = UserInfoDto.class))),
            @ApiResponse(responseCode = "404", description = "Lecture not found", content = @Content(schema =
            @Schema(implementation = ApiExceptionDto.class)))
    })
    public ResponseEntity<List<UserInfoDto>> getRegisteredUser(@RequestParam(required = false, defaultValue = "0") long lecture_id){
        List<User> users = userService.findAllRegisteredUsers(lecture_id);
        return new ResponseEntity<>(users.stream().map(UserInfoDto::new).collect(Collectors.toList()), HttpStatus.OK);

    }

}
