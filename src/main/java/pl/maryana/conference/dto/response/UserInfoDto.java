package pl.maryana.conference.dto.response;

import lombok.Getter;
import lombok.Setter;
import pl.maryana.conference.model.User;

@Getter
@Setter
public class UserInfoDto {

    private long id;
    private String email;
    private String login;
    private String role;

    public UserInfoDto(User user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.login = user.getLogin();
        this.role = user.getRole();
    }

}
