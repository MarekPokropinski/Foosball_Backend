package pl.ncdchot.foosball;

public class RankedGameStats extends GameStats {
	String[] redTeamNames;
	String[] blueTeamNames;
	
	public String[] getRedTeamNames() {
		return redTeamNames;
	}
	public void setRedTeamNames(String[] redTeamNames) {
		this.redTeamNames = redTeamNames;
	}
	public String[] getBlueTeamNames() {
		return blueTeamNames;
	}
	public void setBlueTeamNames(String[] blueTeamNames) {
		this.blueTeamNames = blueTeamNames;
	}	
}
