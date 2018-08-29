package pl.ncdchot.foosball.modelDTO;

import java.util.Date;

public class GameHistoryDTO {
	private int redScore;
	private int blueScore;
	private int durationInSeconds;
	private int redLongestSeries;
	private int blueLongestSeries;
	private String[] redTeamNicks;
	private String[] blueTeamNicks;
	Date startDate;

	public GameHistoryDTO() {

	}

	public GameHistoryDTO(int redScore, int blueScore, int durationInSeconds, int redLongestSeries,
			int blueLongestSeries, String[] redTeamNicks, String[] blueTeamNicks, Date startDate) {
		this.redScore = redScore;
		this.blueScore = blueScore;
		this.durationInSeconds = durationInSeconds;
		this.redLongestSeries = redLongestSeries;
		this.blueLongestSeries = blueLongestSeries;
		this.redTeamNicks = redTeamNicks;
		this.blueTeamNicks = blueTeamNicks;
		this.startDate = startDate;
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

	public int getDurationInSeconds() {
		return durationInSeconds;
	}

	public void setDurationInSeconds(int durationInSeconds) {
		this.durationInSeconds = durationInSeconds;
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

	public String[] getRedTeamNicks() {
		return redTeamNicks;
	}

	public void setRedTeamNicks(String[] redTeamNicks) {
		this.redTeamNicks = redTeamNicks;
	}

	public String[] getBlueTeamNicks() {
		return blueTeamNicks;
	}

	public void setBlueTeamNicks(String[] blueTeamNicks) {
		this.blueTeamNicks = blueTeamNicks;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

}
