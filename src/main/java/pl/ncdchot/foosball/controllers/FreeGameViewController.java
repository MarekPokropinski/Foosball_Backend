package pl.ncdchot.foosball.controllers;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pl.ncdchot.foosball.exceptions.GameNotFoundException;
import pl.ncdchot.foosball.game.GameSummary;
import pl.ncdchot.foosball.game.TeamColor;
import pl.ncdchot.foosball.services.FreeGameService;

@RestController
@RequestMapping(path = "/freeGame")
public class FreeGameViewController {

	private static final Logger LOG = Logger.getLogger(FreeGameViewController.class);

	private FreeGameService service;

	@Autowired
	FreeGameViewController(FreeGameService service) {
		this.service = service;
	}

	@GetMapping(value = "/start")
	public ResponseEntity<Long> startGameEndpoint() {
		LOG.info("Starting game");
		long id = service.startGame();
		return new ResponseEntity<>(id, HttpStatus.OK);
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
