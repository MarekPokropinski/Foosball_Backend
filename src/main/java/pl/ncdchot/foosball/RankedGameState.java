package pl.ncdchot.foosball;

public class RankedGameState extends GameState {
	long[] blueTeamIds;
	long[] redTeamIds;
	
	public long[] getBlueTeamIds() {
		return blueTeamIds;
	}
	public void setBlueTeamIds(long[] blueTeamIds) {
		this.blueTeamIds = blueTeamIds;
	}
	public long[] getRedTeamIds() {
		return redTeamIds;
	}
	public void setRedTeamIds(long[] redTeamIds) {
		this.redTeamIds = redTeamIds;
	}
	
}
