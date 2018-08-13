package pl.ncdchot.foosball.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.ncdchot.foosball.database.model.Game;
import pl.ncdchot.foosball.database.model.Rules;
import pl.ncdchot.foosball.database.model.Statistics;
import pl.ncdchot.foosball.database.model.Team;
import pl.ncdchot.foosball.database.repository.StatsRepository;
import pl.ncdchot.foosball.exceptions.GameNotFoundException;
import pl.ncdchot.foosball.game.GameState;
import pl.ncdchot.foosball.game.GameSummary;
import pl.ncdchot.foosball.game.TeamColor;
import pl.ncdchot.foosball.webSockets.SocketHandler;

@Service
public class FreeGameServiceImpl implements FreeGameService {

	private SocketHandler websocket;
	private RulesService rulesService;

	@Autowired
	private GameService gameService;

	private static final Rules NORMAL_RULES = new Rules(0, 10, -1);
	private static final Team RED_TEAM = new Team();
	private static final Team BLUE_TEAM = new Team();

	@Autowired
	FreeGameServiceImpl(StatsRepository statsRepository, SocketHandler websocket, RulesService rulesService) {

		this.websocket = websocket;
		this.rulesService = rulesService;
		this.rulesService.AddRules(NORMAL_RULES);
	}

	@Override
	public long startGame() {

		Statistics stats = gameService.createNewStats();

		Game game = gameService.startGame(NORMAL_RULES, stats, RED_TEAM, BLUE_TEAM);

		GameState data = new GameState();
		data.setId(game.getId());
		data.setBlueScore(stats.getBlueScore());
		data.setRedScore(stats.getRedScore());
		data.setFinished(false);
		data.setTime(0);

		websocket.sendMessageToAllClients(data);
		return game.getId();
	}

	@Override
	public void finishGame(long gameId) throws GameNotFoundException {
		gameService.finishGame(gameId);
	}

	@Override
	public void goal(long gameId, TeamColor team) throws GameNotFoundException {

		if (gameService.isLive(gameId)) {
			
			gameService.goal(gameId, team);
			
			GameState data = new GameState();
			data.setId(gameId);
			data.setBlueScore(gameService.getBlueScore(gameId));
			data.setRedScore(gameService.getRedScore(gameId));
			data.setFinished(false);
			data.setTime(0);

			if (!rulesService.checkRules(gameService.getGame(gameId))) {
				data.setFinished(true);
			}

			websocket.sendMessageToAllClients(data);

			if (data.isFinished()) {
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
