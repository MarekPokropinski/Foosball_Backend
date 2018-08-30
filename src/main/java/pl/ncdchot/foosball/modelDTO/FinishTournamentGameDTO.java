package pl.ncdchot.foosball.modelDTO;

public class FinishTournamentGameDTO {
    private String idGame;
    private String idStatistics;
    private String idWinner;


    public FinishTournamentGameDTO(String idGame, String idStatistics, String idWinner) {
        this.idGame = idGame;
        this.idStatistics = idStatistics;
        this.idWinner = idWinner;
    }

    public String getIdGame() {
        return idGame;
    }

    public void setIdGame(String idGame) {
        this.idGame = idGame;
    }

    public String getIdStatistics() {
        return idStatistics;
    }

    public void setIdStatistics(String idStatistics) {
        this.idStatistics = idStatistics;
    }

    public String getIdWinner() {
        return idWinner;
    }

    public void setIdWinner(String idWinner) {
        this.idWinner = idWinner;
    }

    @Override
    public String toString() {
        return "FinishTournamentGameDTO{" +
                "idGame='" + idGame + '\'' +
                ", idStatistics='" + idStatistics + '\'' +
                ", idWinner='" + idWinner + '\'' +
                '}';
    }
}
