package pl.ncdchot.foosball.services.implementations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.ncdchot.foosball.database.model.Team;
import pl.ncdchot.foosball.database.model.User;
import pl.ncdchot.foosball.database.repository.TeamRepository;
import pl.ncdchot.foosball.services.TeamService;

@Service
public class TeamServiceImpl implements TeamService {

	@Autowired
	private TeamRepository teamRepository;

	@Override
	public void saveTeam(Team team) {
		teamRepository.save(team);
	}

	public Optional<Team> getTeamById(long temID) {
		return teamRepository.findById(temID);
	}

	@Override
	public Team getTeamByUsers(User user1, User user2) {
		return teamRepository.getTeam(user1, user2)
				.orElseGet(() -> teamRepository.save(new Team(Arrays.asList(user1, user2))));
	}

	@Override
	public Team getTeamByUsers(User user) {

		List<Team> teams = teamRepository.findByUsers(Arrays.asList(user));
		for (Team team : teams) {
			if (team.getUsers().size() == 1) {
				return team;
			}
		}

		return teamRepository.save(new Team(Arrays.asList(user)));
	}
}
