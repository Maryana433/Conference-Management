package pl.maryana.conference.service;

import pl.maryana.conference.model.Reservation;

import java.util.List;

public interface ReservationService {

    List<Reservation> findAllByLogin(String login);
    Reservation reserve(String login, String email, long lectureId);
    void cancelReservation(long reservationId, String login);
    List<Reservation> findAll();
    int numberOfReservationsOfLecture(long lectureId);
    int numberOfReservationsOfThematicPath(long thematicPathId);
}
