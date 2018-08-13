package pl.ncdchot.foosball.database.repository;

import org.springframework.data.repository.CrudRepository;

import pl.ncdchot.foosball.database.model.Goal;

public interface GoalRepository extends CrudRepository<Goal, Long> {

}
