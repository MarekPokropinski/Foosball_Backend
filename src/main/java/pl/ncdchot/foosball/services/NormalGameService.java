package pl.ncdchot.foosball.services;

import pl.ncdchot.foosball.exceptions.GameNotFoundException;
import pl.ncdchot.foosball.game.GameSummary;
import pl.ncdchot.foosball.game.TeamColor;

public interface NormalGameService {
	public long startGame();
	public void finishGame(long gameId) throws GameNotFoundException;
	public void goal(long gameId, TeamColor team) throws GameNotFoundException;
	public GameSummary getSummary(long gameId) throws GameNotFoundException;
}
