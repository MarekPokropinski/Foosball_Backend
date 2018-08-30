package pl.ncdchot.foosball.database.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import pl.ncdchot.foosball.database.model.Game;

public interface GameRepository extends CrudRepository<Game, Long> {
	Optional<Game> findByEndDate(Date date);

	List<Game> findTop10ByOrderByStartDateDesc();
}
