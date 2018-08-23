package pl.ncdchot.foosball.services.implementations;

import java.time.Duration;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Service;

import pl.ncdchot.foosball.database.model.GameType;
import pl.ncdchot.foosball.database.model.Rules;
import pl.ncdchot.foosball.exceptions.UserNotExist;

@Service
public class RankedGameServiceImpl extends GameWithHistoryServiceImpl {
	private static final Logger LOG = Logger.getLogger(RankedGameServiceImpl.class);

	private static final int RANKED_GOALS_LIMIT = 10;
	private static final int RANKED_TIME_IN_SEC_LIMIT = 600;
	private static final Rules RANKED_GAME_RULES = new Rules(RANKED_GOALS_LIMIT,
			Duration.ofSeconds(RANKED_TIME_IN_SEC_LIMIT));

	@Override
	public long startGame(long[] users, Rules rules) throws UserNotExist {
		LOG.info("Started ranked game");
		return super.startGame(users, RANKED_GAME_RULES, GameType.RANKED);
	}
}
