package pl.ncdchot.foosball.services.implementations;

import java.util.Optional;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import pl.ncdchot.foosball.database.model.Game;
import pl.ncdchot.foosball.exceptions.GameNotFoundException;
import pl.ncdchot.foosball.services.GameService;
import pl.ncdchot.foosball.services.RulesService;

@Service
public class TimeLimiter {
	private static final Logger LOG = Logger.getLogger(TimeLimiter.class);
	@Autowired
	private GameService gameService;
	@Autowired
	private RulesService rulesService;

	@Scheduled(fixedRate = 250)
	void checkGameFinishByTimeout() {
		Optional<Game> optGame = gameService.getLiveGame();
		optGame.ifPresent(game -> {
			gameService.updateDuration(game);
			if (!rulesService.checkRules(game)) {
				try {
					LOG.info(String.format("Game with id: %s timeouted", game.getId()));
					gameService.finishGame(game.getId());
				} catch (GameNotFoundException e) {
					LOG.error(String.format("Game with id: %s was not present, but it was timeouted", game.getId()));
				}
			}
		});
	}
}
