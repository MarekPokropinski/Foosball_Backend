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
	Optional<Game> getLiveGame();

	Game getCurrentGame(Rules rules, Team redTeam, Team blueTeam);

	Game getCurrentGame(Rules rules);

	void finishGame(long gameId) throws GameNotFoundException;

	void goal(long gameId, TeamColor team) throws GameNotFoundException;

	GameSummary getSummary(long gameId) throws GameNotFoundException;

	GameInfo getGameInfo(long gameId) throws GameNotFoundException;

	int getBlueScore(long gameId) throws GameNotFoundException;

	int getRedScore(long gameId) throws GameNotFoundException;

	Game getGame(long gameId) throws GameNotFoundException;

	boolean isLive(long gameId);

	Statistics createNewStats();
}