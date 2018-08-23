package pl.ncdchot.foosball.controllers;

import java.time.Duration;

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

@RestController
@RequestMapping("/normalGame")
public class NormalGameController {
	private static final Logger LOG = Logger.getLogger(NormalGameController.class);
	private static final int SOLO_GAME_SIZE = 2;
	private static final int DUO_GAME_SIZE = 4;

	@Qualifier("normalGameServiceImpl")
	@Autowired
	private GameWithHistoryService service;

	@GetMapping("/start")
	public ResponseEntity<String> startGame(@RequestParam long[] usersID, @RequestParam int maxGoal,
			@RequestParam int maxTimeInSec) {

		long id;
		if (!isGameCorrect(usersID)) {
			return ResponseEntity.badRequest().body("The number of players is not correct");
		}
		try {
			id = service.startGame(usersID, new Rules(maxGoal, Duration.ofSeconds(maxTimeInSec)));
		} catch (UserNotExist userNotExist) {
			LOG.warn(userNotExist.getMessage());
			return ResponseEntity.badRequest().body("User not exist");
		}
		LOG.info("Starting Normal game id: " + id);
		return new ResponseEntity<>(String.valueOf(id), HttpStatus.OK);
	}

	private boolean isGameCorrect(long[] usersID) {
		return usersID.length == SOLO_GAME_SIZE || usersID.length == DUO_GAME_SIZE;
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
