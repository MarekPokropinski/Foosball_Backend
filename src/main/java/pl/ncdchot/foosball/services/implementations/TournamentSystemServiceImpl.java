package pl.ncdchot.foosball.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import pl.ncdchot.foosball.modelDTO.FinishTournamentGameDTO;
import pl.ncdchot.foosball.modelDTO.PrepareToStartGameInTournamentDTO;
import pl.ncdchot.foosball.modelDTO.TournamentDTO;
import pl.ncdchot.foosball.services.TournamentSystemService;

@Service
public class TournamentSystemServiceImpl implements TournamentSystemService {
	@Value("${tournament.url}")
	private String TOURNAMENT_URL;
	private static final int FIRST_TOURNAMENT = 0;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public TournamentDTO findGameInTournament(PrepareToStartGameInTournamentDTO prepareToStartGameInTournamentDTO) {
		String url = TOURNAMENT_URL + "/check";
		TournamentDTO[] list = restTemplate.postForObject(url, prepareToStartGameInTournamentDTO,
				TournamentDTO[].class);
		return list[FIRST_TOURNAMENT];
	}

	@Override
	public void sandResultToTournament(FinishTournamentGameDTO finishTournamentGameDTO) {
		String url = TOURNAMENT_URL + "/finish";
		restTemplate.postForLocation(url, finishTournamentGameDTO);
	}

}
