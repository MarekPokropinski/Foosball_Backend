package pl.ncdchot.foosball.services.implementations;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import pl.ncdchot.foosball.services.TournamentSystemService;

@Service
public class TournamentSystemServiceImpl implements TournamentSystemService {
	private static final String USER_URL = "http://hotdev:8080/usrmgmt/user/";
	private RestTemplate restTemplate;

	@Autowired
	TournamentSystemServiceImpl(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Override
	public Optional<Long> getUserIdByNick(String nick) {
		List<?> l = restTemplate.getForObject(USER_URL + "all", List.class);
		for (Object o : l) {
			LinkedHashMap<?, ?> user = (LinkedHashMap<?, ?>) o;
			if (user.get("nick").equals(nick)) {
				Long id = new Long((Integer) user.get("id"));
				return Optional.of(id);
			}
		}
		return Optional.empty();
	}
}
