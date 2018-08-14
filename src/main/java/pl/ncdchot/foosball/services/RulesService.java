package pl.ncdchot.foosball.services;

import pl.ncdchot.foosball.database.model.Game;
import pl.ncdchot.foosball.database.model.Rules;

public interface RulesService {
	boolean checkRules(Game game);
	void AddRules(Rules rules);
	Rules getRules(Rules rules);
}
