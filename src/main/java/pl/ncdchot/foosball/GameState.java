package pl.ncdchot.foosball;

public class GameState {

	protected int redScore;
	protected int blueScore;
	boolean finished = false;

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

	public void incrementRed() {
		redScore++;
	}

	public void incrementBlue() {
		blueScore++;
	}

	public void resetScore() {
		redScore = blueScore = 0;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

}
