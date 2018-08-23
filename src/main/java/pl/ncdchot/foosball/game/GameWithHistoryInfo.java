package pl.ncdchot.foosball.game;

public class GameWithHistoryInfo extends GameInfo {
	private long timeLimitMilis;
	private Long[] redTeamIds;
	private Long[] blueTeamIds;

	public GameWithHistoryInfo() {

	}

	public GameWithHistoryInfo(long id, int redScore, int blueScore, long time, boolean finished, long timeLimitMilis,
			Long[] redTeamIds, Long[] blueTeamIds) {
		super(id, redScore, blueScore, time, finished);
		this.timeLimitMilis = timeLimitMilis;
		this.redTeamIds = redTeamIds;
		this.blueTeamIds = blueTeamIds;
	}

	public GameWithHistoryInfo(GameInfo info, long timeLimitMilis, Long[] redTeamIds, Long[] blueTeamIds) {
		super(info);
		this.timeLimitMilis = timeLimitMilis;
		this.redTeamIds = redTeamIds;
		this.blueTeamIds = blueTeamIds;
	}

	public long getTimeLimitMilis() {
		return timeLimitMilis;
	}

	public void setTimeLimitMilis(long timeLimitMilis) {
		this.timeLimitMilis = timeLimitMilis;
	}

	public Long[] getRedTeamIds() {
		return redTeamIds;
	}

	public void setRedTeamIds(Long[] redTeamIds) {
		this.redTeamIds = redTeamIds;
	}

	public Long[] getBlueTeamIds() {
		return blueTeamIds;
	}

	public void setBlueTeamIds(Long[] blueTeamIds) {
		this.blueTeamIds = blueTeamIds;
	}
}
