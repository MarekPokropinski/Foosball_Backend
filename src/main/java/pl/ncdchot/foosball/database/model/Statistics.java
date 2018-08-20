package pl.ncdchot.foosball.database.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import pl.ncdchot.foosball.game.TeamColor;

@Entity
public class Statistics extends BaseModel {
	private int redScore;
	private int blueScore;
	private int redSeries;
	private int blueSeries;
	private long duration;
	@OneToMany
	private List<Goal> goals = new ArrayList<Goal>();

	public Statistics() {

	}

	public int getRedScore() {
		return redScore;
	}

	public void setRedScore(int redScore) {
		this.redScore = redScore;
	}

	public int getBlueScore() {
		return blueScore;
	}

	public void setBlueScore(int blueScore) {
		this.blueScore = blueScore;
	}

	public int getTeamScore(TeamColor team) { return team.equals(TeamColor.RED) ?  getRedScore() :  getBlueScore(); }

	public int getRedSeries() {
		return redSeries;
	}

	public void setRedSeries(int redSeries) {
		this.redSeries = redSeries;
	}

	public int getBlueSeries() {
		return blueSeries;
	}

	public void setBlueSeries(int blueSeries) {
		this.blueSeries = blueSeries;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public List<Goal> getGoals() {
		return goals;
	}
	public int lastGoalIndex() {
		return goals.size()-1;
	}

	public void setGoals(List<Goal> goals) {
		this.goals = goals;
	}
}
