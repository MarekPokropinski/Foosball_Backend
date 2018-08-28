package pl.ncdchot.foosball.services.implementations;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Service;

import pl.ncdchot.foosball.database.model.Game;
import pl.ncdchot.foosball.database.model.GameType;
import pl.ncdchot.foosball.database.model.Rules;
import pl.ncdchot.foosball.exceptions.GameNotFoundException;
import pl.ncdchot.foosball.game.GameInfo;
import pl.ncdchot.foosball.services.FreeGameService;

@Service
public class FreeGameServiceImpl extends GameServiceImpl implements FreeGameService {
	private static int SCORE_LIMIT = 10;
	private static final Rules FREE_RULES = new Rules(SCORE_LIMIT);
	private static final Logger LOG = Logger.getLogger(FreeGameServiceImpl.class);

	FreeGameServiceImpl() {
	}

	@Override
	public long startGame() {
		Game game = getNewGame(GameType.FREE, rulesService.getRules(FREE_RULES));
		try {
			GameInfo info = getGameInfo(game.getId());
			websocket.sendMessageToAllClients(info);
		} catch (GameNotFoundException e) {
			LOG.warn("Game couldn't be created");
		}
		return game.getId();
	}
}
