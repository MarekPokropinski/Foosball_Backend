package pl.ncdchot.foosball.services;

import pl.ncdchot.foosball.database.model.User;

import java.util.Optional;

public interface UserService {
    User getUser(long userId);

    Optional<User> getUserByExternalID(long externalID);

    void saveUser(User user);
}