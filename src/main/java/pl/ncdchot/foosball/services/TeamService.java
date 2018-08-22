package pl.ncdchot.foosball.services;

import pl.ncdchot.foosball.database.model.Team;

import java.util.Optional;

public interface TeamService {
    void saveTeam(Team team);

    Optional<Team> getTeamByUsers(long firstUserID, long secondUserId);
}
