package pl.ncdchot.foosball.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.ncdchot.foosball.database.model.Team;
import pl.ncdchot.foosball.database.repository.TeamRepository;

@Service
public class TeamServiceImpl implements TeamService {

	@Autowired
	private TeamRepository teamRepository;

	@Override
	public void saveTeam(Team team) {
		teamRepository.save(team);
	}

}
