package pl.ncdchot.foosball.controllers;

import java.util.Optional;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pl.ncdchot.foosball.database.model.Game;
import pl.ncdchot.foosball.exceptions.GameNotFoundException;
import pl.ncdchot.foosball.game.TeamColor;
import pl.ncdchot.foosball.services.GameService;

@RestController
public class GoalController {

	private static final Logger LOG = Logger.getLogger(GoalController.class);

	@Autowired
	@Qualifier("gameService")
	private GameService service;

	@PostMapping("/goal")
	public ResponseEntity<Void> goalEndpoint(@RequestParam TeamColor team) {
		LOG.info(String.format("Goal for game for team: %s", team));
		Optional<Game> game = service.getLiveGame();
		if (!game.isPresent()) {
			return new ResponseEntity<>(HttpStatus.ACCEPTED);
		}
		try {
			service.goal(game.get().getId(), team);
		} catch (GameNotFoundException e) {
			LOG.warn(String.format("Tried to score goal in game that was not live. Id: %s", game.get().getId()));
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/revertGoal")
	public ResponseEntity<Void> revertGoalEndpoint(@RequestParam TeamColor team) {
		LOG.info(String.format("Revert goal for team: %s", team));
		Optional<Game> game = service.getLiveGame();
		if (!game.isPresent()) {
			return new ResponseEntity<>(HttpStatus.ACCEPTED);
		}
		try {
			service.revertGoal(game.get().getId(), team);
		} catch (GameNotFoundException e) {
			LOG.warn(String.format("Tried to revert goal in game that was not live. Id: %s", game.get().getId()));
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
