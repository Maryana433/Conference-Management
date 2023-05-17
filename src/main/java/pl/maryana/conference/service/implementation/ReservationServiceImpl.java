package pl.maryana.conference.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.maryana.conference.exception.*;
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

@Service
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserService userService;
    private final LectureService lectureService;
    private final MailService mailService;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository, UserService userService, LectureService lectureService, MailService mailService) {
        this.reservationRepository = reservationRepository;
        this.userService = userService;
        this.lectureService = lectureService;
        this.mailService = mailService;
    }

    @Override
    public List<Reservation> findAllByLogin(String login) {

        Optional<User> user = userService.findByLogin(login);

        if(user.isEmpty()){
            throw new UserNotFound("User with login [" + login + "] not found");
        }

        List<Reservation> reservations = reservationRepository.findAllByUser(user.get());
        reservations.forEach(r -> r.setLecture(lectureService.findById(r.getLectureId()).
                orElseThrow(() -> new LectureNotFound("Lecture with id [" + r.getLectureId() +"] not found"))));
        return reservations;
    }

    @Override
    public Reservation reserve(String login, String email, long lectureId) {

        if(reservationRepository.countAllByLectureId(lectureId) == 5)
            throw new LimitOfReservations("Limit of reservation of lecture with id [" + lectureId + "] reached");


        Optional<User> user = userService.findByLogin(login);
        if(user.isPresent()){

            if(reservationRepository.findByUserAndLectureId(user.get(), lectureId).isPresent())
                throw new LoginIsAlreadyTaken("Podany login jest już zajęty");

            else{
                Reservation reservation = new Reservation();
                reservation.setLectureId(lectureId);
                reservation.setUser(user.get());

                Lecture lecture = lectureService.findById(lectureId).orElseThrow(() -> new LectureNotFound("Lecture with id [" + lectureId +"] not found"));

                reservation.setLecture(lecture);

                mailService.sendEmail(email, login, lecture);

                reservationRepository.save(reservation);
                log.info("Reservation to "  + lectureService.findById(lectureId) + " of user " + login + " was saved");

                return reservation;
            }
        }

        User newUser = new User();
        newUser.setLogin(login);
        newUser.setEmail(email);

        Reservation reservation = new Reservation();
        reservation.setLectureId(lectureId);
        newUser.addReservation(reservation);

        Lecture lecture = lectureService.findById(lectureId).orElseThrow(() -> new LectureNotFound("Lecture with id [" + lectureId +"] not found"));
        reservation.setLecture(lecture);

        mailService.sendEmail(email, login, lecture);

        userService.save(newUser);
        log.info("New user with login " + login +" was saved and his reservation to "  + lectureService.findById(lectureId) + " was saved");

        return reservation;
    }

    @Override
    public void cancelReservation(long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() ->
                new ReservationNotFound("Reservation with id [" + reservationId + "] not found"));
        reservationRepository.delete(reservation);
    }

    @Override
    public Reservation findById(long reservationId) {

        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() ->
                new ReservationNotFound("Reservation with id [" + reservationId + "] not found"));

        Lecture lecture = lectureService.findById(reservation.getLectureId()).orElseThrow(() ->
                new LectureNotFound("Lecture with id [" + reservation.getLectureId() +"] not found"));

        reservation.setLecture(lecture);
        return reservation ;
    }
}
