package pl.ncdchot.foosball.services.implementations;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import pl.ncdchot.foosball.database.model.Game;
import pl.ncdchot.foosball.database.model.Goal;
import pl.ncdchot.foosball.database.model.Replay;
import pl.ncdchot.foosball.database.repository.ReplaysRepository;
import pl.ncdchot.foosball.modelDTO.ReplayDTO;
import pl.ncdchot.foosball.services.ReplaysService;

@Service
public class ReplaysServiceImpl implements ReplaysService {

	private RestTemplate restTemplate;
	private ReplaysRepository replaysRepository;
	
	@Autowired
	ReplaysServiceImpl(RestTemplate restTemplate, ReplaysRepository replaysRepository) {
		this.restTemplate = restTemplate;
		this.replaysRepository = replaysRepository;
	}
	
	@Value("${value.url.cam}")
	private String url;
	
	@Override
	public ArrayList<ReplayDTO> getReplays(long gameId) {
		ArrayList<ReplayDTO> replays = new ArrayList<ReplayDTO>();
		for(Replay replay : replaysRepository.findAll()) {
			if(replay.getGameId() == gameId) {
				replays.add(new ReplayDTO(replay.getGoal(), replay.getUrl()));
			}
		}
		replays.sort((ReplayDTO a, ReplayDTO b) -> {
			return a.getGoal().getTime().compareTo(b.getGoal().getTime());
		});
		return replays;
	}

	@Override
	@Async
	public void saveReplay(Game game, Goal goal) {
		
		String replayUrl;
		try {
			replayUrl = restTemplate.getForObject(url, String.class);
		} catch(Exception e) {
			replayUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstleyVEVO";
		}
		
		replaysRepository.save(new Replay(game.getId(), goal, replayUrl));
	}
}
