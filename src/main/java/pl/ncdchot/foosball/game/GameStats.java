package pl.ncdchot.foosball.game;

public class GameStats {
	int redScore;
	int blueScore;
	int redLongestSeries;
	int blueLongestSeries;
	long gameTime;

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

	public int getRedLongestSeries() {
		return redLongestSeries;
	}

	public void setRedLongestSeries(int redLongestSeries) {
		this.redLongestSeries = redLongestSeries;
	}

	public int getBlueLongestSeries() {
		return blueLongestSeries;
	}

	public void setBlueLongestSeries(int blueLongestSeries) {
		this.blueLongestSeries = blueLongestSeries;
	}

	public long getGameTime() {
		return gameTime;
	}

	public void setGameTime(long gameTime) {
		this.gameTime = gameTime;
	}
}
