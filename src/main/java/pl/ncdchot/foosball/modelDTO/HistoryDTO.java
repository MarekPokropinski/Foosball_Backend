package pl.ncdchot.foosball.modelDTO;

public class HistoryDTO {
	private String nick;
	private String normalGames;
	private String normalWinRatio;
	private String rankedSoloGames;
	private String rankedSoloWinRatio;
	private String rankedDuoGames;
	private String rankedDuoWinRatio;
	private String soloRankingPos;
	private String duoRankingPos;

	public HistoryDTO(String nick, String normalGames, String normalWinRatio, String rankedSoloGames,
			String rankedSoloWinRatio, String rankedDuoGames, String rankedDuoWinRatio, String soloRankingPos,
			String duoRankingPos) {
		this.nick = nick;
		this.normalGames = normalGames;
		this.normalWinRatio = normalWinRatio;
		this.rankedSoloGames = rankedSoloGames;
		this.rankedSoloWinRatio = rankedSoloWinRatio;
		this.rankedDuoGames = rankedDuoGames;
		this.rankedDuoWinRatio = rankedDuoWinRatio;
		this.soloRankingPos = soloRankingPos;
		this.duoRankingPos = duoRankingPos;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getNormalGames() {
		return normalGames;
	}

	public void setNormalGames(String normalGames) {
		this.normalGames = normalGames;
	}

	public String getNormalWinRatio() {
		return normalWinRatio;
	}

	public void setNormalWinRatio(String normalWinRatio) {
		this.normalWinRatio = normalWinRatio;
	}

	public String getRankedSoloGames() {
		return rankedSoloGames;
	}

	public void setRankedSoloGames(String rankedSoloGames) {
		this.rankedSoloGames = rankedSoloGames;
	}

	public String getRankedSoloWinRatio() {
		return rankedSoloWinRatio;
	}

	public void setRankedSoloWinRatio(String rankedSoloWinRatio) {
		this.rankedSoloWinRatio = rankedSoloWinRatio;
	}

	public String getRankedDuoGames() {
		return rankedDuoGames;
	}

	public void setRankedDuoGames(String rankedDuoGames) {
		this.rankedDuoGames = rankedDuoGames;
	}

	public String getRankedDuoWinRatio() {
		return rankedDuoWinRatio;
	}

	public void setRankedDuoWinRatio(String rankedDuoWinRatio) {
		this.rankedDuoWinRatio = rankedDuoWinRatio;
	}

	public String getSoloRankingPos() {
		return soloRankingPos;
	}

	public void setSoloRankingPos(String soloRankingPos) {
		this.soloRankingPos = soloRankingPos;
	}

	public String getDuoRankingPos() {
		return duoRankingPos;
	}

	public void setDuoRankingPos(String duoRankingPos) {
		this.duoRankingPos = duoRankingPos;
	}
}
