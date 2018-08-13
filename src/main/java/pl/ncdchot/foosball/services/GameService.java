package pl.ncdchot.foosball.services;

import java.util.Optional;

import pl.ncdchot.foosball.database.model.Game;
import pl.ncdchot.foosball.database.model.Rules;
import pl.ncdchot.foosball.database.model.Statistics;
import pl.ncdchot.foosball.database.model.Team;
import pl.ncdchot.foosball.exceptions.GameNotFoundException;
import pl.ncdchot.foosball.game.GameSummary;
import pl.ncdchot.foosball.game.TeamColor;

public interface GameService {
	public Optional<Game> getLiveGame();

	public Game startGame(Rules rules, Statistics stats, Team redTeam, Team blueTeam);

	public void finishGame(long gameId) throws GameNotFoundException;

	public void goal(long gameId, TeamColor team) throws GameNotFoundException;

	public GameSummary getSummary(long gameId) throws GameNotFoundException;

	public int getBlueScore(long gameId) throws GameNotFoundException;

	public int getRedScore(long gameId) throws GameNotFoundException;

	public Game getGame(long gameId) throws GameNotFoundException;

	public boolean isLive(long gameId);

	public Statistics createNewStats();
}
