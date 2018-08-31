package pl.ncdchot.foosball.services.implementations;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.ncdchot.foosball.database.model.Game;
import pl.ncdchot.foosball.database.model.GameInTournament;
import pl.ncdchot.foosball.database.model.GameType;
import pl.ncdchot.foosball.database.model.Rules;
import pl.ncdchot.foosball.exceptions.GameNotFoundException;
import pl.ncdchot.foosball.exceptions.TeamNoExistException;
import pl.ncdchot.foosball.exceptions.UserNotExistException;
import pl.ncdchot.foosball.game.GameSummary;
import pl.ncdchot.foosball.modelDTO.FinishTournamentGameDTO;
import pl.ncdchot.foosball.modelDTO.CheckTournamentDTO;
import pl.ncdchot.foosball.modelDTO.GameTournamentDTO;
import pl.ncdchot.foosball.services.ManagementSystemService;

import java.time.LocalDateTime;

@Service
public class TournamentServiceImpl extends GameWithHistoryServiceImpl {
    private static final Logger LOG = Logger.getLogger(TournamentServiceImpl.class);
    private static final String TOURNAMENT_GAME_NAME = "FOOSBALL";

    private CheckTournamentDTO checkTournamentDTO;

    @Autowired
    private TournamentSystemServiceImpl tournamentSystemServiceImpl;
    @Autowired
    private ManagementSystemService managementSystemService;
    @Autowired
    private GameInTournamentService gameInTournamentService;

    @Override
    public long startGame(long[] redTeamUsers, long[] blueTeamUsers, Rules rules) throws UserNotExistException, TeamNoExistException, GameNotFoundException {
        GameTournamentDTO gameTournamentDTO = getTournamentGame(redTeamUsers, blueTeamUsers);
        long gameID = startFoosballGame(redTeamUsers, blueTeamUsers);
        Game game = getGame(gameID);
        createTournamentGame(gameTournamentDTO,game);

        return gameID;
    }

    private GameTournamentDTO getTournamentGame(long[] redTeamUsers, long[] blueTeamUsers) throws TeamNoExistException {
        checkTournamentDTO = createTournamentGame(redTeamUsers, blueTeamUsers);
        return tournamentSystemServiceImpl.findGameInTournament(checkTournamentDTO);
    }

    private long startFoosballGame(long[] redTeamUsers, long[] blueTeamUsers) throws UserNotExistException {
        return super.startGame(redTeamUsers, blueTeamUsers, RankedGameServiceImpl.RANKED_GAME_RULES, GameType.TOURNAMENT);
    }

    private void createTournamentGame(GameTournamentDTO gameTournamentDTO,  Game game) {
        if (!checkTournamentDTO.getIdOfFirstPlayer().equals(gameTournamentDTO.getIdOfFirstPlayer())) {
            game.getBlueTeam().setExternalID(Long.valueOf(gameTournamentDTO.getIdOfFirstPlayer()));
            game.getRedTeam().setExternalID(Long.valueOf(gameTournamentDTO.getIdOfSecondPlayer()));
        }else {
            game.getBlueTeam().setExternalID(Long.valueOf(gameTournamentDTO.getIdOfSecondPlayer()));
            game.getRedTeam().setExternalID(Long.valueOf(gameTournamentDTO.getIdOfFirstPlayer()));
        }
        GameInTournament gameInTournament = new GameInTournament(game, gameTournamentDTO.getId());
        gameInTournamentService.save(gameInTournament);
    }

    private CheckTournamentDTO createTournamentGame(long[] redTeam, long[] blueTeam) throws TeamNoExistException {
        String red = managementSystemService.getTournamentTeamID(redTeam);
        String blue = managementSystemService.getTournamentTeamID(blueTeam);
        return new CheckTournamentDTO(TOURNAMENT_GAME_NAME, red, blue);
    }

    @Override
    public void finishGame(long gameId) throws GameNotFoundException {
        super.finishGame(gameId);
        LOG.info(LocalDateTime.now() + " : Tournament game is finished");
        FinishTournamentGameDTO finishTournamentGameDTO = createFinishedGame(gameId);
        tournamentSystemServiceImpl.sandResultToTournament(finishTournamentGameDTO);
        LOG.info(LocalDateTime.now() + " : Sand info to tournament system");
        LOG.info(LocalDateTime.now() + finishTournamentGameDTO.toString());
    }


    private FinishTournamentGameDTO createFinishedGame(long gameId) throws GameNotFoundException {
        Game game = getGame(gameId);
        String statsID = getStatsID(game);
        String tournamentGameID = getTournamentGameID(gameId);
        String winnerID = getWinner(game);

        return new FinishTournamentGameDTO(tournamentGameID, statsID, winnerID);
    }

    private String getTournamentGameID(long gameId) {
        GameInTournament tournamentGame = gameInTournamentService.getTournamentGameByNormalID(gameId);
        return tournamentGame.getTournamentGameID();
    }

    private String getStatsID(Game game) {
        return String.valueOf(game.getStats().getId());
    }

    private String getWinner(Game game) throws GameNotFoundException {
        long winnerID;
        GameSummary gameSummary = getSummary(game.getId());
        if (gameSummary.getBlueScore() > gameSummary.getRedScore()) {
            winnerID = game.getBlueTeam().getExternalID();
        } else if (gameSummary.getBlueScore() < gameSummary.getRedScore()) {
            winnerID = game.getRedTeam().getExternalID();
        } else {
            winnerID = 0;
        }
        return String.valueOf(winnerID);
    }
}
