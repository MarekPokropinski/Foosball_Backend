package pl.ncdchot.foosball.database.model;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class UserHistory extends BaseModel {
	@OneToOne
	private User user;
	private int rankedSoloWins;
	private int rankedSoloPlayed;
	private int rankedDuoWins;
	private int rankedDuoPlayed;
	private int normalWins;
	private int normalPlayed;
	private double soloEloPoints;
	private double teamEloPoints;

	public UserHistory() {
	}

	public UserHistory(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getRankedSoloWins() {
		return rankedSoloWins;
	}

	public void setRankedSoloWins(int rankedSoloWins) {
		this.rankedSoloWins = rankedSoloWins;
	}

	public int getRankedSoloPlayed() {
		return rankedSoloPlayed;
	}

	public void setRankedSoloPlayed(int rankedSoloPlayed) {
		this.rankedSoloPlayed = rankedSoloPlayed;
	}

	public int getRankedDuoWins() {
		return rankedDuoWins;
	}

	public void setRankedDuoWins(int rankedDuoWins) {
		this.rankedDuoWins = rankedDuoWins;
	}

	public int getRankedDuoPlayed() {
		return rankedDuoPlayed;
	}

	public void setRankedDuoPlayed(int rankedDuoPlayed) {
		this.rankedDuoPlayed = rankedDuoPlayed;
	}

	public double getSoloEloPoints() {
		return soloEloPoints;
	}

	public void setSoloEloPoints(double soloEloPoints) {
		this.soloEloPoints = soloEloPoints;
	}

	public double getTeamEloPoints() {
		return teamEloPoints;
	}

	public void setTeamEloPoints(double teamEloPoints) {
		this.teamEloPoints = teamEloPoints;
	}

	public int getNormalWins() {
		return normalWins;
	}

	public void setNormalWins(int normalWins) {
		this.normalWins = normalWins;
	}

	public int getNormalPlayed() {
		return normalPlayed;
	}

	public void setNormalPlayed(int normalPlayed) {
		this.normalPlayed = normalPlayed;
	}

}
