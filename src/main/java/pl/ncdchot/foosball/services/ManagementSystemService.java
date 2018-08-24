package pl.ncdchot.foosball.services;

import java.util.Optional;

import pl.ncdchot.foosball.exceptions.UserByCardIDNotExistException;
import pl.ncdchot.foosball.exceptions.UserByNickNoExistException;
import pl.ncdchot.foosball.exceptions.UserNotExistException;
import pl.ncdchot.foosball.modelDTO.UserDTO;

public interface ManagementSystemService {
	Optional<Long> getUserIdByNick(String nick);

	UserDTO getExternalUserByExternalId(long externalId) throws UserNotExistException;

	boolean isUserInExternalService(long externalId);

    UserDTO getUserByCardID(Long userID) throws UserByCardIDNotExistException;

	UserDTO getUserByNickOrCardID(String textValue) throws UserByCardIDNotExistException, UserNotExistException, UserByNickNoExistException;
}
