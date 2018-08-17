package pl.ncdchot.foosball.database.model;

import javax.persistence.Entity;

@Entity
public class Rules extends BaseModel {
	private int timeLimit;
	private int scoreLimit;
	private long creatorId;

	private static final int NO_TIME_LIMIT = 0;
	private static final int NO_CREATOR = -1;

	public Rules() {
		timeLimit = NO_TIME_LIMIT;
		creatorId = NO_CREATOR;
	}

	public Rules(int scoreLimit) {
		this();
		this.scoreLimit = scoreLimit;
	}

	public Rules(int scoreLimit, int timeLimit) {
		this(scoreLimit);
		this.timeLimit = timeLimit;
	}

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
