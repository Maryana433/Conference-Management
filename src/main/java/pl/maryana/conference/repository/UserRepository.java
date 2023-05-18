package pl.maryana.conference.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.maryana.conference.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLogin(String login);

    @Query("select u from User u where size(u.reservations) > 0")
    List<User> findByMakeReservation();

    @Query(nativeQuery = true, value =
            "SELECT * from conference_user u join reservation r on r.user_id = u.id " +
                    "where r.lecture_id = :lectureId ")
    List<User> findByMakeReservation(@Param("lectureId") long lectureId);
}
