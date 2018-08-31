package pl.ncdchot.foosball.services;

import pl.ncdchot.foosball.exceptions.TournamentGameDontExistsException;
import pl.ncdchot.foosball.modelDTO.FinishTournamentGameDTO;
import pl.ncdchot.foosball.modelDTO.CheckTournamentDTO;
import pl.ncdchot.foosball.modelDTO.GameTournamentDTO;

public interface TournamentSystemService {
    GameTournamentDTO findGameInTournament(CheckTournamentDTO checkTournamentDTO) throws TournamentGameDontExistsException;

    void sandResultToTournament(FinishTournamentGameDTO finishTournamentGameDTO);
}
