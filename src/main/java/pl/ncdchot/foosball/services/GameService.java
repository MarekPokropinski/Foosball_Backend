package pl.ncdchot.foosball.services;

import pl.ncdchot.foosball.database.model.*;
import pl.ncdchot.foosball.exceptions.GameNotFoundException;
import pl.ncdchot.foosball.game.GameInfo;
import pl.ncdchot.foosball.game.GameSummary;
import pl.ncdchot.foosball.game.TeamColor;

import java.util.Optional;

public interface GameService {
	Optional<Game> getLiveGame();

	Game getCurrentGame(GameType gameType,Rules rules, Team redTeam, Team blueTeam);

	Game getCurrentGame(Rules rules);

	void finishGame(long gameId) throws GameNotFoundException;

	void goal(long gameId, TeamColor team) throws GameNotFoundException;

	void revertGoal(long gameId, TeamColor team) throws GameNotFoundException;

	GameSummary getSummary(long gameId) throws GameNotFoundException;

	GameInfo getGameInfo(long gameId) throws GameNotFoundException;

	int getBlueScore(long gameId) throws GameNotFoundException;

	int getRedScore(long gameId) throws GameNotFoundException;

	Game getGame(long gameId) throws GameNotFoundException;

	boolean isLive(long gameId);

	Statistics createNewStats();
}