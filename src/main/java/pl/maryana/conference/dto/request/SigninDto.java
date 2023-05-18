package pl.maryana.conference.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SigninDto {

    @Size(min = 4, message = "Login should have at least 4 characters")
    private String login;

    @Size(min = 8, message = "Login should have at least 8 characters")
    private String password;
}
