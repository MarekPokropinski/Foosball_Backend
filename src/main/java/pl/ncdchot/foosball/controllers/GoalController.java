package pl.ncdchot.foosball.controllers;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import pl.ncdchot.foosball.database.model.Game;
import pl.ncdchot.foosball.exceptions.GameNotFoundException;
import pl.ncdchot.foosball.game.TeamColor;
import pl.ncdchot.foosball.services.GameService;

@RestController
public class GoalController {

	private static final Logger LOG = Logger.getLogger(FreeGameViewController.class);

	@Autowired
	@Qualifier("gameService")
	private GameService service;

	@PostMapping("/goal")
	public ResponseEntity<?> goalEndpoint(@RequestParam TeamColor team) {
		LOG.info(String.format("Goal for game for team: %s", team));
		Optional<Game> game = service.getLiveGame();
		if (!game.isPresent()) {
			return new ResponseEntity<>(HttpStatus.ACCEPTED);
		}
		try {
			service.goal(game.get().getId(), team);
		} catch (GameNotFoundException e) {
			LOG.warn(String.format("Tried to score goal in game that was not live. Id: %s" , game.get().getId()));
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
