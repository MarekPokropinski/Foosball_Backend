package pl.ncdchot.foosball.modelDTO;

import pl.ncdchot.foosball.database.model.Goal;

public class ReplayDTO {
	private Goal goal;
	private String url;
	
	public ReplayDTO(Goal goal, String url) {
		this.goal = goal;
		this.url = url;
	}

	public Goal getGoal() {
		return goal;
	}

	public String getUrl() {
		return url;
	}
}
