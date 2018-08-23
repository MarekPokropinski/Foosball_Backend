package pl.ncdchot.foosball.database.model;

import java.time.Duration;

import javax.persistence.Entity;

@Entity
public class Rules extends BaseModel {
	private static final Duration NO_TIME_LIMIT = Duration.ZERO;
	private static final int NO_CREATOR = -1;
	private Duration timeLimit;
	private int scoreLimit;
	private long creatorId;

	public Rules() {
		timeLimit = NO_TIME_LIMIT;
		creatorId = NO_CREATOR;
	}

	public Rules(int scoreLimit) {
		this();
		this.scoreLimit = scoreLimit;
	}

	public Rules(int scoreLimit, Duration timeLimit) {
		this(scoreLimit);
		this.timeLimit = timeLimit;
	}

	public Rules(int scoreLimit, Duration timeLimit, long creatorId) {
		this(scoreLimit, timeLimit);
		this.creatorId = creatorId;
	}

	public Duration getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(Duration maxTime) {
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
