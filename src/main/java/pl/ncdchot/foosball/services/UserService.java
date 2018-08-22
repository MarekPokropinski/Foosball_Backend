package pl.ncdchot.foosball.services;

import pl.ncdchot.foosball.database.model.User;

public interface UserService {
	User getUser(long userId);

	User getUserByExternalId(long externalId);
}