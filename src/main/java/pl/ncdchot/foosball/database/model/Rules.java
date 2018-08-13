package pl.ncdchot.foosball.database.model;

import javax.persistence.Entity;

@Entity
public class Rules extends BaseModel {

	public Rules() {
		scoreLimit = 10;
		timeLimit = 0;
		creatorId = -1;
	}

	private int timeLimit;
	private int scoreLimit;
	private long creatorId;

	public Rules(int timeLimit, int scoreLimit, long creatorId) {
		this.timeLimit = timeLimit;
		this.scoreLimit = scoreLimit;
		this.creatorId = creatorId;
	}

	public int getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(int maxTime) {
		this.timeLimit = maxTime;
	}

	public int getScoreLimit() {
		return scoreLimit;
	}

	public void setScoreLimit(int scoreLimit) {
		this.scoreLimit = scoreLimit;
	}

	public long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}
}
