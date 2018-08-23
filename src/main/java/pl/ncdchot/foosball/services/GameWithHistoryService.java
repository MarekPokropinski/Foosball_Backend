package pl.ncdchot.foosball.services;

import pl.ncdchot.foosball.database.model.GameType;
import pl.ncdchot.foosball.database.model.Rules;
import pl.ncdchot.foosball.exceptions.UserNotExist;

public interface GameWithHistoryService extends GameService {
    long startGame(long[] redTeamUsers, long[] blueTeamUsers, Rules rules, GameType gameType) throws UserNotExist;

    long startGame(long[] redTeamUsers, long[] blueTeamUsers, Rules rules) throws UserNotExist;

//    GameSummary getSummary(long gameId) throws GameNotFoundException;
}
