package pl.maryana.conference.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.maryana.conference.exception.LoginIsAlreadyTaken;
import pl.maryana.conference.exception.UserNotFound;
import pl.maryana.conference.model.Role;
import pl.maryana.conference.model.User;
import pl.maryana.conference.repository.UserRepository;
import pl.maryana.conference.service.UserService;

import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }


    @Override
    public User register(String password, String login, String email) {
        if(userRepository.findByLogin(login).isPresent())
            throw new LoginIsAlreadyTaken("Podany login jest już zajęty");

        User user = new User();
        user.setLogin(login);
        user.setEmail(email);
        user.setPassword(password);

        user.setRole("ROLE_" + Role.USER);

        User saved = userRepository.save(user);

        log.info("User with login [" + login +"] and email [" + email +"] registered");

        return saved;
    }

    @Override
    public User updateEmail(String login, String newEmail) {
        User user = userRepository.findByLogin(login).orElseThrow(() -> new UserNotFound("User [" + login +"] not found"));
        user.setEmail(newEmail);
        return userRepository.save(user);
    }

}
