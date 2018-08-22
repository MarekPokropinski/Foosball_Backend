package pl.ncdchot.foosball.services;

import pl.ncdchot.foosball.database.model.GameType;
import pl.ncdchot.foosball.database.model.Rules;
import pl.ncdchot.foosball.exceptions.GameNotFoundException;
import pl.ncdchot.foosball.exceptions.UserNotExist;
import pl.ncdchot.foosball.game.GameSummary;

public interface GameWithHistoryService extends GameService {
    long startGame(long[] users, Rules rules, GameType gameType) throws UserNotExist;

    long startGame(long[] users, Rules rules) throws UserNotExist;

//    long startGame(long[] users) throws UserNotExist;

    GameSummary getSummary(long gameId) throws GameNotFoundException;
}
