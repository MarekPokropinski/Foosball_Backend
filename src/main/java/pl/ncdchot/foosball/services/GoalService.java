package pl.ncdchot.foosball.services;

import pl.ncdchot.foosball.database.model.Goal;
import pl.ncdchot.foosball.game.TeamColor;

public interface GoalService {
	Goal getNewGoal(TeamColor team);
}
