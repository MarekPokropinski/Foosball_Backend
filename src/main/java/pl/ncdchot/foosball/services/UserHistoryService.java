package pl.ncdchot.foosball.services;

import pl.ncdchot.foosball.database.model.Game;
import pl.ncdchot.foosball.database.model.UserHistory;

public interface UserHistoryService {
	UserHistory createNewHistory();

	void updateHistory(Game game);
}
