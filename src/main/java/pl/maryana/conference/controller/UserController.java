package pl.maryana.conference.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.maryana.conference.dto.response.ApiExceptionDto;
import pl.maryana.conference.dto.request.EmailDto;
import pl.maryana.conference.dto.response.UserInfoDto;
import pl.maryana.conference.model.User;
import pl.maryana.conference.service.UserService;

import javax.validation.Valid;

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

}
