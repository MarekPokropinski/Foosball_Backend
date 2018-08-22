package pl.ncdchot.foosball.database.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import pl.ncdchot.foosball.database.model.User;
import pl.ncdchot.foosball.database.model.UserHistory;

public interface UserHistoryRepository extends CrudRepository<UserHistory, Long> {
	Optional<UserHistory> findByUser(User user);
}