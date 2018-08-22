package pl.ncdchot.foosball.services.implementations;

import java.util.ArrayList;
import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import pl.ncdchot.foosball.database.model.Game;
import pl.ncdchot.foosball.database.model.GameType;
import pl.ncdchot.foosball.database.model.Rules;
import pl.ncdchot.foosball.database.model.Team;
import pl.ncdchot.foosball.database.model.User;
import pl.ncdchot.foosball.exceptions.GameNotFoundException;
import pl.ncdchot.foosball.exceptions.UserNotExist;
import pl.ncdchot.foosball.game.GameInfo;
import pl.ncdchot.foosball.game.GameSummary;
import pl.ncdchot.foosball.game.GameWithIdentificationSummary;
import pl.ncdchot.foosball.game.TeamColor;
import pl.ncdchot.foosball.services.GameService;
import pl.ncdchot.foosball.services.GameWithHistoryService;
import pl.ncdchot.foosball.services.RulesService;
import pl.ncdchot.foosball.services.TeamService;
import pl.ncdchot.foosball.services.UserService;

public abstract class GameWithHistoryServiceImpl extends GameServiceImpl implements GameWithHistoryService {
	private static final Logger LOG = Logger.getLogger(GameWithHistoryServiceImpl.class);

	private static final int SOLO_GAME_SIZE = 2;

	private static final int FIRST_RED_PLAYER_INDEX = 0;
	private static final int SECOND_RED_PLAYER_INDEX = 2;

	private static final int FIRST_BLUE_PLAYER_INDEX = 1;
	private static final int SECOND_BLUE_PLAYER_INDEX = 3;

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
		Team redTeam = createTeam(TeamColor.RED, users);
		Team blueTeam = createTeam(TeamColor.BLUE, users);

		Rules savedRules = rulesService.getRules(rules);
		return getCurrentGame(gameType, savedRules, redTeam, blueTeam);
	}

	private Team createTeam(TeamColor teamColor, long[] users) throws UserNotExist {
		Team team;
		User firstUser = getUser(
				users[teamColor.equals(TeamColor.RED) ? FIRST_RED_PLAYER_INDEX : FIRST_BLUE_PLAYER_INDEX]);
		if (isGameSolo(users)) {
			team = teamService.getTeamByUsers(firstUser);
		} else {
			User secondUser = getUser(
					users[teamColor.equals(TeamColor.RED) ? SECOND_RED_PLAYER_INDEX : SECOND_BLUE_PLAYER_INDEX]);
			team = teamService.getTeamByUsers(firstUser, secondUser);
			return team;
		}
		return team;
	}

	private User getUser(long userID) throws UserNotExist {
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

		return new GameWithIdentificationSummary(baseSummary, redTeamIds, blueTeamIds);
	}
}
