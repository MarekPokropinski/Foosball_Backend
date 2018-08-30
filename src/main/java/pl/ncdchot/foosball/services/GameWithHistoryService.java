package pl.ncdchot.foosball.services;

import pl.ncdchot.foosball.database.model.GameType;
import pl.ncdchot.foosball.database.model.Rules;
import pl.ncdchot.foosball.exceptions.GameNotFoundException;
import pl.ncdchot.foosball.exceptions.TeamNoExistException;
import pl.ncdchot.foosball.exceptions.UserNotExistException;

public interface GameWithHistoryService extends GameService {
    long startGame(long[] redTeamUsers, long[] blueTeamUsers, Rules rules, GameType gameType) throws UserNotExistException;

    long startGame(long[] redTeamUsers, long[] blueTeamUsers, Rules rules) throws UserNotExistException, TeamNoExistException, GameNotFoundException;

}
