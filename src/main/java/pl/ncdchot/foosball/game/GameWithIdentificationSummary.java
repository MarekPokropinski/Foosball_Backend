package pl.ncdchot.foosball.game;

import java.util.List;

public class GameWithIdentificationSummary extends GameSummary {
	private List<Long> redTeamIds;
	private List<Long> blueTeamIds;

	public GameWithIdentificationSummary() {

	}

	public GameWithIdentificationSummary(GameSummary baseSummary, List<Long> redTeamIds, List<Long> blueTeamIds) {
		super(baseSummary);
		this.redTeamIds = redTeamIds;
		this.blueTeamIds = blueTeamIds;
	}

	public List<Long> getRedTeamIds() {
		return redTeamIds;
	}

	public void setRedTeamIds(List<Long> redTeamIds) {
		this.redTeamIds = redTeamIds;
	}

	public List<Long> getBlueTeamIds() {
		return blueTeamIds;
	}

	public void setBlueTeamIds(List<Long> blueTeamIds) {
		this.blueTeamIds = blueTeamIds;
	}
}
