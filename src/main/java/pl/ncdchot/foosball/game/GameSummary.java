package pl.ncdchot.foosball.game;

public class GameSummary {
	private int redScore;
	private int blueScore;
	private long gameDuration;
	private int redLongestSeries;
	private int blueLongestSeries;

	public GameSummary() {

	}

	public GameSummary(int redScore, int blueScore, long gameDuration, int redLongestSeries, int blueLongestSeries) {
		this.redScore = redScore;
		this.blueScore = blueScore;
		this.gameDuration = gameDuration;
		this.redLongestSeries = redLongestSeries;
		this.blueLongestSeries = blueLongestSeries;
	}

	public GameSummary(GameSummary summary) {
		this.redScore = summary.redScore;
		this.blueScore = summary.blueScore;
		this.gameDuration = summary.gameDuration;
		this.redLongestSeries = summary.redLongestSeries;
		this.blueLongestSeries = summary.blueLongestSeries;
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
