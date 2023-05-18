package pl.maryana.conference.service.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.maryana.conference.exception.*;
import pl.maryana.conference.model.Lecture;
import pl.maryana.conference.model.Reservation;
import pl.maryana.conference.model.User;
import pl.maryana.conference.repository.ReservationRepository;
import pl.maryana.conference.service.LectureService;
import pl.maryana.conference.service.MailService;
import pl.maryana.conference.service.ReservationService;
import pl.maryana.conference.service.UserService;

import java.time.LocalDateTime;
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
    @Mock
    private TimeService timeService;

    private ReservationService reservationService;

    private final static long reservationLimit = 5L;


    @BeforeEach
    void init(){
        reservationService = new ReservationServiceImpl(timeService, reservationRepository,
                userService, lectureService, mailService);
    }

    @Test
    void shouldThrowExceptionWhenUserWithLoginNotExistsWhenTryToFindAllReservations(){
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
    void shouldThrowExceptionWhenLectureHasAlreadyStartedAndUserTryToMakeReservation(){
        String login = "login";
        String email = "email";
        long lectureId = 1L;

        Lecture lecture = new Lecture();
        lecture.setStartDateTime(LocalDateTime.now());

        when(lectureService.findById(lectureId)).thenReturn(Optional.of(lecture));
        when(timeService.isExpired(ArgumentMatchers.any(LocalDateTime.class))).thenReturn(true);

        assertThrows(LectureReservationExpiredException.class, () -> reservationService.reserve(login, email, lectureId));
    }

    @Test
    void shouldThrowExceptionWhenLectureNotFound(){
        String login = "login";
        String email = "email";
        long lectureId = 1L;

        when(lectureService.findById(lectureId)).thenReturn(Optional.empty());
        assertThrows(LectureNotFound.class, () -> reservationService.reserve(login, email, lectureId));
    }

    @Test
    void shouldThrowExceptionWhenLimitOfLectureReached(){

            String login = "login";
            String email = "email";
            long lectureId = 1L;

            Lecture lecture = new Lecture();
            lecture.setStartDateTime(LocalDateTime.now());

            when(reservationRepository.countAllByLectureId(lectureId)).thenReturn(reservationLimit);
            when(lectureService.findById(lectureId)).thenReturn(Optional.of(lecture));
            when(timeService.isExpired(ArgumentMatchers.any(LocalDateTime.class))).thenReturn(false);

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

        Lecture lecture = new Lecture();
        lecture.setStartDateTime(LocalDateTime.now());

        when(reservationRepository.countAllByLectureId(lectureId)).thenReturn(reservationLimit - 1);
        when(reservationRepository.findByLectureIdAndUserLogin(lectureId, login)).thenReturn(Optional.of(new Reservation()));
        when(lectureService.findById(lectureId)).thenReturn(Optional.of(lecture));
        when(timeService.isExpired(ArgumentMatchers.any(LocalDateTime.class))).thenReturn(false);

        //when
        assertThrows(DuplicateReservationException.class, () -> reservationService.reserve(login, email, lectureId));
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
        when(reservationRepository.findByLectureIdAndUserLogin(lectureId, login)).thenReturn(Optional.empty());
        when(userService.findByLogin(login)).thenReturn(Optional.of(user));
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
    void shouldThrowExceptionWhenUserTryToCancelNotHisReservation(){
        long reservationId = 1L;
        String login = "login";
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(new Reservation()));
        when(reservationRepository.findByIdAndUserLogin(reservationId, login)).thenReturn(Optional.empty());

        assertThrows(UnauthorizedCancellationException.class, () -> reservationService.cancelReservation(reservationId, login));
    }


    @Test
    void shouldThrowExceptionWhenReservationNotFoundAndUserTryToCancelIt(){
        long reservationId = 1L;
        String login = "login";
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        assertThrows(ReservationNotFound.class, () -> reservationService.cancelReservation(reservationId, login));
    }


    @Test
    void shouldCancelReservation(){
        //given
        long reservationId = 1L;
        String login = "login";
        Reservation reservation = new Reservation();
        when(reservationRepository.findByIdAndUserLogin(reservationId, login)).thenReturn(Optional.of(reservation));
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        //when
        reservationService.cancelReservation(reservationId, login);


        //then
        verify(reservationRepository).delete(reservation);

    }



}
