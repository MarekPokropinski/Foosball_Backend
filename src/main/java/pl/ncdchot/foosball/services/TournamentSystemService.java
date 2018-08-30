package pl.ncdchot.foosball.services;

import pl.ncdchot.foosball.modelDTO.FinishTournamentGameDTO;
import pl.ncdchot.foosball.modelDTO.PrepareToStartGameInTournamentDTO;
import pl.ncdchot.foosball.modelDTO.TournamentDTO;

public interface TournamentSystemService {
    TournamentDTO findGameInTournament(PrepareToStartGameInTournamentDTO prepareToStartGameInTournamentDTO);

    void sandResultToTournament(FinishTournamentGameDTO finishTournamentGameDTO);
}
