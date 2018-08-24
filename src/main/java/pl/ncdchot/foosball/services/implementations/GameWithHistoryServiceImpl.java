package pl.ncdchot.foosball.services.implementations;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import pl.ncdchot.foosball.database.model.*;
import pl.ncdchot.foosball.exceptions.GameNotFoundException;
import pl.ncdchot.foosball.exceptions.UserNotExistException;
import pl.ncdchot.foosball.game.GameInfo;
import pl.ncdchot.foosball.game.GameSummary;
import pl.ncdchot.foosball.game.GameWithHistoryInfo;
import pl.ncdchot.foosball.game.GameWithHistorySummary;
import pl.ncdchot.foosball.services.*;

import java.util.ArrayList;
import java.util.List;

public abstract class GameWithHistoryServiceImpl extends GameServiceImpl implements GameWithHistoryService {
    private static final Logger LOG = Logger.getLogger(GameWithHistoryServiceImpl.class);

    private static final int SOLO_GAME_SIZE = 1;

    private static final int FIRST_PLAYER_INDEX = 0;
    private static final int SECOND_PLAYER_INDEX = 1;

    @Autowired
    private TeamService teamService;

    @Autowired
    private RulesService rulesService;

    @Autowired
    private UserService userService;

    @Autowired
    private GameService gameService;

    @Override
    public long startGame(long[] redTeamUsers, long[] blueTeamUsers, Rules rules, GameType gameType) throws UserNotExistException {
        Game game = createGame(redTeamUsers, blueTeamUsers, rules, gameType);
        try {
            GameInfo gameInfo = getGameInfo(game.getId());
            websocket.sendMessageToAllClients(gameInfo);
        } catch (GameNotFoundException e) {
            LOG.warn("Game: " + game + " couldn't be created");
        }
        return game.getId();
    }

    private Game createGame(long[] redTeamUsers, long[] blueTeamUsers, Rules rules, GameType gameType) throws UserNotExistException {
        Team redTeam = createTeam(redTeamUsers);
        Team blueTeam = createTeam(blueTeamUsers);

        Rules savedRules = rulesService.getRules(rules);
        return getCurrentGame(gameType, savedRules, redTeam, blueTeam);
    }

    private Team createTeam(long[] users) throws UserNotExistException {
        Team team;
        User firstUser = getUser(
                users[FIRST_PLAYER_INDEX]);
        if (isGameSolo(users)) {
            team = teamService.getTeamByUsers(firstUser);
        } else {
            User secondUser = getUser(
                    users[SECOND_PLAYER_INDEX]);
            team = teamService.getTeamByUsers(firstUser, secondUser);
            return team;
        }
        return team;
    }

    private User getUser(long userID) throws UserNotExistException {
        return userService.getUserByExternalID(userID);
    }

    private boolean isGameSolo(long[] users) {
        return users.length == SOLO_GAME_SIZE;
    }

    @Override
    public GameSummary getSummary(long gameId) throws GameNotFoundException {
        GameSummary baseSummary = super.getSummary(gameId);
        Game game = gameService.getGame(gameId);
        List<User> redTeamUsers = game.getRedTeam().getUsers();
        List<User> blueTeamUsers = game.getBlueTeam().getUsers();

        List<Long> redTeamIds = new ArrayList<>(redTeamUsers.size());
        List<Long> blueTeamIds = new ArrayList<>(blueTeamUsers.size());

        for (User user : redTeamUsers) {
            redTeamIds.add(user.getExternalID());
        }
        for (User user : blueTeamUsers) {
            blueTeamIds.add(user.getExternalID());
        }

        return new GameWithHistorySummary(baseSummary, redTeamIds, blueTeamIds);
    }

    @Override
    public GameInfo getGameInfo(long gameId) throws GameNotFoundException {
        GameInfo base = super.getGameInfo(gameId);
        Game game = gameService.getGame(gameId);
        Long[] redTeamIds = game.getRedTeam().getUsers().stream().map(user -> user.getId()).toArray(Long[]::new);
        Long[] blueTeamIds = game.getBlueTeam().getUsers().stream().map(user -> user.getId()).toArray(Long[]::new);
        return new GameWithHistoryInfo(base, game.getRules().getTimeLimit().toMillis(), redTeamIds, blueTeamIds);
    }
}
