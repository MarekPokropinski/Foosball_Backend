package pl.ncdchot.foosball.services;

import pl.ncdchot.foosball.game.TeamColor;


public interface RankedGameService {
	public long startGame(long[] redTeamIds, long[] blueTeamIds);
	public void finishGame(long gameId);
	public void goal(long gameId, TeamColor team);
}
