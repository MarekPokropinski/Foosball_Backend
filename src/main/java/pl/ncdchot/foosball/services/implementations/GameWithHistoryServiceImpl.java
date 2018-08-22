package pl.ncdchot.foosball.services.implementations;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import pl.ncdchot.foosball.database.model.*;
import pl.ncdchot.foosball.exceptions.GameNotFoundException;
import pl.ncdchot.foosball.exceptions.UserNotExist;
import pl.ncdchot.foosball.game.GameInfo;
import pl.ncdchot.foosball.game.GameSummary;
import pl.ncdchot.foosball.game.GameWithIdentificationSummary;
import pl.ncdchot.foosball.modelDTO.UserDTO;
import pl.ncdchot.foosball.services.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class GameWithHistoryServiceImpl extends GameServiceImpl implements GameWithHistoryService {
    private static final String GET_USER_URL = "http://hotdev:8080/usrmgmt/user/get/";

    private static final Logger LOG = Logger.getLogger(GameWithHistoryServiceImpl.class);

    private static final int SOLO_GAME_SIZE = 2;

    private static final int FIRST_RED_PLAYER_INDEX = 0;
    private static final int SECOND_RED_PLAYER_INDEX = 2;

    private static final int FIRST_BLUE_PLAYER_INDEX = 1;
    private static final int SECOND_BLUE_PLAYER_INDEX = 3;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TeamService teamService;

    @Autowired
    private RulesService rulesService;

    @Autowired
    private UserService userService;

    @Autowired
    private GameService gameService;

    @Override
    public long startGame(long[] users, Rules rules, GameType gameType) throws UserNotExist {
        Game game = createGame(users, rules, gameType);
        try {
            GameInfo gameInfo = getGameInfo(game.getId());
            websocket.sendMessageToAllClients(gameInfo);
        } catch (GameNotFoundException e) {
            LOG.warn("Game: " + game + " couldn't be created");
        }
        return game.getId();
    }

    private Game createGame(long[] users, Rules rules, GameType gameType) throws UserNotExist {
        Team redTeam = createTeam(true, users);
        Team blueTeam = createTeam(false, users);

        Rules savedRules = rulesService.getRules(rules);
        return getCurrentGame(gameType, savedRules, redTeam, blueTeam);
    }

    private Team createTeam(boolean isRed, long[] users) throws UserNotExist {
        Team team;
        User firstUser = getUser(users[isRed ? FIRST_RED_PLAYER_INDEX : FIRST_BLUE_PLAYER_INDEX]);
        if (isGameSolo(users)) {
            team = new Team(firstUser);
        } else {
            User secondUser = getUser(users[isRed ? SECOND_RED_PLAYER_INDEX : SECOND_BLUE_PLAYER_INDEX]);
            team = new Team(Arrays.asList(firstUser, secondUser));
        }
        teamService.saveTeam(team);
        return team;
    }

    private User getUser(long userID) throws UserNotExist {
        Optional<User> user = userService.getUserByExternalID(userID);
        if (user.isPresent()) {
            return user.get();
        } else {
            String url = GET_USER_URL + userID;
            UserDTO userDTO = restTemplate.getForObject(url, UserDTO.class);

            if (userDTO != null) {
                User tempUser = new User(Long.parseLong(userDTO.getId()));
                userService.saveUser(tempUser);
                return tempUser;
            }
            throw new UserNotExist(userID);
        }
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

        return new GameWithIdentificationSummary(baseSummary, redTeamIds, blueTeamIds);
    }
}
