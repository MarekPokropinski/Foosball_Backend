package pl.ncdchot.foosball.game;

public class GameInfo {
	private long time;
	private int redScore;
	private int blueScore;
	private boolean finished;
	private long id;

	public GameInfo(long id, int redScore, int blueScore, long time, boolean finished) {
		this.id = id;
		this.redScore = redScore;
		this.blueScore = blueScore;
		this.time = time;
		this.finished = finished;
	}

	public GameInfo(GameInfo info) {
		this.id = info.id;
		this.redScore = info.redScore;
		this.blueScore = info.blueScore;
		this.time = info.time;
		this.finished = info.finished;
	}

	public GameInfo() {

	}

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
