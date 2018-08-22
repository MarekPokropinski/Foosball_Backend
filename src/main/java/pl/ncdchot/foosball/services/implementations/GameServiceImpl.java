package pl.ncdchot.foosball.services.implementations;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.ncdchot.foosball.database.model.*;
import pl.ncdchot.foosball.database.repository.GameRepository;
import pl.ncdchot.foosball.exceptions.GameNotFoundException;
import pl.ncdchot.foosball.game.GameInfo;
import pl.ncdchot.foosball.game.GameSummary;
import pl.ncdchot.foosball.game.TeamColor;
import pl.ncdchot.foosball.services.GameService;
import pl.ncdchot.foosball.services.GoalService;
import pl.ncdchot.foosball.services.RulesService;
import pl.ncdchot.foosball.services.StatisticsService;
import pl.ncdchot.foosball.webSockets.SocketHandler;

import java.util.Date;
import java.util.Optional;

@Service("gameService")
public class GameServiceImpl implements GameService {

    private static final Logger LOG = Logger.getLogger(GameServiceImpl.class);

    @Autowired
    protected GameRepository gameRepository;

    @Autowired
    protected StatisticsService statsService;

    @Autowired
    protected SocketHandler websocket;

    @Autowired
    protected GoalService goalService;

    @Autowired
    protected RulesService rulesService;

    @Override
    public Optional<Game> getLiveGame() {
        return gameRepository.findByEndDate(null);
    }

    @Override
    public Game getCurrentGame(GameType gameType, Rules rules, Team redTeam, Team blueTeam) {
        return liveGameExists() ? getExistingGame() : createNewGame(gameType, rules, redTeam, blueTeam);
    }

    private boolean liveGameExists() {
        return getLiveGame().isPresent();
    }

    private Game getExistingGame() {
        Long.getLong(String.valueOf(123));
        Game liveGame = getLiveGame().get();
        LOG.info("There is live game: " + liveGame.getId());
        return liveGame;
    }

    private Game createNewGame(Rules rules) {
        Statistics stats = statsService.createEmpty();
        Game game = new Game(GameType.NORMAL, rules, stats);
        gameRepository.save(game);
        return game;
    }

    private Game createNewGame(GameType gameType, Rules rules, Team redTeam, Team blueTeam) {
        Statistics stats = statsService.createEmpty();
        Game game = new Game(gameType, rules, stats, redTeam, blueTeam);
        gameRepository.save(game);
        return game;
    }

    @Override
    public Game getCurrentGame(Rules rules) {
        return liveGameExists() ? getExistingGame() : createNewGame(rules);
    }

    private long getTimeDifference(Date startDate) {
        return new Date().getTime() - startDate.getTime();
    }

    @Override
    public void finishGame(long gameId) throws GameNotFoundException {
        Optional<Game> optGame = gameRepository.findById(gameId);
        if (optGame.isPresent()) {
            Game game = optGame.get();
            game.setEndDate(new Date());
            Statistics stats = game.getStats();
            stats.setDuration((game.getEndDate().getTime() - game.getStartDate().getTime()) / 1000);
            statsService.saveStats(stats);
            gameRepository.save(game);
            GameInfo info = getGameInfo(gameId);
            info.setFinished(true);
            websocket.sendMessageToAllClients(info);
        } else {
            throw new GameNotFoundException();
        }
    }

    private void scoreGoal(Game game, TeamColor team) {
        Statistics stats = game.getStats();
        changeScore(stats, team, 1);
        stats.getGoals().add(goalService.getNewGoal(team));
        statsService.saveStats(stats);
    }

    private void changeScore(Statistics stats, TeamColor team, int points) {
        switch (team) {
            case RED:
                stats.setRedScore(stats.getRedScore() + points);
                break;
            case BLUE:
                stats.setBlueScore(stats.getBlueScore() + points);
                break;
        }
    }

    @Override
    public void goal(long gameId, TeamColor team) throws GameNotFoundException {
        if (isLive(gameId) && rulesService.checkRules(getGame(gameId))) {
            Game game = getGame(gameId);
            scoreGoal(game, team);
            GameInfo info = getGameInfo(gameId);

            if (!rulesService.checkRules(getGame(gameId))) {
                info.setFinished(true);
                finishGame(gameId);
            } else {
                websocket.sendMessageToAllClients(info);
            }
        } else {
            throw new GameNotFoundException();
        }
    }

