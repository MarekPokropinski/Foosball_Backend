package pl.ncdchot.foosball.services.implementations;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.ncdchot.foosball.database.model.Game;
import pl.ncdchot.foosball.database.model.GameType;
import pl.ncdchot.foosball.database.model.Goal;
import pl.ncdchot.foosball.database.model.Rules;
import pl.ncdchot.foosball.database.model.Statistics;
import pl.ncdchot.foosball.database.model.Team;
import pl.ncdchot.foosball.database.model.User;
import pl.ncdchot.foosball.database.repository.GameRepository;
import pl.ncdchot.foosball.exceptions.GameNotFoundException;
import pl.ncdchot.foosball.exceptions.UserNotExistException;
import pl.ncdchot.foosball.game.GameInfo;
import pl.ncdchot.foosball.game.GameSummary;
import pl.ncdchot.foosball.game.TeamColor;
import pl.ncdchot.foosball.modelDTO.GameHistoryDTO;
import pl.ncdchot.foosball.services.GameService;
import pl.ncdchot.foosball.services.GoalService;
import pl.ncdchot.foosball.services.ManagementSystemService;
import pl.ncdchot.foosball.services.ReplaysService;
import pl.ncdchot.foosball.services.RulesService;
import pl.ncdchot.foosball.services.StatisticsService;
import pl.ncdchot.foosball.services.UserHistoryService;
import pl.ncdchot.foosball.webSockets.SocketHandler;

@Service("gameService")
public class GameServiceImpl implements GameService {

	private static final Logger LOG = Logger.getLogger(GameServiceImpl.class);

	@Autowired
	protected GameRepository gameRepository;

	@Autowired
	protected StatisticsService statsService;

	@Autowired
	protected SocketHandler websocket;

	@Autowired
	protected GoalService goalService;

	@Autowired
	protected RulesService rulesService;

	@Autowired
	private UserHistoryService userHistoryService;

	@Autowired
	private ReplaysService replaysService;

	@Autowired
	private ManagementSystemService managementSystemService;

	@Override
	public Optional<Game> getLiveGame() {
		return gameRepository.findByEndDate(null);
	}

	@Override
	public Game getNewGame(GameType gameType, Rules rules, Team redTeam, Team blueTeam) {
		finishLiveGame();
		return createNewGame(gameType, rules, redTeam, blueTeam);
	}

	private void finishLiveGame() {
		getLiveGame().ifPresent((livegame -> {
			try {
				finishGame(livegame.getId());
			} catch (GameNotFoundException e) {
				LOG.warn("Couldn't end live game");
			}
		}));
	}

	private Game createNewGame(GameType type, Rules rules) {
		Statistics stats = statsService.createEmpty();
		Game game = new Game(type, rules, stats);
		gameRepository.save(game);
		return game;
	}

	private Game createNewGame(GameType gameType, Rules rules, Team redTeam, Team blueTeam) {
		Statistics stats = statsService.createEmpty();
		Game game = new Game(gameType, rules, stats, redTeam, blueTeam);
		gameRepository.save(game);
		return game;
	}

	@Override
	public Game getNewGame(GameType type, Rules rules) {
		finishLiveGame();
		return createNewGame(type, rules);
	}

	private long getTimeDifference(Date startDate) {
		return new Date().getTime() - startDate.getTime();
	}

	@Override
	public void finishGame(long gameId) throws GameNotFoundException {
		Optional<Game> optGame = gameRepository.findById(gameId);
		if (optGame.isPresent()) {
			Game game = optGame.get();
			if (game.getEndDate() == null) {
				game.setEndDate(new Date());
				Statistics stats = game.getStats();
				stats.setDuration(Duration.ofMillis(game.getEndDate().getTime() - game.getStartDate().getTime()));
				statsService.saveStats(stats);
				gameRepository.save(game);
				userHistoryService.updateHistory(game);
			}
			GameInfo info = getGameInfo(gameId);
			info.setFinished(true);
			websocket.sendMessageToAllClients(info);
		} else {
			throw new GameNotFoundException();
		}
	}

	private Goal scoreGoal(Game game, TeamColor team) {
		Statistics stats = game.getStats();
		changeScore(stats, team, 1);
		Goal goal = goalService.getNewGoal(team);
		stats.getGoals().add(goal);
		statsService.saveStats(stats);
		return goal;
	}

	private void changeScore(Statistics stats, TeamColor team, int points) {
		switch (team) {
		case RED:
			stats.setRedScore(stats.getRedScore() + points);
			break;
		case BLUE:
			stats.setBlueScore(stats.getBlueScore() + points);
			break;
		}
	}

	@Override
	public void goal(long gameId, TeamColor team) throws GameNotFoundException {
		if (isLive(gameId) && rulesService.checkRules(getGame(gameId))) {
			Game game = getGame(gameId);
			Goal newGoal = scoreGoal(game, team);
			GameInfo info = getGameInfo(gameId);

			replaysService.saveReplay(game, newGoal);
			;

			if (!rulesService.checkRules(getGame(gameId))) {
				info.setFinished(true);
				finishGame(gameId);
			} else {
				websocket.sendMessageToAllClients(info);
			}
		} else {
			throw new GameNotFoundException();
		}
	}

	@Override
	public void revertGoal(long gameId, TeamColor team) throws GameNotFoundException {
		if (isLive(gameId) && rulesService.checkRules(getGame(gameId))) {
			Game game = getGame(gameId);
			removeGoal(game, team);
			GameInfo info = getGameInfo(gameId);
			websocket.sendMessageToAllClients(info);

		} else {
			throw new GameNotFoundException();
		}
	}

