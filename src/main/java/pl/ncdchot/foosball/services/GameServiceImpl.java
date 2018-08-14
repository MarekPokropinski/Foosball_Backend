package pl.ncdchot.foosball.services;

import java.util.Date;
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
import pl.ncdchot.foosball.database.repository.GameRepository;
import pl.ncdchot.foosball.exceptions.GameNotFoundException;
import pl.ncdchot.foosball.game.GameInfo;
import pl.ncdchot.foosball.game.GameSummary;
import pl.ncdchot.foosball.game.TeamColor;

@Service
public class GameServiceImpl implements GameService {

	private static final Logger LOG = Logger.getLogger(GameServiceImpl.class);

	@Autowired
	private GameRepository gameRepository;

	@Autowired
	private StatisticsService statsService;

	@Autowired
	private GoalService goalService;

	@Override
	public Optional<Game> getLiveGame() {
		return gameRepository.findByEndDate(null);
	}

	@Override
	public Game getCurrentGame(Rules rules, Team redTeam, Team blueTeam) {
		return liveGameExists() ? extistingGame() : createNewGame(rules, redTeam, blueTeam);
	}

	private boolean liveGameExists() {
		return getLiveGame().isPresent();
	}

	private Game extistingGame() {
		Game liveGame = getLiveGame().get();
		LOG.info("There is live game: " + liveGame.getId());
		return liveGame;
	}

	private Game createNewGame(Rules rules) {
		Statistics stats = statsService.createEmpty();
		Game game = new Game(GameType.NORMAL, rules, stats);
		gameRepository.save(game);
		return game;
	}

	private Game createNewGame(Rules rules, Team redTeam, Team blueTeam) {
		Statistics stats = statsService.createEmpty();
		Game game = new Game(GameType.NORMAL, rules, stats, redTeam, blueTeam);
		gameRepository.save(game);
		return game;
	}

	@Override
	public Game getCurrentGame(Rules rules) {
		return liveGameExists() ? extistingGame() : createNewGame(rules);
	}

	private long getTimeDifference(Date startDate) {
		return new Date().getTime() - startDate.getTime();
	}

	@Override
	public void finishGame(long gameId) throws GameNotFoundException {
		Optional<Game> optGame = gameRepository.findById(gameId);
		if (optGame.isPresent()) {
			Game game = optGame.get();
			game.setEndDate(new Date());
			Statistics stats = game.getStats();
			stats.setDuration((game.getEndDate().getTime() - game.getStartDate().getTime()) / 1000);
			statsService.saveStats(stats);
			gameRepository.save(game);
		} else {
			throw new GameNotFoundException();
		}
	}

	private void scoreGoal(Statistics stats, TeamColor team) {
		stats.getGoals().add(goalService.getNewGoal(team));
	}

	@Override
	public void goal(long gameId, TeamColor team) throws GameNotFoundException {
		Optional<Game> optGame = gameRepository.findById(gameId);
		if (optGame.isPresent()) {
			Game game = optGame.get();
			Statistics stats = game.getStats();
			switch (team) {
			case RED:
				stats.setRedScore(stats.getRedScore() + 1);
				break;
			case BLUE:
				stats.setBlueScore(stats.getBlueScore() + 1);
				break;
			}
			scoreGoal(stats, team);
			statsService.saveStats(stats);

		} else {
			throw new GameNotFoundException();
		}
	}

	@Override
	public GameSummary getSummary(long gameId) throws GameNotFoundException {
		GameSummary summary = new GameSummary();
		Optional<Game> optGame = gameRepository.findById(gameId);
		if (!optGame.isPresent()) {
			throw new GameNotFoundException();
		}
		Game game = optGame.get();
		Statistics stats = game.getStats();
		summary.setBlueScore(stats.getBlueScore());
		summary.setRedScore(stats.getRedScore());
		summary.setGameDuration(game.getEndDate().getTime() - game.getStartDate().getTime());

		int redSeries = 0;
		int blueSeries = 0;
		int series = 0;

		TeamColor previousGoalTeam = TeamColor.RED;

		stats.getGoals().sort((a, b) -> Long.compare(b.getTime(), a.getTime()));

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

		summary.setRedLongestSeries(redSeries);
		summary.setBlueLongestSeries(blueSeries);

		statsService.saveStats(stats);

		return summary;
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

		Game game = getGame(gameId);
		Statistics stats = game.getStats();

		GameInfo info = new GameInfo(game.getId(), stats.getRedScore(), stats.getBlueScore(),
				getTimeDifference(game.getStartDate()), false);

		return info;
	}

}
