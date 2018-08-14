package pl.ncdchot.foosball.services;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.ncdchot.foosball.database.model.Game;
import pl.ncdchot.foosball.database.model.Rules;
import pl.ncdchot.foosball.database.model.Statistics;
import pl.ncdchot.foosball.exceptions.GameNotFoundException;
import pl.ncdchot.foosball.game.GameInfo;
import pl.ncdchot.foosball.game.GameSummary;
import pl.ncdchot.foosball.game.TeamColor;
import pl.ncdchot.foosball.webSockets.SocketHandler;

@Service
public class FreeGameServiceImpl implements FreeGameService {
	private SocketHandler websocket;
	private RulesService rulesService;
	private GameService gameService;

	private static final Rules NORMAL_RULES = new Rules(0, 10, -1);
	private static final Logger LOG = Logger.getLogger(FreeGameServiceImpl.class);

	@Autowired
	FreeGameServiceImpl(GameService gameService, SocketHandler websocket, RulesService rulesService) {
		this.websocket = websocket;
		this.rulesService = rulesService;
		this.gameService = gameService;
		this.rulesService.getRules(NORMAL_RULES);
	}

	@Override
	public long startGame() {
		Statistics stats = gameService.createNewStats();
		Game game = gameService.getCurrentGame(NORMAL_RULES, stats);

		try {
			GameInfo info = gameService.getGameInfo(game.getId());
			websocket.sendMessageToAllClients(info);
		} catch (GameNotFoundException e) {
			LOG.warn("Game couldn't be created");
		}

		return game.getId();
	}

	@Override
	public void finishGame(long gameId) throws GameNotFoundException {
		gameService.finishGame(gameId);
	}

	@Override
	public void goal(long gameId, TeamColor team) throws GameNotFoundException {

		if (gameService.isLive(gameId) && rulesService.checkRules(gameService.getGame(gameId))) {
			gameService.goal(gameId, team);
			GameInfo info = gameService.getGameInfo(gameId);

			if (!rulesService.checkRules(gameService.getGame(gameId))) {
				info.setFinished(true);
			}

			websocket.sendMessageToAllClients(info);

			if (info.isFinished()) {
				finishGame(gameId);
			}
		} else {
			throw new GameNotFoundException();
		}
	}

	@Override
	public GameSummary getSummary(long id) throws GameNotFoundException {
		return gameService.getSummary(id);
	}

}
