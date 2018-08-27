package pl.ncdchot.foosball.modelDTO;

import pl.ncdchot.foosball.database.model.UserHistory;

public class HistoryDTO {
	String nick;
	private int normalGames;
	private float normalWinRatio;
	private int rankedSoloGames;
	private float rankedSoloWinRatio;
	private int rankedDuoGames;
	private float rankedDuoWinRatio;
	private int soloRankingPos;
	private int duoRankingPos;

	public HistoryDTO(String nick, int normalGames, float normalWinRatio, int rankedSoloGames, float rankedSoloWinRatio,
			int rankedDuoGames, float rankedDuoWinRatio, int soloRankingPos, int duoRankingPos) {
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

	public HistoryDTO(UserHistory history, String nick) {
		this(nick, history.getNormalPlayed(), (float) history.getNormalWins() / history.getNormalPlayed(),
				history.getRankedSoloPlayed(), (float) history.getRankedSoloWins() / history.getRankedSoloPlayed(),
				history.getRankedDuoPlayed(), (float) history.getRankedDuoWins() / history.getRankedDuoPlayed(), 0, 0);
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public int getNormalGames() {
		return normalGames;
	}

	public void setNormalGames(int normalGames) {
		this.normalGames = normalGames;
	}

	public float getNormalWinRatio() {
		return normalWinRatio;
	}

	public void setNormalWinRatio(float normalWinRatio) {
		this.normalWinRatio = normalWinRatio;
	}

	public int getRankedSoloGames() {
		return rankedSoloGames;
	}

	public void setRankedSoloGames(int rankedSoloGames) {
		this.rankedSoloGames = rankedSoloGames;
	}

	public float getRankedSoloWinRatio() {
		return rankedSoloWinRatio;
	}

	public void setRankedSoloWinRatio(float rankedSoloWinRatio) {
		this.rankedSoloWinRatio = rankedSoloWinRatio;
	}

	public int getRankedDuoGames() {
		return rankedDuoGames;
	}

	public void setRankedDuoGames(int rankedDuoGames) {
		this.rankedDuoGames = rankedDuoGames;
	}

	public float getRankedDuoWinRatio() {
		return rankedDuoWinRatio;
	}

	public void setRankedDuoWinRatio(float rankedDuoWinRatio) {
		this.rankedDuoWinRatio = rankedDuoWinRatio;
	}

	public int getSoloRankingPos() {
		return soloRankingPos;
	}

	public void setSoloRankingPos(int soloRankingPos) {
		this.soloRankingPos = soloRankingPos;
	}

	public int getDuoRankingPos() {
		return duoRankingPos;
	}

	public void setDuoRankingPos(int duoRankingPos) {
		this.duoRankingPos = duoRankingPos;
	}
}
