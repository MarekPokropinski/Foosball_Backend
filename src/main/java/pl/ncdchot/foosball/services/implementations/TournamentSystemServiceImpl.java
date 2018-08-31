package pl.ncdchot.foosball.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import pl.ncdchot.foosball.modelDTO.FinishTournamentGameDTO;
import pl.ncdchot.foosball.modelDTO.CheckTournamentDTO;
import pl.ncdchot.foosball.modelDTO.GameTournamentDTO;
import pl.ncdchot.foosball.services.TournamentSystemService;

@Service
public class TournamentSystemServiceImpl implements TournamentSystemService {
	@Value("${tournament.url}")
	private String TOURNAMENT_URL;
	private static final int FIRST_TOURNAMENT = 0;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public GameTournamentDTO findGameInTournament(CheckTournamentDTO checkTournamentDTO) {
		String url = String.format("%s/check",TOURNAMENT_URL);
		GameTournamentDTO[] list = restTemplate.postForObject(url, checkTournamentDTO,
				GameTournamentDTO[].class);
		return list[FIRST_TOURNAMENT];
	}

	@Override
	public void sandResultToTournament(FinishTournamentGameDTO finishTournamentGameDTO) {
		String url = String.format("%s/finish",TOURNAMENT_URL);
		restTemplate.postForLocation(url, finishTournamentGameDTO);
	}

}
