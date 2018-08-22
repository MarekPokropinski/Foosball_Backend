package pl.ncdchot.foosball.services.implementations;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import pl.ncdchot.foosball.database.model.User;
import pl.ncdchot.foosball.exceptions.UserNotExist;
import pl.ncdchot.foosball.modelDTO.UserDTO;
import pl.ncdchot.foosball.services.ManagementSystemService;
import pl.ncdchot.foosball.services.UserService;

@Service
public class ManagementSystemServiceImpl implements ManagementSystemService {
	private static final String USER_URL = "http://hotdev:8080/usrmgmt/user/";
	private static final Logger LOG = Logger.getLogger(ManagementSystemServiceImpl.class);
	private RestTemplate restTemplate;
	private UserService userService;

	@Autowired
	ManagementSystemServiceImpl(RestTemplate restTemplate, UserService userService) {
		this.restTemplate = restTemplate;
		this.userService = userService;
	}

	@Override
	public Optional<Long> getUserIdByNick(String nick) {
		List<?> l;
		try {
			l = restTemplate.getForObject(USER_URL + "all", List.class);
		} catch (RestClientException e) {
			LOG.warn("Failed to get users from user management");
			return Optional.empty();
		}
		for (Object o : l) {
			LinkedHashMap<?, ?> user = (LinkedHashMap<?, ?>) o;
			if (user.get("nick").equals(nick)) {
				Long id = new Long((Integer) user.get("id"));
				return Optional.of(id);
			}
		}
		return Optional.empty();
	}

	@Override
	public User getUserByExternalId(long externalId) throws UserNotExist {
		UserDTO user = restTemplate.getForObject(String.format("%sget/%s", USER_URL, externalId), UserDTO.class);
		if (user == null) {
			throw new UserNotExist(externalId);
		} else {
			return userService.getUserByExternalId(externalId);
		}
	}
}
