package pl.ncdchot.foosball.services;

import java.util.List;
import java.util.Optional;

import pl.ncdchot.foosball.database.model.User;
import pl.ncdchot.foosball.database.model.UserHistory;
import pl.ncdchot.foosball.exceptions.UserNotExistException;
import pl.ncdchot.foosball.modelDTO.HistoryDTO;

public interface UserService {
	Optional<User> getUser(long userId);

	User getUserByExternalID(long externalID) throws UserNotExistException;

	List<HistoryDTO> getAllHistory();

	User getUserByHistory(UserHistory userHistory);
}