package pl.ncdchot.foosball.services.implementations;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.ncdchot.foosball.database.model.User;
import pl.ncdchot.foosball.database.repository.UserRepository;
import pl.ncdchot.foosball.exceptions.UserNotExistException;
import pl.ncdchot.foosball.services.ManagementSystemService;
import pl.ncdchot.foosball.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	ManagementSystemService managementSystemService;

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
			return userRepository.save(new User(externalId));
		} else {
			throw new UserNotExistException(externalId);
		}
	}
}
