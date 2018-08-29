package pl.ncdchot.foosball.services;

import java.util.ArrayList;

import pl.ncdchot.foosball.database.model.Game;
import pl.ncdchot.foosball.database.model.Goal;
import pl.ncdchot.foosball.modelDTO.ReplayDTO;

public interface ReplaysService {

	ArrayList<ReplayDTO> getReplays(long gameId);
	void saveReplay(Game game, Goal goal);
}
