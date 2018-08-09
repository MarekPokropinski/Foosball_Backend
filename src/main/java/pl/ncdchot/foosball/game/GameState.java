package pl.ncdchot.foosball.game;

public class GameState {

	protected int redScore;
	protected int blueScore;
	boolean finished = false;
	long gameTime;
	
	long gameStartTime;

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

	public long getGameTime() {
		gameTime = System.currentTimeMillis() - gameStartTime;
		return gameTime;
	}
	public void restartTimer() {
		gameTime = 0;
		gameStartTime = System.currentTimeMillis();
	}
}
