package pl.ncdchot.foosball.controllers;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.ncdchot.foosball.database.model.Rules;
import pl.ncdchot.foosball.exceptions.GameNotFoundException;
import pl.ncdchot.foosball.exceptions.UserNotExistException;
import pl.ncdchot.foosball.game.GameSummary;
import pl.ncdchot.foosball.game.TeamColor;
import pl.ncdchot.foosball.services.GameWithHistoryService;

@RestController
@RequestMapping("/rankedGame")
public class RankedGameController {
    private static final Logger LOG = Logger.getLogger(RankedGameController.class);

    @Qualifier("rankedGameServiceImpl")
    @Autowired
    private GameWithHistoryService service;

    @GetMapping("/start")
    public ResponseEntity<String> startGame(
            @RequestParam long[] redTeamUsersID,
            @RequestParam long[] blueTeamUsersID) {


        if (!isGameCorrect(redTeamUsersID, blueTeamUsersID)) {
            return ResponseEntity.badRequest().body("The number of players is not correct");
        }
        try {
            long id = service.startGame(redTeamUsersID, blueTeamUsersID, new Rules());
            LOG.info("Starting Ranked game id: " + id);
            return new ResponseEntity<>(String.valueOf(id), HttpStatus.OK);
        } catch (UserNotExistException userNotExistException) {
            LOG.warn(userNotExistException.getMessage());
            return ResponseEntity.badRequest().body("User not exist");
        }
    }

    private boolean isGameCorrect(long[] redTeam, long[] blueTeam) {
        return redTeam.length == blueTeam.length;
    }

    @GetMapping("/goal")
    public ResponseEntity<?> goalEndpoint(@RequestParam TeamColor team, @RequestParam long gameId) {
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
