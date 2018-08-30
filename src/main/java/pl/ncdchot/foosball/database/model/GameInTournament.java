package pl.ncdchot.foosball.database.model;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class GameInTournament extends BaseModel{
    @OneToOne
    private Game game;
    private String tournamentGameID;

    public GameInTournament() {
    }

    public GameInTournament(Game game, String tournamentGameID) {
        this.game = game;
        this.tournamentGameID = tournamentGameID;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getTournamentGameID() {
        return tournamentGameID;
    }

    public void setTournamentGameID(String tournamentGameID) {
        this.tournamentGameID = tournamentGameID;
    }

}
