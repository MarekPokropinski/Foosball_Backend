package pl.ncdchot.foosball.database.model;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class Replay extends BaseModel {
	private long gameId;
	@OneToOne
	private Goal goal;
	private String url;
	
	public Replay() {
		
	}
	
	public Replay(long gameId, Goal goal, String url) {
		this.gameId = gameId;
		this.goal = goal;
		this.url = url;
	}
	
	public long getGameId() {
		return gameId;
	}
	public Goal getGoal() {
		return goal;
	}
	public String getUrl() {
		return url;
	}	
}
