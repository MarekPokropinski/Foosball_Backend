package pl.ncdchot.foosball.database.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import pl.ncdchot.foosball.database.model.Team;
import pl.ncdchot.foosball.database.model.User;

public interface TeamRepository extends CrudRepository<Team, Long> {
	@SuppressWarnings("rawtypes")
	List<Team> findByUsers(User user);

}
