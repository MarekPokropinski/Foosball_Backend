package pl.ncdchot.foosball.services;

import java.util.Optional;

public interface TournamentSystemService {
	Optional<Long> getUserIdByNick(String nick);
}
