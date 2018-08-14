package pl.ncdchot.foosball.services;

import java.util.Optional;

import pl.ncdchot.foosball.database.model.Game;
import pl.ncdchot.foosball.database.model.Rules;
import pl.ncdchot.foosball.database.model.Statistics;
import pl.ncdchot.foosball.database.model.Team;
import pl.ncdchot.foosball.exceptions.GameNotFoundException;
import pl.ncdchot.foosball.game.GameInfo;
import pl.ncdchot.foosball.game.GameSummary;
import pl.ncdchot.foosball.game.TeamColor;

public interface GameService {
	public Optional<Game> getLiveGame();

	public Game getCurrentGame(Rules rules, Team redTeam, Team blueTeam);
	
	public Game getCurrentGame(Rules rules);

	public void finishGame(long gameId) throws GameNotFoundException;

	public void goal(long gameId, TeamColor team) throws GameNotFoundException;

	public GameSummary getSummary(long gameId) throws GameNotFoundException;
	
	public GameInfo getGameInfo(long gameId) throws GameNotFoundException;

	public int getBlueScore(long gameId) throws GameNotFoundException;

	public int getRedScore(long gameId) throws GameNotFoundException;

	public Game getGame(long gameId) throws GameNotFoundException;

	public boolean isLive(long gameId);

	public Statistics createNewStats();
	
	
}
