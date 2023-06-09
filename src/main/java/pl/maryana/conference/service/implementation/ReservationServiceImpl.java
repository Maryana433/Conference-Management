package pl.maryana.conference.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.maryana.conference.exception.*;
import pl.maryana.conference.model.Lecture;
import pl.maryana.conference.model.Reservation;
import pl.maryana.conference.model.User;
import pl.maryana.conference.repository.ReservationRepository;
import pl.maryana.conference.service.LectureThematicPathService;
import pl.maryana.conference.service.MailService;
import pl.maryana.conference.service.ReservationService;
import pl.maryana.conference.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserService userService;
    private final LectureThematicPathService lectureThematicPathService;
    private final MailService mailService;
    private final TimeServiceImpl timeServiceImpl;
    private final long lectureLimit;

    @Autowired
    public ReservationServiceImpl(@Value("${conference.lecture.limit}") int limit, TimeServiceImpl timeServiceImpl, ReservationRepository reservationRepository, UserService userService, LectureThematicPathService lectureThematicPathService, MailService mailService) {
        this.lectureLimit = limit;
        this.reservationRepository = reservationRepository;
        this.userService = userService;
        this.lectureThematicPathService = lectureThematicPathService;
        this.mailService = mailService;
        this.timeServiceImpl = timeServiceImpl;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> findAllByLogin(String login) {

        Optional<User> user = userService.findByLogin(login);

        if(user.isEmpty()){
            throw new UserNotFound("User with login [" + login + "] not found");
        }

        List<Reservation> reservations = reservationRepository.findAllByUser(user.get());
        reservations.forEach(r -> r.setLecture(lectureThematicPathService.findById(r.getLectureId()).
                orElseThrow(() -> new LectureNotFound("Lecture with id [" + r.getLectureId() +"] not found"))));
        return reservations;
    }

    @Override
    public Reservation reserve(String login, String email, long lectureId) {

        Lecture lecture = lectureThematicPathService.findById(lectureId).orElseThrow(() -> new LectureNotFound("Lecture with id [" + lectureId +"] not found"));
        LocalDateTime lectureStartDateTime = lecture.getStartDateTime();

        if(timeServiceImpl.isExpired(lectureStartDateTime))
            throw new LectureReservationExpiredException("Lecture has already started. You cannot make reservation");

        User user = userService.findByLogin(login).orElseThrow(() -> new UserNotFound("User [" + login + "] not found"));
        List<Reservation> listOfUsersReservations = reservationRepository.findAllByUser(user);
        for(Reservation r: listOfUsersReservations){
            Lecture lectureReserved =  lectureThematicPathService.findById(r.getLectureId()).orElseThrow(() -> new LectureNotFound("Lecture with id [" + lectureId +"] not found"));
            if(lecture.getStartDateTime().isEqual(lectureReserved.getStartDateTime()))
                throw new DuplicateReservationException("User try to make reservation to the same time [" + lectureReserved.getStartDateTime() +"]");
        }

        if(reservationRepository.countAllByLectureId(lectureId) == this.lectureLimit)
            throw new LimitOfReservations("Limit of reservation of lecture with id [" + lectureId + "] reached");


        Reservation reservation = new Reservation();
        reservation.setLectureId(lectureId);
        reservation.setUser(userService.findByLogin(login).orElseThrow(() -> new UserNotFound("User [" + login +"] not found")));

        reservation.setLecture(lecture);
        reservation.setThematicPathId(lecture.getThematicPath().getId());

        mailService.sendEmail(email, login, lecture);

        reservationRepository.save(reservation);
        log.info("Reservation to "  + lectureThematicPathService.findById(lectureId) + " of user " + login + " was saved");

        return reservation;
    }

    @Override
    public void cancelReservation(long reservationId, String login) {

        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() ->
                new ReservationNotFound("Reservation with id [" + reservationId + "] not found"));

        if(reservationRepository.findByIdAndUserLogin(reservationId, login).isEmpty()) {
            log.info("User [" + login +"] try do cancel not his reservation [" + reservationId +"]");
            throw new UnauthorizedCancellationException("User [" + login + "] doesn't have access to cancel reservation [" + reservationId + "]");
        }

        reservationRepository.delete(reservation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public int numberOfReservationsOfLecture(long lectureId) {
        return reservationRepository.countAllByLectureId(lectureId);
    }

    @Override
    @Transactional(readOnly = true)
    public int numberOfReservationsOfThematicPath(long thematicPathId) {
        return reservationRepository.countAllByThematicPathId(thematicPathId);
    }


}
