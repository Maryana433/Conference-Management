package pl.maryana.conference.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.maryana.conference.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
