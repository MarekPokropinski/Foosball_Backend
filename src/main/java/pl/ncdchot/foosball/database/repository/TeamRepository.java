package pl.ncdchot.foosball.database.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import pl.ncdchot.foosball.database.model.Team;
import pl.ncdchot.foosball.database.model.User;

public interface TeamRepository extends CrudRepository<Team, Long> {
	@SuppressWarnings("rawtypes")
	List<Team> findByUsers(List user);

	@Query("select t from Team t join t.users u1 join t.users u2 where u1=:user1 and u2=:user2")
	Optional<Team> getTeam(User user1, User user2);

	@SuppressWarnings("rawtypes")
	List<Team> findByUsersEquals(List users);
}
