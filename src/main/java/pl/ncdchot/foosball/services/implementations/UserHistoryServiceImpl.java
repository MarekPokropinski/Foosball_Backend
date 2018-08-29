package pl.ncdchot.foosball.services.implementations;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.ncdchot.foosball.database.model.Game;
import pl.ncdchot.foosball.database.model.GameType;
import pl.ncdchot.foosball.database.model.Statistics;
import pl.ncdchot.foosball.database.model.Team;
import pl.ncdchot.foosball.database.model.User;
import pl.ncdchot.foosball.database.model.UserHistory;
import pl.ncdchot.foosball.database.repository.UserHistoryRepository;
import pl.ncdchot.foosball.game.TeamColor;
import pl.ncdchot.foosball.services.UserHistoryService;

@Service
public class UserHistoryServiceImpl implements UserHistoryService {
	private static final double ELO_DIFFERENCE_FACTOR = 400.0;
	private static final double ELO_BASE_10 = 10.0;
	private static final double ELO_WIN_RESULT = 1.0;
	private static final double ELO_DRAW_RESULT = 0.5;
	private static final double ELO_LOSS_RESULT = 0.0;
	private UserHistoryRepository userHistoryRepository;

	@Autowired
	UserHistoryServiceImpl(UserHistoryRepository userHistoryRepository) {
		this.userHistoryRepository = userHistoryRepository;
	}

	private void calculateEloPoints(UserHistory redHistory, UserHistory blueHistory, Statistics gameStats) {
		double redPlayerElo = redHistory.getSoloEloPoints();
		double bluePlayerElo = blueHistory.getSoloEloPoints();

		double goalDifferenceIndex = getGoalDifferenceIndex(gameStats);
		double expectedResult = getExpectedResult(redPlayerElo, bluePlayerElo);
		double redTeamResultOfMatch = getResultOfMatch(gameStats, TeamColor.RED);
		double blueTeamResultOfMatch = getResultOfMatch(gameStats, TeamColor.BLUE);

		redPlayerElo += goalDifferenceIndex * (redTeamResultOfMatch - expectedResult);
		bluePlayerElo += goalDifferenceIndex * (blueTeamResultOfMatch - expectedResult);

		redHistory.setSoloEloPoints(redPlayerElo);
		blueHistory.setSoloEloPoints(bluePlayerElo);
	}

	private void calculateEloPoints(UserHistory redHistory1, UserHistory redHistory2, UserHistory blueHistory1,
			UserHistory blueHistory2, Statistics gameStats) {
		double redTeamElo = (redHistory1.getTeamEloPoints() + redHistory2.getTeamEloPoints()) / 2;
		double blueTeamElo = (blueHistory1.getTeamEloPoints() + blueHistory2.getTeamEloPoints()) / 2;

		double goalDifferenceIndex = getGoalDifferenceIndex(gameStats);
		double expectedResult = getExpectedResult(redTeamElo, blueTeamElo);
		double redTeamResultOfMatch = getResultOfMatch(gameStats, TeamColor.RED);
		double blueTeamResultOfMatch = getResultOfMatch(gameStats, TeamColor.BLUE);

		double redEloChange = goalDifferenceIndex * (redTeamResultOfMatch - expectedResult) / 2;
		double blueEloChange = goalDifferenceIndex * (blueTeamResultOfMatch - expectedResult) / 2;

		redHistory1.setTeamEloPoints(redHistory1.getTeamEloPoints() + redEloChange);
		redHistory2.setTeamEloPoints(redHistory2.getTeamEloPoints() + redEloChange);
		blueHistory1.setTeamEloPoints(blueHistory1.getTeamEloPoints() + blueEloChange);
		blueHistory2.setTeamEloPoints(blueHistory2.getTeamEloPoints() + blueEloChange);
	}

	private double getExpectedResult(double redElo, double blueElo) {
		double difference = redElo - blueElo;
		return 1.0 / (Math.pow(ELO_BASE_10, -difference / ELO_DIFFERENCE_FACTOR) + 1.0);
	}

	private double getResultOfMatch(Statistics gameStats, TeamColor team) {
		if (gameStats.getBlueScore() == gameStats.getRedScore()) {
			return ELO_DRAW_RESULT;
		} else if (gameStats.getRedScore() > gameStats.getBlueScore()) {
			return team.equals(TeamColor.RED) ? ELO_WIN_RESULT : ELO_LOSS_RESULT;
		} else {
			return team.equals(TeamColor.BLUE) ? ELO_WIN_RESULT : ELO_LOSS_RESULT;
		}
	}

	private double getGoalDifferenceIndex(Statistics gameStats) {
		int goalDifference = Math.abs(gameStats.getRedScore() - gameStats.getBlueScore());
		if (goalDifference <= 1) {
			return 1.0;
		} else if (goalDifference <= 2) {
			return 3.0 / 2.0;
		} else {
			return (11.0 + goalDifference) / 8.0;
		}
	}

