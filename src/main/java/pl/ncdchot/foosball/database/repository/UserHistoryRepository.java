package pl.ncdchot.foosball.database.repository;

import org.springframework.data.repository.CrudRepository;

import pl.ncdchot.foosball.database.model.UserHistory;

public interface UserHistoryRepository extends CrudRepository<UserHistory, Long> {
}