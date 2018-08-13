package pl.ncdchot.foosball.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.ncdchot.foosball.database.model.Game;
import pl.ncdchot.foosball.database.model.GameType;
import pl.ncdchot.foosball.database.model.Goal;
import pl.ncdchot.foosball.database.model.Rules;
import pl.ncdchot.foosball.database.model.Statistics;
import pl.ncdchot.foosball.database.model.Team;
import pl.ncdchot.foosball.database.repository.GameRepository;
import pl.ncdchot.foosball.database.repository.StatsRepository;
import pl.ncdchot.foosball.database.repository.TeamRepository;
import pl.ncdchot.foosball.exceptions.GameNotFoundException;
import pl.ncdchot.foosball.game.GameState;
import pl.ncdchot.foosball.game.GameSummary;
import pl.ncdchot.foosball.game.TeamColor;

@Service
public class GameServiceImpl implements GameService {

	@Autowired
	private GameRepository gameRepository;
	@Autowired
	private TeamRepository teamRepository;
	@Autowired
	private StatsRepository statsRepository;

	@Override
	public Optional<Game> getLiveGame() {
		return gameRepository.findByEndDate(null);
	}

	@Override
	public Game startGame(Rules rules, Statistics stats, Team redTeam, Team blueTeam) {

		Optional<Game> liveGame = getLiveGame();

		if (liveGame.isPresent()) {
			System.out.println("There is live game: " + liveGame.get().getId());
			return liveGame.get();
		}

		teamRepository.save(redTeam);
		teamRepository.save(blueTeam);

		Game game = new Game();
		game.setStartDate(new Date());
		game.setType(GameType.NORMAL);
		game.setRules(rules);
		game.setStats(stats);

		game.setBlueTeam(blueTeam);
		game.setRedTeam(redTeam);

		gameRepository.save(game);

		GameState data = new GameState();
		data.setId(game.getId());
		data.setBlueScore(stats.getBlueScore());
		data.setRedScore(stats.getRedScore());
		data.setFinished(false);
		data.setTime(0);

		return game;
	}

	@Override
	public void finishGame(long gameId) throws GameNotFoundException {
		Optional<Game> optGame = gameRepository.findById(gameId);
		if (optGame.isPresent()) {
			Game game = optGame.get();
			game.setEndDate(new Date());
			Statistics stats = game.getStats();
			stats.setDuration((game.getEndDate().getTime() - game.getStartDate().getTime()) / 1000);
			statsRepository.save(stats);
			gameRepository.save(game);
		} else {
			throw new GameNotFoundException();
		}
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
			statsRepository.save(stats);

			GameState data = new GameState();
			data.setId(game.getId());
			data.setBlueScore(stats.getBlueScore());
			data.setRedScore(stats.getRedScore());
			data.setFinished(false);
			data.setTime(0);

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

		long redTeamId = game.getRedTeam().getId();
		long blueTeamId = game.getBlueTeam().getId();

		long t = redTeamId;

		for (Goal goal : stats.getGoals()) {
			if (t == goal.getTeam().getId()) {
				series++;
			} else {
				series = 1;
			}
			if (goal.getTeam().getId() == redTeamId) {
				redSeries = Math.max(redSeries, series);
			} else {
				blueTeamId = Math.max(blueTeamId, series);
			}
		}

		stats.setRedSeries(redSeries);
		stats.setBlueSeries(blueSeries);

		summary.setRedLongestSeries(redSeries);
		summary.setBlueLongestSeries(blueSeries);

		statsRepository.save(stats);

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
		Statistics stats = new Statistics();
		statsRepository.save(stats);
		return stats;
	}

}
