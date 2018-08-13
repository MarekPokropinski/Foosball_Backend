package pl.ncdchot.foosball.database.repository;

import org.springframework.data.repository.CrudRepository;

import pl.ncdchot.foosball.database.model.Rules;

public interface RulesRepository extends CrudRepository<Rules, Long> {
	
}
