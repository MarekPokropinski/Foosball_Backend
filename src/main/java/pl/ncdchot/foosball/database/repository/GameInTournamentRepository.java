package pl.ncdchot.foosball.database.repository;


import org.springframework.data.repository.CrudRepository;
import pl.ncdchot.foosball.database.model.GameInTournament;

import java.util.Optional;

public interface GameInTournamentRepository extends CrudRepository<GameInTournament, Long> {
    Optional<GameInTournament> findByGame_Id(long gameId);

}
