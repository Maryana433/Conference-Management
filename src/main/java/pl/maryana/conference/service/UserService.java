package pl.maryana.conference.service;

import pl.maryana.conference.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByLogin(String login);
    void save(User user);
}
