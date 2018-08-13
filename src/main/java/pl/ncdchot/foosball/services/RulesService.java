package pl.ncdchot.foosball.services;

import pl.ncdchot.foosball.database.model.Game;
import pl.ncdchot.foosball.database.model.Rules;

public interface RulesService {
	public boolean checkRules(Game game);
	public void AddRules(Rules rules);
}
