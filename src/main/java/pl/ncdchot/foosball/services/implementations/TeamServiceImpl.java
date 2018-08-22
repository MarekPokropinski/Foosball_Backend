package pl.ncdchot.foosball.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.ncdchot.foosball.database.model.Team;
import pl.ncdchot.foosball.database.repository.TeamRepository;
import pl.ncdchot.foosball.services.TeamService;

import java.util.Optional;

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
    public Optional<Team> getTeamByUsers(long firstUserID, long lastUserID) {
        return teamRepository.getTeam(firstUserID, lastUserID);
    }
}
