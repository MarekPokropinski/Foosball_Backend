package pl.ncdchot.foosball.services.implementations;

import java.util.Optional;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import pl.ncdchot.foosball.exceptions.TeamNoExistException;
import pl.ncdchot.foosball.exceptions.UserByCardIDNotExistException;
import pl.ncdchot.foosball.exceptions.UserByNickNoExistException;
import pl.ncdchot.foosball.exceptions.UserNotExistException;
import pl.ncdchot.foosball.modelDTO.UserDTO;
import pl.ncdchot.foosball.services.ManagementSystemService;

@Service
public class ManagementSystemServiceImpl implements ManagementSystemService {
	private static final Logger LOG = Logger.getLogger(ManagementSystemServiceImpl.class);

	@Value("${userManagement.user.url}")
	private static final String USER_URL = "http://hotdev:8080/usrmgmt/user";
	@Value("${userManagement.team.url}")
	private static final String TEAM_URL = "http://hotdev:8080/usrmgmt/team";

	private RestTemplate restTemplate;

	@Autowired
	ManagementSystemServiceImpl(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Override
	public Optional<Long> getUserIdByNick(String nick) {
		UserDTO[] users;
		try {
			users = restTemplate.getForObject(USER_URL + "/all", UserDTO[].class);
		} catch (RestClientException e) {
			LOG.warn("Failed to get users from user management");
			return Optional.empty();
		}
		for (UserDTO user : users) {
			if (user.getNick().equals(nick)) {
				Long id = Long.parseLong(user.getId());
				return Optional.of(id);
			}
		}
		return Optional.empty();
	}

	@Override
	public UserDTO getExternalUserByExternalId(long externalId) throws UserNotExistException {
		try {
			UserDTO user = restTemplate.getForObject(String.format("%s/get/%s", USER_URL, externalId), UserDTO.class);
			if (user != null) {
				return user;
			} else {
				throw new UserNotExistException(externalId);
			}
		} catch (RestClientException e) {
			LOG.warn("Error with connection to external service");
			throw new UserNotExistException(externalId);
		}
	}

	@Override
	public boolean isUserInExternalService(long externalId) {
		try {
			UserDTO user = restTemplate.getForObject(String.format("%s/get/%s", USER_URL, externalId), UserDTO.class);
			return user != null;
		} catch (RestClientException e) {
			LOG.warn("Error with connection to external service");
			return false;
		}
	}

	@Override
	public UserDTO getUserByCardID(String cardID) throws UserByCardIDNotExistException {
		String url = String.format("%s/get/by-cardid/%s", USER_URL, cardID);
		UserDTO user = restTemplate.getForObject(url, UserDTO.class);
		if (user != null) {
			return user;
		} else {
			throw new UserByCardIDNotExistException(cardID);
		}
	}

	@Override
	public UserDTO getUserByNickOrCardID(String textValue)
			throws UserByCardIDNotExistException, UserByNickNoExistException, UserNotExistException {

		if (isCardID(textValue)) {
			return getUserByCardID(textValue);
		} else if (isNickName(textValue)) {
			return getUserByNick(textValue);
		} else
			throw new UserNotExistException(Long.valueOf(textValue));
	}

	private boolean isNickName(String textValue) {
		String regex = "[a-zA-Z_0-9]*";
		return textValue.matches(regex) && textValue.length() > 0;
	}

	private boolean isCardID(String textValue) {
		String regex = "[0-9]+";
		return textValue.matches(regex) && textValue.length() == 10;
	}

	private UserDTO getUserByNick(String nickName) throws UserByNickNoExistException {
		String url = String.format("%s/get/by-nick/%s", USER_URL, nickName);
		UserDTO user;
		try {
			user = restTemplate.getForObject(url, UserDTO.class);
		} catch (RestClientException e) {
			LOG.warn("Error with connection to external service");
			throw new UserByNickNoExistException(nickName);
		}
		if (user == null) {
			throw new UserByNickNoExistException(nickName);
		}
		return user;
	}

	@Override
	public String getTournamentTeamID(long[] teamUsersID) throws TeamNoExistException {
		String url = String.format("%s/get-by-users", TEAM_URL);
		String teamID = restTemplate.postForObject(url, teamUsersID, String.class);
		if (teamID == null) {
			throw new TeamNoExistException();
		}
		return teamID;
	}

}
