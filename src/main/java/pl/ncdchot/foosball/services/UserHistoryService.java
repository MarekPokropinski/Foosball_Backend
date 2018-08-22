package pl.ncdchot.foosball.services;

import pl.ncdchot.foosball.database.model.Game;
import pl.ncdchot.foosball.database.model.User;
import pl.ncdchot.foosball.database.model.UserHistory;

public interface UserHistoryService {
	UserHistory getHistory(User user);

	void updateHistory(Game game);
}
