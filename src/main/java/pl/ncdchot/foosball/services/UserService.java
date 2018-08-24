package pl.ncdchot.foosball.services;

import java.util.Optional;

import pl.ncdchot.foosball.database.model.User;
import pl.ncdchot.foosball.exceptions.UserNotExistException;

public interface UserService {
	Optional<User> getUser(long userId);

	User getUserByExternalID(long externalID) throws UserNotExistException;
}