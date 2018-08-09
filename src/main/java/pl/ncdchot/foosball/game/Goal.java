package pl.ncdchot.foosball.game;

public class Goal {
	long time;
	TeamColor team;

	public Goal(long time, TeamColor team) {
		this.time = time;
		this.team = team;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public TeamColor getTeam() {
		return team;
	}

	public void setTeam(TeamColor team) {
		this.team = team;
	}
}
