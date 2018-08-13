package pl.ncdchot.foosball.database.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Goal extends BaseModel {

	private long time;
	@ManyToOne
	private Team team;

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}
}
