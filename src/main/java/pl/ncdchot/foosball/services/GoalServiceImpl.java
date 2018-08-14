package pl.ncdchot.foosball.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.ncdchot.foosball.database.model.Goal;
import pl.ncdchot.foosball.database.repository.GoalRepository;
import pl.ncdchot.foosball.game.TeamColor;

@Service
public class GoalServiceImpl implements GoalService {

	@Autowired
	GoalRepository goalRepository;

	@Override
	public Goal getNewGoal(TeamColor team) {
		Goal goal = new Goal(team);
		goalRepository.save(goal);
		return goal;
	}
}
