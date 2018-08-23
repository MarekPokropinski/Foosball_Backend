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
import pl.ncdchot.foosball.exceptions.UserNotExist;
import pl.ncdchot.foosball.game.GameSummary;
import pl.ncdchot.foosball.game.TeamColor;
import pl.ncdchot.foosball.services.GameWithHistoryService;
import pl.ncdchot.foosball.services.implementations.RankedGameServiceImpl;

import java.time.Duration;

@RestController
@RequestMapping("/normalGame")
public class NormalGameController {
    private static final Logger LOG = Logger.getLogger(NormalGameController.class);

    @Qualifier("normalGameServiceImpl")
    @Autowired
    private GameWithHistoryService service;

    @GetMapping("/start")
    public ResponseEntity<String> startGame(
            @RequestParam long[] redTeamUsersID,
            @RequestParam long[] blueTeamUsersID,
            @RequestParam(required = false) Integer maxGoal,
            @RequestParam(required = false) Integer maxTimeInSec) {

        Rules gameRules = getRules(maxGoal, maxTimeInSec);
        try {
            long id = service.startGame(redTeamUsersID, blueTeamUsersID, gameRules);
            LOG.info("Starting Normal game id: " + id);
            return new ResponseEntity<>(String.valueOf(id), HttpStatus.OK);
        } catch (UserNotExist userNotExist) {
            LOG.warn(userNotExist.getMessage());
            return ResponseEntity.badRequest().body("User not exist");
        }
    }

    private Rules getRules(@RequestParam(required = false) Integer maxGoal,
                           @RequestParam(required = false) Integer maxTimeInSec) {
        int goalLimit = getMaxGoalNumber(maxGoal, RankedGameServiceImpl.RANKED_GOALS_LIMIT);
        int timeLimit = getMaxGoalNumber(maxTimeInSec, RankedGameServiceImpl.RANKED_TIME_IN_SEC_LIMIT);

        Duration duration = Duration.ofSeconds(timeLimit);
        return new Rules(goalLimit, duration);
    }

    private int getMaxGoalNumber(Integer limitFromUser, int standardValue) {
        return limitFromUser == null ? standardValue : limitFromUser;
    }

    @GetMapping("/goal")
    public ResponseEntity<String> goalEndpoint(@RequestParam TeamColor team, @RequestParam long gameId) {
        LOG.info(String.format("Goal for game id: %s for team: %s", gameId, team));
        try {
            service.goal(gameId, team);
        } catch (GameNotFoundException e) {
            LOG.warn(String.format("Tried to score goal in game that was not live. Id: %s", gameId));
            return ResponseEntity.badRequest().body("This game it's not live");
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
