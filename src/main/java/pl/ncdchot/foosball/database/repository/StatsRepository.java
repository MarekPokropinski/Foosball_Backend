package pl.ncdchot.foosball.database.repository;

import org.springframework.data.repository.CrudRepository;

import pl.ncdchot.foosball.database.model.Statistics;

public interface StatsRepository extends CrudRepository<Statistics, Long> {
	
}
