package pl.ncdchot.foosball.services;

import java.util.Optional;

import pl.ncdchot.foosball.exceptions.UserNotExist;
import pl.ncdchot.foosball.modelDTO.UserDTO;

public interface ManagementSystemService {
	Optional<Long> getUserIdByNick(String nick);

	UserDTO getExternalUserByExternalId(long externalId) throws UserNotExist;

	boolean isUserInExternalService(long externalId);
}
