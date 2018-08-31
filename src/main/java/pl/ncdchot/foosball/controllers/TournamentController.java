package pl.ncdchot.foosball.controllers;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.ncdchot.foosball.database.model.Rules;
import pl.ncdchot.foosball.exceptions.GameNotFoundException;
import pl.ncdchot.foosball.exceptions.TeamNoExistException;
import pl.ncdchot.foosball.exceptions.TournamentGameDontExistsException;
import pl.ncdchot.foosball.exceptions.UserNotExistException;
import pl.ncdchot.foosball.game.GameSummary;
import pl.ncdchot.foosball.game.TeamColor;
import pl.ncdchot.foosball.services.implementations.TournamentServiceImpl;

@RestController
@RequestMapping("/tournamentGame")
public class TournamentController {
    private static final Logger LOG = Logger.getLogger(TournamentController.class);

    @Autowired
    private TournamentServiceImpl service;

    @GetMapping("/start")
    public ResponseEntity<Long> startGame(
            @RequestParam long[] redTeamUsersID,
            @RequestParam long[] blueTeamUsersID) throws UserNotExistException, TeamNoExistException, GameNotFoundException, TournamentGameDontExistsException {

        long id = service.startGame(redTeamUsersID, blueTeamUsersID, new Rules());
        LOG.info("Starting tournament game id: " + id);
        return ResponseEntity.ok(id);
    }

    @GetMapping("/goal")
    public ResponseEntity<Void> goalEndpoint(@RequestParam TeamColor team, @RequestParam long gameId) {
        LOG.info(String.format("Goal for game id: %s for team: %s", gameId, team));
        try {
            service.goal(gameId, team);
        } catch (GameNotFoundException e) {
            LOG.warn(String.format("Tried to score goal in game that was not live. Id: %s", gameId));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/finish")
    public ResponseEntity<GameSummary> finishGameEndpoint(@RequestParam long gameId) {
        LOG.info(String.format("Ended game with id: %s", gameId));
        try {
            service.finishGame(gameId);
        } catch (GameNotFoundException e) {
            LOG.warn(String.format("Tried to finish game that was not live. Id: %s", gameId));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            return new ResponseEntity<>(service.getSummary(gameId), HttpStatus.OK);
        } catch (GameNotFoundException e) {
            LOG.warn(String.format("Tried to get summary of game that was not live. Id: %s", gameId));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
