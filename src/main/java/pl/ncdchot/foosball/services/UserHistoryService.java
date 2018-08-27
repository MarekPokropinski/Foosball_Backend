package pl.ncdchot.foosball.services;

import java.util.List;

import pl.ncdchot.foosball.database.model.Game;
import pl.ncdchot.foosball.database.model.User;
import pl.ncdchot.foosball.database.model.UserHistory;
import pl.ncdchot.foosball.modelDTO.HistoryDTO;

public interface UserHistoryService {
	UserHistory getHistory(User user);

	List<HistoryDTO> getAllHistory();

	void updateHistory(Game game);
}
