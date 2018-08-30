package pl.ncdchot.foosball.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.ncdchot.foosball.database.model.GameInTournament;
import pl.ncdchot.foosball.database.repository.GameInTournamentRepository;

import java.util.Optional;

@Service
public class GameInTournamentService {

    @Autowired
    private GameInTournamentRepository repository;

    public GameInTournament getTournamentGameByNormalID(long gameId) {
        Optional<GameInTournament> game = repository.findByGame_Id(gameId);
        return game.orElse(null);
    }

    public void save(GameInTournament gameInTournament) {
        repository.save(gameInTournament);
    }
}
