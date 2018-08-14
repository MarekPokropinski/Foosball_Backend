package pl.ncdchot.foosball.database.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Statistics extends BaseModel {
	
	private int redScore;
	private int blueScore;
	private int redSeries;
	private int blueSeries;
	private long duration;
	
	public Statistics() {
		goals = new ArrayList<Goal>();
	}

	@OneToMany
	private List<Goal> goals;

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

	public void setGoals(List<Goal> goals) {
		this.goals = goals;
	}
}