	private Goal getLastGoalByTeam(Statistics stats, TeamColor team) {
		stats.getGoals().sort((a, b) -> (b.getTime().compareTo(a.getTime())));
		Goal toRemoval = null;
		for (Goal g : stats.getGoals()) {
			if (g.getTeam().equals(team)) {
				toRemoval = g;
				break;
			}
		}
		return toRemoval;
	}

	private void removeGoal(Game game, TeamColor team) {
		Statistics stats = game.getStats();
		if (stats.getTeamScore(team) > 0) {
			changeScore(stats, team, -1);
			Goal toRemoval = getLastGoalByTeam(stats, team);
			stats.getGoals().remove(toRemoval);
			statsService.saveStats(stats);
		}
	}

	private void getSeries(Statistics stats) {
		int redSeries = 0;
		int blueSeries = 0;
		int series = 0;

		TeamColor previousGoalTeam = TeamColor.RED;

		stats.getGoals().sort((a, b) -> b.getTime().compareTo(a.getTime()));

		for (Goal goal : stats.getGoals()) {
			if (previousGoalTeam.equals(goal.getTeam())) {
				series++;
			} else {
				series = 1;
			}
			if (goal.getTeam().equals(TeamColor.RED)) {
				redSeries = Math.max(redSeries, series);
			} else {
				blueSeries = Math.max(blueSeries, series);
			}
			previousGoalTeam = goal.getTeam();
		}
		stats.setRedSeries(redSeries);
		stats.setBlueSeries(blueSeries);
		statsService.saveStats(stats);
	}

	@Override
	public GameSummary getSummary(long gameId) throws GameNotFoundException {
		Optional<Game> optGame = gameRepository.findById(gameId);
		Game game = optGame.orElseThrow(GameNotFoundException::new);
		Statistics stats = game.getStats();
		getSeries(stats);
		return new GameSummary(stats.getRedScore(), stats.getBlueScore(),
				game.getEndDate().getTime() - game.getStartDate().getTime(), stats.getRedSeries(),
				stats.getBlueSeries());
	}

	@Override
	public int getBlueScore(long gameId) throws GameNotFoundException {
		Optional<Game> optGame = gameRepository.findById(gameId);
		if (optGame.isPresent()) {
			return optGame.get().getStats().getBlueScore();
		} else {
			throw new GameNotFoundException();
		}
	}

	@Override
	public int getRedScore(long gameId) throws GameNotFoundException {
		Optional<Game> optGame = gameRepository.findById(gameId);
		if (optGame.isPresent()) {
			return optGame.get().getStats().getRedScore();
		} else {
			throw new GameNotFoundException();
		}
	}

	@Override
	public Game getGame(long gameId) throws GameNotFoundException {
		Optional<Game> optGame = gameRepository.findById(gameId);
		if (optGame.isPresent()) {
			return optGame.get();
		} else {
			throw new GameNotFoundException();
		}
	}

	@Override
	public boolean isLive(long gameId) {
		Optional<Game> optGame = gameRepository.findById(gameId);
		if (optGame.isPresent()) {
			if (optGame.get().getEndDate() == null) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Statistics createNewStats() {
		return statsService.createEmpty();
	}

	@Override
	public GameInfo getGameInfo(long gameId) throws GameNotFoundException {
		boolean isGameFinished = false;
		Game game = getGame(gameId);
		if (game.getEndDate() != null) {
			isGameFinished = true;
		}
		Statistics stats = game.getStats();
		GameInfo info = new GameInfo(game.getId(), stats.getRedScore(), stats.getBlueScore(),
				getTimeDifference(game.getStartDate()), isGameFinished);

		return info;
	}

	@Override
	public void updateDuration(Game game) {
		Statistics stats = game.getStats();
		stats.setDuration(Duration.ofMillis(new Date().getTime() - game.getStartDate().getTime()));
	}

	@Override
	public List<GameHistoryDTO> getLastGames() {
		List<Game> games = gameRepository.findTop10ByOrderByStartDate();
		List<GameHistoryDTO> lastGames = new ArrayList<>();
		for (Game game : games) {
			lastGames.add(createGameHistoryDTO(game));
		}
		return lastGames;
	}

	private GameHistoryDTO createGameHistoryDTO(Game game) {
		String[] redNicks;
		String[] blueNicks;
		if (game.getType() == GameType.FREE) {
			redNicks = new String[0];
			blueNicks = new String[0];
		} else {
			redNicks = getUsersNicks(game.getRedTeam().getUsers());
			blueNicks = getUsersNicks(game.getBlueTeam().getUsers());
		}
		Statistics stats = game.getStats();
		GameHistoryDTO gameHistory = new GameHistoryDTO(stats.getRedScore(), stats.getBlueScore(),
				(int) stats.getDuration().getSeconds(), stats.getRedSeries(), stats.getBlueSeries(), redNicks,
				blueNicks, game.getStartDate(), game.getType());
		return gameHistory;
	}

	private String[] getUsersNicks(List<User> users) {
		return users.stream().map(user -> {
			try {
				return managementSystemService.getExternalUserByExternalId(user.getExternalID()).getNick();
			} catch (UserNotExistException e) {
				return "Deleted user";
			}
		}).toArray(String[]::new);
	}
}
