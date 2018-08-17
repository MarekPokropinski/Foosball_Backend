package pl.ncdchot.foosball.database.model;

import java.util.Date;

import javax.persistence.Entity;

import pl.ncdchot.foosball.game.TeamColor;

@Entity
public class Goal extends BaseModel {
	private Date time;
	private TeamColor team;

	public Goal() {

	}

	public Goal(TeamColor team) {
		this.team = team;
		this.time = new Date();
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public TeamColor getTeam() {
		return team;
	}

	public void setTeam(TeamColor team) {
		this.team = team;
	}
}
