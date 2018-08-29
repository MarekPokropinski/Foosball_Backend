package pl.ncdchot.foosball.modelDTO;

import java.util.Date;

import pl.ncdchot.foosball.database.model.GameType;

public class GameHistoryDTO {
	private int redScore;
	private int blueScore;
	private int gameDuration;
	private int redLongestSeries;
	private int blueLongestSeries;
	private String[] redTeamNicks;
	private String[] blueTeamNicks;
	Date dateStart;
	GameType typeOfGame;

	public GameHistoryDTO() {

	}

	public GameHistoryDTO(int redScore, int blueScore, int durationInSeconds, int redLongestSeries,
			int blueLongestSeries, String[] redTeamNicks, String[] blueTeamNicks, Date startDate, GameType typeOfGame) {
		this.redScore = redScore;
		this.blueScore = blueScore;
		this.gameDuration = durationInSeconds;
		this.redLongestSeries = redLongestSeries;
		this.blueLongestSeries = blueLongestSeries;
		this.redTeamNicks = redTeamNicks;
		this.blueTeamNicks = blueTeamNicks;
		this.dateStart = startDate;
		this.typeOfGame = typeOfGame;
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

	public int getGameDuration() {
		return gameDuration;
	}

	public void setGameDuration(int durationInSeconds) {
		this.gameDuration = durationInSeconds;
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

	public Date getDateStart() {
		return dateStart;
	}

	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}

	public GameType getTypeOfGame() {
		return typeOfGame;
	}

	public void setTypeOfGame(GameType typeOfGame) {
		this.typeOfGame = typeOfGame;
	}
}
