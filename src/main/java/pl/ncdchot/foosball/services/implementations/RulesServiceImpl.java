package pl.ncdchot.foosball.services.implementations;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.ncdchot.foosball.database.model.Game;
import pl.ncdchot.foosball.database.model.Rules;
import pl.ncdchot.foosball.database.model.Statistics;
import pl.ncdchot.foosball.database.repository.RulesRepository;
import pl.ncdchot.foosball.services.GameService;
import pl.ncdchot.foosball.services.RulesService;

@Service
public class RulesServiceImpl implements RulesService {

	private static final int NO_SCORE_LIMIT = 0;
	private static final Duration NO_TIME_LIMIT = Duration.ZERO;
	@Autowired
	RulesRepository rulesRepository;

	@Autowired
	GameService gameService;

	private boolean checkTimeLimit(Rules rules, Statistics stats) {
		if (rules.getTimeLimit() != NO_TIME_LIMIT && stats.getDuration().compareTo(rules.getTimeLimit()) >= 0) {
			return false;
		}
		return true;
	}

	private boolean checkScoreLimit(Rules rules, Statistics stats) {
		if (rules.getScoreLimit() != NO_SCORE_LIMIT
				&& (stats.getBlueScore() >= rules.getScoreLimit() || stats.getRedScore() >= rules.getScoreLimit())) {
			return false;
		}
		return true;
	}

	@Override
	public boolean checkRules(Game game) {
		Rules rules = game.getRules();
		Statistics stats = game.getStats();
		gameService.updateDuration(game);
		return checkTimeLimit(rules, stats) && checkScoreLimit(rules, stats);
	}

	@Override
	public void addRules(Rules rules) {
		rulesRepository.save(rules);
	}

	@Override
	public Rules getRules(Rules rules) {
		Optional<Rules> optRules = rulesRepository.findByCreatorIdAndScoreLimitAndTimeLimit(rules.getCreatorId(),
				rules.getScoreLimit(), rules.getTimeLimit());

		if (optRules.isPresent()) {
			return optRules.get();
		} else {
			rulesRepository.save(rules);
			return rules;
		}
	}

	@Override
	public boolean isRulesExist(Rules rules) {
		return rulesRepository.existsByScoreLimitAndTimeLimit(rules.getScoreLimit(), rules.getTimeLimit());
	}

	@Override
	public List<Rules> getAll() {
		return rulesRepository.findAll();
	}
}
