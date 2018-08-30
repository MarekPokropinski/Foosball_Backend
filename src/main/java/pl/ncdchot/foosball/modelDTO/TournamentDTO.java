package pl.ncdchot.foosball.modelDTO;

import java.util.Objects;

public class TournamentDTO {
        private String id;
        private String tournamentId;
        private String idOfFirstPlayer;
        private String idOfSecondPlayer;

    public TournamentDTO() {
    }

    public TournamentDTO(String id, String tournamentId, String idOfFirstPlayer, String idOfSecondPlayer) {
        this.id = id;
        this.tournamentId = tournamentId;
        this.idOfFirstPlayer = idOfFirstPlayer;
        this.idOfSecondPlayer = idOfSecondPlayer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(String tournamentId) {
        this.tournamentId = tournamentId;
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
        TournamentDTO that = (TournamentDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(tournamentId, that.tournamentId) &&
                Objects.equals(idOfFirstPlayer, that.idOfFirstPlayer) &&
                Objects.equals(idOfSecondPlayer, that.idOfSecondPlayer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tournamentId, idOfFirstPlayer, idOfSecondPlayer);
    }
}

