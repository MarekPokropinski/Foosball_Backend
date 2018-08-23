package pl.ncdchot.foosball.services.implementations;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import pl.ncdchot.foosball.exceptions.UserNotExist;
import pl.ncdchot.foosball.modelDTO.UserDTO;
import pl.ncdchot.foosball.services.ManagementSystemService;

@Service
public class ManagementSystemServiceImpl implements ManagementSystemService {
	private static final String USER_URL = "http://hotdev:8080/usrmgmt/user/";
	private static final Logger LOG = Logger.getLogger(ManagementSystemServiceImpl.class);
	private RestTemplate restTemplate;

	@Autowired
	ManagementSystemServiceImpl(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Override
	public Optional<Long> getUserIdByNick(String nick) {
		List<?> listOfAllUsers;
		try {
			listOfAllUsers = restTemplate.getForObject(USER_URL + "all", List.class);
		} catch (RestClientException e) {
			LOG.warn("Failed to get users from user management");
			return Optional.empty();
		}
		for (Object userObject : listOfAllUsers) {
			LinkedHashMap<?, ?> user = (LinkedHashMap<?, ?>) userObject;
			if (user.get("nick").equals(nick)) {
				Long id = new Long((Integer) user.get("id"));
				return Optional.of(id);
			}
		}
		return Optional.empty();
	}

	@Override
	public UserDTO getExternalUserByExternalId(long externalId) throws UserNotExist {
		UserDTO user = restTemplate.getForObject(String.format("%sget/%s", USER_URL, externalId), UserDTO.class);
		if (user != null) {
			return user;
		} else {
			throw new UserNotExist(externalId);
		}
	}

	@Override
	public boolean isUserInExternalService(long externalId) {
		UserDTO user = restTemplate.getForObject(String.format("%sget/%s", USER_URL, externalId), UserDTO.class);
		return user != null;
	}
}
