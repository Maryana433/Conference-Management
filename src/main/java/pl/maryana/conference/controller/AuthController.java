package pl.maryana.conference.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.maryana.conference.dto.*;
import pl.maryana.conference.model.User;
import pl.maryana.conference.security.JwtUtils;
import pl.maryana.conference.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("api/auth/")
public class AuthController {

    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserService userService, JwtUtils jwtUtils, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signup")
    @Operation(summary="Create an account",description = "User can create an account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully registered",content = @Content(
                    schema = @Schema(implementation = UserInfoDto.class))),
            @ApiResponse(responseCode = "409", description = "Login is already taken", content = @Content(
                    schema = @Schema(implementation = ApiExceptionDto.class)))})
    public ResponseEntity<UserInfoDto> registerUser(@RequestBody @Valid SignupDto userSignupInfo) {

        User savedUser = userService.
                register(passwordEncoder.encode(userSignupInfo.getPassword()), userSignupInfo.getLogin(), userSignupInfo.getPassword());

        return new ResponseEntity<>(new UserInfoDto(savedUser), HttpStatus.CREATED);
    }


    @PostMapping("/authenticate")
    @Operation(summary="Authenticate",description = "User can authenticate by login and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully registered",content = @Content(
                    schema = @Schema(implementation = JwtDto.class)))
    })
    public ResponseEntity<JwtDto> authenticateUser(@RequestBody SigninDto userSigninInfo) {

        Authentication authentication = authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(userSigninInfo.getLogin(),userSigninInfo.getPassword()));

        String code = jwtUtils.generateJwtToken(authentication);

        return new ResponseEntity<>(new JwtDto(code), HttpStatus.OK);
    }
}
