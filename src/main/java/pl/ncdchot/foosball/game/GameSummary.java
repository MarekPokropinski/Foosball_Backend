package pl.ncdchot.foosball.game;

public class GameSummary {
	private int redScore;
	private int blueScore;
	private long gameDuration;
	private int redLongestSeries;
	private int blueLongestSeries;

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

	public long getGameDuration() {
		return gameDuration;
	}

	public void setGameDuration(long gameDuration) {
		this.gameDuration = gameDuration;
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
}
