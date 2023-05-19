package pl.maryana.conference.service.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.maryana.conference.exception.LectureNotFound;
import pl.maryana.conference.exception.LoginIsAlreadyTaken;
import pl.maryana.conference.model.Lecture;
import pl.maryana.conference.model.User;
import pl.maryana.conference.repository.UserRepository;
import pl.maryana.conference.service.LectureThematicPathService;
import pl.maryana.conference.service.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayName("UserService Tests")
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private LectureThematicPathService lectureThematicPathService;
    private UserService userService;

    @BeforeEach
    void init(){
        userService = new UserServiceImpl(userRepository, lectureThematicPathService);
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
    void shouldThrowExceptionWhenUserWithLoginExists(){
        String login = "login";
        when(userRepository.findByLogin(login)).thenReturn(Optional.of(new User()));

        assertThrows(LoginIsAlreadyTaken.class, () -> userService.register("password", login, "email"));

    }

    @Test
    void shouldRegisterUser(){
        //given
        String login = "login";
        String password = "password";
        String email = "email";
        when(userRepository.findByLogin(login)).thenReturn(Optional.empty());


        //when
        userService.register(password,login, email);

        //then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        User captured = userArgumentCaptor.getValue();

        assertEquals(login, captured.getLogin());
        assertEquals(password, captured.getPassword());
        assertEquals(email, captured.getEmail());
    }

    @Test
    void shouldUpdateEmail(){
        //given
        String newEmail = "newEmail";
        String login = "login";
        User user = new User();
        when(userRepository.findByLogin(login)).thenReturn(Optional.of(user));

        //when
        userService.updateEmail(login, newEmail);

        //then
        assertEquals(newEmail, user.getEmail());
        verify(userRepository).save(user);
    }


    @Test
    void shouldReturnAllRegisteredUser(){
        long lectureId = 0L;

        userService.findAllRegisteredUsers(lectureId);

        verify(userRepository).findByMakeReservation();
        verifyNoMoreInteractions(userRepository);
    }


    @Test
    void shouldReturnRegisteredUsersByLecture(){
        long lectureId = 1L;
        when(lectureThematicPathService.findById(lectureId)).thenReturn(Optional.of(new Lecture()));

        userService.findAllRegisteredUsers(lectureId);

        verify(userRepository).findByMakeReservation(lectureId);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldThrowExceptionWhenLectureNotFound(){
        long lectureId = 1L;
        when(lectureThematicPathService.findById(lectureId)).thenReturn(Optional.empty());

        assertThrows(LectureNotFound.class, () -> userService.findAllRegisteredUsers(lectureId));

    }

}
