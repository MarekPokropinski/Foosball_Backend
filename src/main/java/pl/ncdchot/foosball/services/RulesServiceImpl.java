package pl.ncdchot.foosball.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.ncdchot.foosball.database.model.Game;
import pl.ncdchot.foosball.database.model.Rules;
import pl.ncdchot.foosball.database.model.Statistics;
import pl.ncdchot.foosball.database.repository.RulesRepository;

@Service
public class RulesServiceImpl implements RulesService {
	
	@Autowired
	RulesRepository rulesRepository;

	@Override
	public boolean checkRules(Game game) {
		Rules rules = game.getRules();
		Statistics stats = game.getStats();

		if (stats.getBlueScore() >= rules.getScoreLimit() || stats.getRedScore() >= rules.getScoreLimit()) {
			return false;
		}
		return true;
	}

	@Override
	public void AddRules(Rules rules) {
		rulesRepository.save(rules);
	}

}