	private void updateEloPointsSolo(Team redTeam, Team blueTeam, Statistics gameStats) {
		User redPlayer = redTeam.getUsers().get(0);
		User bluePlayer = blueTeam.getUsers().get(0);

		UserHistory redHistory = redPlayer.getUserHistory();
		UserHistory blueHistory = bluePlayer.getUserHistory();
		calculateEloPoints(redHistory, blueHistory, gameStats);

		userHistoryRepository.save(redHistory);
		userHistoryRepository.save(blueHistory);
	}

	private void updateEloPointsDuo(Team redTeam, Team blueTeam, Statistics gameStats) {
		UserHistory redHistory1 = redTeam.getUsers().get(0).getUserHistory();
		UserHistory redHistory2 = redTeam.getUsers().get(1).getUserHistory();
		UserHistory blueHistory1 = blueTeam.getUsers().get(0).getUserHistory();
		UserHistory blueHistory2 = blueTeam.getUsers().get(1).getUserHistory();
		calculateEloPoints(redHistory1, redHistory2, blueHistory1, blueHistory2, gameStats);
		userHistoryRepository.saveAll(Arrays.asList(redHistory1, redHistory2, blueHistory1, blueHistory2));
	}

	private void updateEloPoints(Team redTeam, Team blueTeam, Statistics gameStats) {
		int redPlayersSize = redTeam.getUsers().size();
		int bluePlayersSize = blueTeam.getUsers().size();

		if (redPlayersSize == 1 && bluePlayersSize == 1) {
			updateEloPointsSolo(redTeam, blueTeam, gameStats);
		} else if (redPlayersSize == 2 && bluePlayersSize == 2) {
			updateEloPointsDuo(redTeam, blueTeam, gameStats);
		}
	}

	private void updateHistoryOfNormalGame(Game game, TeamColor winningTeam) {
		for (User user : game.getRedTeam().getUsers()) {
			UserHistory hist = user.getUserHistory();
			if (winningTeam.equals(TeamColor.RED)) {
				hist.setNormalWins(hist.getNormalWins() + 1);
			}
			hist.setNormalPlayed(hist.getNormalPlayed() + 1);
			userHistoryRepository.save(hist);
		}
		for (User user : game.getBlueTeam().getUsers()) {
			UserHistory hist = user.getUserHistory();
			if (winningTeam.equals(TeamColor.BLUE)) {
				hist.setNormalWins(hist.getNormalWins() + 1);
			}
			hist.setNormalPlayed(hist.getNormalPlayed() + 1);
			userHistoryRepository.save(hist);
		}
	}

	private void updateHistoryOfRankedSoloGame(Game game, TeamColor winningTeam) {
		UserHistory hist = game.getRedTeam().getUsers().get(0).getUserHistory();
		if (winningTeam.equals(TeamColor.RED)) {
			hist.setRankedSoloWins(hist.getRankedSoloWins() + 1);
		}
		hist.setRankedSoloPlayed(hist.getRankedSoloPlayed() + 1);
		userHistoryRepository.save(hist);

		hist = game.getBlueTeam().getUsers().get(0).getUserHistory();
		if (winningTeam.equals(TeamColor.BLUE)) {
			hist.setRankedSoloWins(hist.getRankedSoloWins() + 1);
		}
		hist.setRankedSoloPlayed(hist.getRankedSoloPlayed() + 1);
		userHistoryRepository.save(hist);
	}

	private void updateHistoryOfRankedDuoGame(Game game, TeamColor winningTeam) {
		for (User user : game.getRedTeam().getUsers()) {
			UserHistory hist = user.getUserHistory();
			if (winningTeam.equals(TeamColor.RED)) {
				hist.setRankedDuoWins(hist.getRankedDuoWins() + 1);
			}
			hist.setRankedDuoPlayed(hist.getRankedDuoPlayed() + 1);
			userHistoryRepository.save(hist);
		}
		for (User user : game.getBlueTeam().getUsers()) {
			UserHistory hist = user.getUserHistory();
			if (winningTeam.equals(TeamColor.BLUE)) {
				hist.setRankedDuoWins(hist.getRankedDuoWins() + 1);
			}
			hist.setRankedDuoPlayed(hist.getRankedDuoPlayed() + 1);
			userHistoryRepository.save(hist);
		}
	}

	private void updateHistoryOfRankedGame(Game game, TeamColor winningTeam) {
		updateEloPoints(game.getRedTeam(), game.getBlueTeam(), game.getStats());
		if (game.getRedTeam().getUsers().size() == 1) {
			updateHistoryOfRankedSoloGame(game, winningTeam);
		} else {
			updateHistoryOfRankedDuoGame(game, winningTeam);
		}
	}

	@Override
	public void updateHistory(Game game) {
		Statistics stats = game.getStats();
		TeamColor winningTeam = stats.getBlueScore() > stats.getRedScore() ? TeamColor.BLUE : TeamColor.RED;
		if (game.getType().equals(GameType.NORMAL)) {
			updateHistoryOfNormalGame(game, winningTeam);
		} else if (game.getType().equals(GameType.RANKED)) {
			updateHistoryOfRankedGame(game, winningTeam);
		}
	}

	@Override
	public UserHistory createNewHistory() {
		return userHistoryRepository.save(new UserHistory());
	}
}
