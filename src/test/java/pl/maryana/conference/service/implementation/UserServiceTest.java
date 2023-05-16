package pl.maryana.conference.service.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.maryana.conference.model.User;
import pl.maryana.conference.repository.UserRepository;
import pl.maryana.conference.service.UserService;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@DisplayName("UserService Tests")
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void init(){
        userService = new UserServiceImpl(userRepository);
    }


    @Test
    void shouldFindUserByLogin(){

        //given
        String login = "login";

        //when
        userService.findByLogin(login);

        //then
        verify(userRepository).findByLogin(login);
        verifyNoMoreInteractions(userRepository);
    }


    @Test
    void shouldSaveUser(){
        //given
        User userToSave = new User();

        //when
        userService.save(userToSave);

        //then
        verify(userRepository).save(userToSave);
        verifyNoMoreInteractions(userRepository);

    }

}
