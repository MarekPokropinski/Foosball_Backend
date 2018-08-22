package pl.ncdchot.foosball.services;

import java.util.Optional;

import pl.ncdchot.foosball.database.model.User;
import pl.ncdchot.foosball.exceptions.UserNotExist;

public interface ManagementSystemService {
	Optional<Long> getUserIdByNick(String nick);

	User getUserByExternalId(long externalId) throws UserNotExist;
}
