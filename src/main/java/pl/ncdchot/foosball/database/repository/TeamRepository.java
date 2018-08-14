package pl.ncdchot.foosball.database.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import pl.ncdchot.foosball.database.model.Team;

public interface TeamRepository extends CrudRepository<Team, Long> {
	@SuppressWarnings("rawtypes")
	List<Team> findByUsers(List user);	
}
