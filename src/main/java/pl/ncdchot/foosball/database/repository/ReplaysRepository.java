package pl.ncdchot.foosball.database.repository;

import org.springframework.data.repository.CrudRepository;
import pl.ncdchot.foosball.database.model.Replay;

public interface ReplaysRepository extends CrudRepository<Replay, Long> {
}
