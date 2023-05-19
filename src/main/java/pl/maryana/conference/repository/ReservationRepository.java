package pl.maryana.conference.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.maryana.conference.model.Reservation;
import pl.maryana.conference.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    int countAllByLectureId(long lectureId);

    List<Reservation> findAllByUser(User user);

    @Query("select r from Reservation r where r.id = :reservationId and r.user.login = :login")
    Optional<Reservation> findByIdAndUserLogin(@Param("reservationId") long reservationId,
                                               @Param("login") String login);

    @Query("select r from Reservation r where r.lectureId = :lectureId and r.user.login = :login")
    Optional<Reservation> findByLectureIdAndUserLogin(@Param("lectureId") long lectureID,
                                                      @Param("login") String login);

    int countAllByThematicPathId(long thematicPathId);
}
