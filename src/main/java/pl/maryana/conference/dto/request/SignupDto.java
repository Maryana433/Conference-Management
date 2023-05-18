package pl.maryana.conference.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupDto {

    @Size(min = 4, message = "Login should have at least 4 characters")
    private String login;

    @Size(min = 8, message = "Password should have at least 8 characters")
    private String password;

    @Email(message = "Email is not valid")
    private String email;
}
