package pl.maryana.conference.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.maryana.conference.model.Reservation;
import pl.maryana.conference.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    long countAllByLectureId(long lectureId);

    Optional<Reservation> findByUserAndLectureId(User user, long lectureId);

    List<Reservation> findAllByUser(User user);
}
