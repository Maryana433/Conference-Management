package pl.maryana.conference.service.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.maryana.conference.exception.LimitOfReservations;
import pl.maryana.conference.exception.LoginIsAlreadyTaken;
import pl.maryana.conference.exception.ReservationNotFound;
import pl.maryana.conference.exception.UserNotFound;
import pl.maryana.conference.model.Lecture;
import pl.maryana.conference.model.Reservation;
import pl.maryana.conference.model.User;
import pl.maryana.conference.repository.ReservationRepository;
import pl.maryana.conference.service.LectureService;
import pl.maryana.conference.service.MailService;
import pl.maryana.conference.service.ReservationService;
import pl.maryana.conference.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("ReservationService Tests")
@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private UserService userService;
    @Mock
    private LectureService lectureService;
    @Mock
    private MailService mailService;

    private ReservationService reservationService;

    private static long reservationLimit = 5L;


    @BeforeEach
    void init(){
        reservationService = new ReservationServiceImpl(reservationRepository,
                userService, lectureService, mailService);
    }

    @Test
    void shouldThrowExceptionWhenReservationNotExists(){

        //given
        long reservationId = 1L;
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        //when
        assertThrows(ReservationNotFound.class, () -> reservationService.findById(reservationId) );
    }

    @Test
    void shouldFindReservationById(){
        //given
        long reservationId = 1L;
        long lectureId = 1L;
        Lecture lecture = new Lecture();
        lecture.setId(lectureId);
        Reservation reservation = new Reservation();
        reservation.setId(reservationId);
        reservation.setLecture(lecture);
        reservation.setLectureId(lectureId);
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(lectureService.findById(lectureId)).thenReturn(Optional.of(lecture));

        //then
        Reservation reservationFromService = reservationService.findById(reservationId);

        //when
        verify(reservationRepository).findById(reservationId);
        verify(lectureService).findById(lectureId);
        assertEquals(reservation, reservationFromService);
    }

    @Test
    void shouldThrowExceptionWhenUserWithLoginNotExists(){
        String login = "login";
        when(userService.findByLogin(login)).thenReturn(Optional.empty());

        assertThrows(UserNotFound.class, () -> reservationService.findAllByLogin(login));
    }

    @Test
    void shouldReturnListOfReservationsOfUserByLoginAndSetLecture(){
        //given
        String login = "login";
        User user = new User();
        user.setLogin(login);
        when(userService.findByLogin(login)).thenReturn(Optional.of(user));

        //when
        List<Reservation> reservationList = reservationService.findAllByLogin(login);

        //then
        verify(reservationRepository).findAllByUser(user);
        assertTrue(reservationList.stream().noneMatch(e -> e.getLecture() == null));
    }

    @Test
    void shouldThrowExceptionWhenLimitOfLectureReached(){
        String login = "login";
        String email = "email";
        long lectureId = 1L;
        when(reservationRepository.countAllByLectureId(lectureId)).thenReturn(reservationLimit);

        assertThrows(LimitOfReservations.class, () -> reservationService.reserve(login, email, lectureId));
    }

    @Test
    void shouldThrowExceptionWhenUserAlreadyReserveLecture(){
        //given
        String login = "login";
        String email = "email";
        long lectureId = 1L;

        User user = new User();
        user.setEmail(email);
        user.setLogin(login);

        when(reservationRepository.countAllByLectureId(lectureId)).thenReturn(reservationLimit - 1);
        when(userService.findByLogin(login)).thenReturn(Optional.of(user));
        when(reservationRepository.findByUserAndLectureId(user, lectureId)).thenReturn(Optional.of(new Reservation()));

        //when
        assertThrows(LoginIsAlreadyTaken.class, () -> reservationService.reserve(login, email, lectureId));
    }

    @Test
    void shouldAddReservationIfUserWithLoginExistsAndSendEmail(){
        //given
        String login = "login";
        String email = "email";
        long lectureId = 1L;

        User user = new User();
        user.setEmail(email);
        user.setLogin(login);

        Lecture lecture = new Lecture();
        lecture.setId(lectureId);

        when(reservationRepository.countAllByLectureId(lectureId)).thenReturn(reservationLimit - 1);
        when(userService.findByLogin(login)).thenReturn(Optional.of(user));
        when(reservationRepository.findByUserAndLectureId(user, lectureId)).thenReturn(Optional.empty());
        when(lectureService.findById(lectureId)).thenReturn(Optional.of(lecture));


        //when
        Reservation reservationFromService = reservationService.reserve(login, email, lectureId);


        //then
        verify(mailService).sendEmail(email, login, lecture);

        assertEquals(lectureId, reservationFromService.getLectureId());
        assertEquals(user, reservationFromService.getUser());
        assertEquals(lecture, reservationFromService.getLecture());
    }

    @Test
    void shouldSaveUserAndReservationWhenUserNotExistsAndSendEmail(){
        //given
        String login = "login";
        String email = "email";
        long lectureId = 1L;

        User user = new User();
        user.setEmail(email);
        user.setLogin(login);

        Lecture lecture = new Lecture();
        lecture.setId(lectureId);

        when(reservationRepository.countAllByLectureId(lectureId)).thenReturn(reservationLimit - 1);
        when(userService.findByLogin(login)).thenReturn(Optional.empty());
        when(lectureService.findById(lectureId)).thenReturn(Optional.of(lecture));

        //when
        Reservation reservationFromService = reservationService.reserve(login, email, lectureId);

        verify(mailService).sendEmail(email, login, lecture);
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userService).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();

        assertEquals(email, capturedUser.getEmail());
        assertEquals(login, capturedUser.getLogin());
        assertEquals(reservationFromService, capturedUser.getReservations().get(0));

        assertEquals(lectureId, reservationFromService.getLectureId());
        assertEquals(lecture, reservationFromService.getLecture());

    }

    @Test
    void shouldThrowExceptionWhenReservationNotFoundAndUserTryToCancelIt(){
        long reservationId = 1L;
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        assertThrows(ReservationNotFound.class, () -> reservationService.cancelReservation(reservationId));
    }


    @Test
    void shouldCancelReservation(){
        //given
        long reservationId = 1L;
        Reservation reservation = new Reservation();
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        //when
        reservationService.cancelReservation(reservationId);


        //then
        verify(reservationRepository).delete(reservation);

    }



}