    @Override
    public void revertGoal(long gameId, TeamColor team) throws GameNotFoundException {
        if (isLive(gameId) && rulesService.checkRules(getGame(gameId))) {
            Game game = getGame(gameId);
            removeGoal(game, team);
            GameInfo info = getGameInfo(gameId);
            websocket.sendMessageToAllClients(info);

        } else {
            throw new GameNotFoundException();
        }
    }

    private Goal getLastGoalByTeam(Statistics stats, TeamColor team) {
        stats.getGoals().sort((a, b) -> (b.getTime().compareTo(a.getTime())));
        Goal toRemoval = null;
        for (Goal g : stats.getGoals()) {
            if (g.getTeam().equals(team)) {
                toRemoval = g;
                break;
            }
        }
        return toRemoval;
    }

    private void removeGoal(Game game, TeamColor team) {
        Statistics stats = game.getStats();
        if (stats.getTeamScore(team) > 0) {
            changeScore(stats, team, -1);
            Goal toRemoval = getLastGoalByTeam(stats, team);
            stats.getGoals().remove(toRemoval);
            statsService.saveStats(stats);
        }
    }

    private void getSeries(Statistics stats) {
        int redSeries = 0;
        int blueSeries = 0;
        int series = 0;

        TeamColor previousGoalTeam = TeamColor.RED;

        stats.getGoals().sort((a, b) -> b.getTime().compareTo(a.getTime()));

        for (Goal goal : stats.getGoals()) {
            if (previousGoalTeam.equals(goal.getTeam())) {
                series++;
            } else {
                series = 1;
            }
            if (goal.getTeam().equals(TeamColor.RED)) {
                redSeries = Math.max(redSeries, series);
            } else {
                blueSeries = Math.max(blueSeries, series);
            }
            previousGoalTeam = goal.getTeam();
        }
        stats.setRedSeries(redSeries);
        stats.setBlueSeries(blueSeries);
        statsService.saveStats(stats);
    }

    @Override
    public GameSummary getSummary(long gameId) throws GameNotFoundException {
        Optional<Game> optGame = gameRepository.findById(gameId);
        Game game = optGame.orElseThrow(GameNotFoundException::new);
        Statistics stats = game.getStats();
        getSeries(stats);
        return new GameSummary(stats.getRedScore(), stats.getBlueScore(),
                game.getEndDate().getTime() - game.getStartDate().getTime(), stats.getRedSeries(),
                stats.getBlueSeries());
    }

    @Override
    public int getBlueScore(long gameId) throws GameNotFoundException {
        Optional<Game> optGame = gameRepository.findById(gameId);
        if (optGame.isPresent()) {
            return optGame.get().getStats().getBlueScore();
        } else {
            throw new GameNotFoundException();
        }
    }

    @Override
    public int getRedScore(long gameId) throws GameNotFoundException {
        Optional<Game> optGame = gameRepository.findById(gameId);
        if (optGame.isPresent()) {
            return optGame.get().getStats().getRedScore();
        } else {
            throw new GameNotFoundException();
        }
    }

    @Override
    public Game getGame(long gameId) throws GameNotFoundException {
        Optional<Game> optGame = gameRepository.findById(gameId);
        if (optGame.isPresent()) {
            return optGame.get();
        } else {
            throw new GameNotFoundException();
        }
    }

    @Override
    public boolean isLive(long gameId) {
        Optional<Game> optGame = gameRepository.findById(gameId);
        if (optGame.isPresent()) {
            if (optGame.get().getEndDate() == null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Statistics createNewStats() {
        return statsService.createEmpty();
    }

    @Override
    public GameInfo getGameInfo(long gameId) throws GameNotFoundException {

        boolean isGameFinished = false;
        Game game = getGame(gameId);
        if (game.getEndDate() != null) {
            isGameFinished = true;
        }
        Statistics stats = game.getStats();
        GameInfo info = new GameInfo(game.getId(), stats.getRedScore(), stats.getBlueScore(),
                getTimeDifference(game.getStartDate()), isGameFinished);

        return info;
    }

}
