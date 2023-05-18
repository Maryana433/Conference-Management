package pl.maryana.conference.service;

import pl.maryana.conference.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByLogin(String login);
    User register(String password, String login, String email);
    User updateEmail(String login, String newEmail);
}
