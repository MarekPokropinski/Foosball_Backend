package pl.ncdchot.foosball.services;

import pl.ncdchot.foosball.database.model.Team;
import pl.ncdchot.foosball.database.model.User;

public interface TeamService {
	void saveTeam(Team team);

	Team getTeamByUsers(User user1, User user2);

	Team getTeamByUsers(User user);
}
