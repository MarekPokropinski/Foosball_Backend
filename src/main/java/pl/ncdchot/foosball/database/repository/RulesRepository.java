package pl.ncdchot.foosball.database.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import pl.ncdchot.foosball.database.model.Rules;

public interface RulesRepository extends CrudRepository<Rules, Long> {
	Optional<Rules> findByCreatorIdAndScoreLimitAndTimeLimit(long creatorId, int scoreLimit, long timeLimit);
}
