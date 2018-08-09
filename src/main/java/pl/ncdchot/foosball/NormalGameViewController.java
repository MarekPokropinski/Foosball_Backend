package pl.ncdchot.foosball;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.ncdchot.foosball.webSockets.SocketHandler;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping(path = "/normalGame")
public class NormalGameViewController extends GameViewController {

	@Autowired
	NormalGameViewController(SocketHandler websock) {
		game = new NormalGameState();
		this.websock = websock;

		scoreLimit = 10;
	}

	@GetMapping(value = "/start")
	@ResponseBody
	public ResponseEntity<GameState> startGameEndpoint() {

		startGame();

		sendGameWithWebsocket();
		return new ResponseEntity<>(game, HttpStatus.OK);
	}

	@Override
	@PostMapping("/goal")
	@ResponseBody
	public ResponseEntity<String> goalEndpoint(@RequestBody TeamColor team) {

		if (game == null) {
			return new ResponseEntity<>("You must start game first", HttpStatus.BAD_REQUEST);
		}
		if (game.isFinished()) {
			return new ResponseEntity<>("Game is already finished", HttpStatus.BAD_REQUEST);
		}

		incrementScore(team);

		sendGameWithWebsocket();

		return new ResponseEntity<>("OK", HttpStatus.OK);
	}

	@Override
	@GetMapping("/finish")
	@ResponseBody
	public ResponseEntity<GameStats> finishGameEndpoint() {
		GameStats stats = new GameStats();

		getGameStats(stats);

		finishGame();

		return new ResponseEntity<>(stats, HttpStatus.OK);
	}

}
