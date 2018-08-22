package pl.ncdchot.foosball.services.implementations;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.ncdchot.foosball.database.model.User;
import pl.ncdchot.foosball.database.repository.UserRepository;
import pl.ncdchot.foosball.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public User getUser(long userId) {
		Optional<User> optUser = userRepository.findById(userId);

		if (optUser.isPresent()) {
			return optUser.get();
		} else {
			User user = new User();
			userRepository.save(user);
			return user;
		}
	}

	@Override
	public User getUserByExternalId(long externalId) {
		Optional<User> optUser = userRepository.findByExternalID(externalId);
		return optUser.orElseGet(() -> userRepository.save(new User(externalId)));
	}

}
