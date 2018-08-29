package pl.ncdchot.foosball.services.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.ncdchot.foosball.database.model.User;
import pl.ncdchot.foosball.database.model.UserHistory;
import pl.ncdchot.foosball.database.repository.UserRepository;
import pl.ncdchot.foosball.exceptions.UserNotExistException;
import pl.ncdchot.foosball.modelDTO.HistoryDTO;
import pl.ncdchot.foosball.services.ManagementSystemService;
import pl.ncdchot.foosball.services.UserHistoryService;
import pl.ncdchot.foosball.services.UserService;

@Service
public class UserServiceImpl implements UserService {
	private static final String winRatioFormat = "%.2f";
	private static final Logger LOG = Logger.getLogger(UserServiceImpl.class);
	@Autowired
	private UserRepository userRepository;
	@Autowired
	ManagementSystemService managementSystemService;
	@Autowired
	UserHistoryService userHistoryService;

	@Override
	public Optional<User> getUser(long userId) {
		return userRepository.findById(userId);
	}

	@Override
	public User getUserByExternalID(long externalId) throws UserNotExistException {
		Optional<User> optUser = userRepository.findByExternalID(externalId);
		if (optUser.isPresent()) {
			return optUser.get();
		} else if (managementSystemService.isUserInExternalService(externalId)) {
			UserHistory newHistory = userHistoryService.createNewHistory();
			return userRepository.save(new User(externalId, newHistory));
		} else {
			throw new UserNotExistException(externalId);
		}
	}

	@Override
	public User getUserByHistory(UserHistory userHistory) {
		return userRepository.findByUserHistory(userHistory);
	}

	@Override
	public List<HistoryDTO> getAllHistory() {
		// Iterable<UserHistory> allHistoriesIterable = userHistoryRepository.findAll();
		Iterable<User> allUsers = userRepository.findAll();
		// List<UserHistory> allHistories = Lists.newArrayList(allHistoriesIterable);

		List<HistoryDTO> histories = new ArrayList<>();
		List<UserHistory> userHistories = new ArrayList<>();
		for (User user : allUsers) {
			String nick;
			try {
				nick = managementSystemService.getExternalUserByExternalId(user.getExternalID()).getNick();
				histories.add(getHistoryDTO(user.getUserHistory(), nick));
				userHistories.add(user.getUserHistory());
			} catch (UserNotExistException e) {
				LOG.warn("Tried to get history of player who doesn't exist");
			}
		}

		List<Double> soloElo = new ArrayList<Double>();
		List<Double> duoElo = new ArrayList<Double>();

		for (UserHistory userHistory : userHistories) {
			soloElo.add(new Double(userHistory.getSoloEloPoints()));
			duoElo.add(new Double(userHistory.getTeamEloPoints()));
		}
		soloElo.sort((a, b) -> Double.compare(b, a));
		duoElo.sort((a, b) -> Double.compare(b, a));

		for (int i = 0; i < histories.size(); i++) {
			int soloRankingPos = soloElo.indexOf(userHistories.get(i).getSoloEloPoints()) + 1;
			int duoRankingPos = duoElo.indexOf(userHistories.get(i).getTeamEloPoints()) + 1;
			histories.get(i).setSoloRankingPos(String.valueOf(soloRankingPos));
			histories.get(i).setDuoRankingPos(String.valueOf(duoRankingPos));
		}
		return histories;
	}

	private HistoryDTO getHistoryDTO(UserHistory history, String nick) {
		return new HistoryDTO(nick, String.valueOf(history.getNormalPlayed()),
				String.format(winRatioFormat, getNormalWinRatio(history)),
				String.valueOf(history.getRankedSoloPlayed()),
				String.format(winRatioFormat, getRankedSoloWinRatio(history)),
				String.valueOf(history.getRankedDuoPlayed()),
				String.format(winRatioFormat, getRankedDuoWinRatio(history)), "", "");
	}

	private float getNormalWinRatio(UserHistory history) {
		if (history.getNormalPlayed() != 0) {
			return 100.0f * history.getNormalWins() / history.getNormalPlayed();
		} else {
			return 0.0f;
		}
	}

	private float getRankedSoloWinRatio(UserHistory history) {
		if (history.getRankedSoloPlayed() != 0) {
			return 100.0f * history.getRankedSoloWins() / history.getRankedSoloPlayed();
		} else {
			return 0.0f;
		}
	}

	private float getRankedDuoWinRatio(UserHistory history) {
		if (history.getRankedDuoPlayed() != 0) {
			return 100.0f * history.getRankedDuoWins() / history.getRankedDuoPlayed();
		} else {
			return 0.0f;
		}
	}
}
