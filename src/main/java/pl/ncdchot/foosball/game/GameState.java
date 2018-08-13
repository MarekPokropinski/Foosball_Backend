package pl.ncdchot.foosball.game;

public class GameState {
	private long time;
	private int redScore;
	private int blueScore;
	private boolean finished;
	private long id;

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
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

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
