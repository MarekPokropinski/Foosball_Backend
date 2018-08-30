package pl.ncdchot.foosball.modelDTO;

import java.util.Objects;

public class PrepareToStartGameInTournamentDTO {
    private String gameType;
    private String idOfFirstPlayer;
    private String idOfSecondPlayer;

    public PrepareToStartGameInTournamentDTO() {
    }

    public PrepareToStartGameInTournamentDTO(String gameType, String idOfFirstPlayer, String idOfSecondPlayer) {
        this.gameType = gameType;
        this.idOfFirstPlayer = idOfFirstPlayer;
        this.idOfSecondPlayer = idOfSecondPlayer;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String getIdOfFirstPlayer() {
        return idOfFirstPlayer;
    }

    public void setIdOfFirstPlayer(String idOfFirstPlayer) {
        this.idOfFirstPlayer = idOfFirstPlayer;
    }

    public String getIdOfSecondPlayer() {
        return idOfSecondPlayer;
    }

    public void setIdOfSecondPlayer(String idOfSecondPlayer) {
        this.idOfSecondPlayer = idOfSecondPlayer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrepareToStartGameInTournamentDTO that = (PrepareToStartGameInTournamentDTO) o;
        return Objects.equals(gameType, that.gameType) &&
                Objects.equals(idOfFirstPlayer, that.idOfFirstPlayer) &&
                Objects.equals(idOfSecondPlayer, that.idOfSecondPlayer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameType, idOfFirstPlayer, idOfSecondPlayer);
    }
}
