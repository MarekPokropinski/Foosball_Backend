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

	@PostMapping("/goal")
	@ResponseBody
	public ResponseEntity<String> goal(@RequestBody TeamColor team) {

		if (game.isFinished()) {
			return new ResponseEntity<>("No live game", HttpStatus.BAD_REQUEST);
		}

		incrementScore(team);

		if (game.getBlueScore() >= scoreLimit || game.getRedScore() >= scoreLimit) {
			finishGame();
		}
		sendGameWithWebsocket();

		return new ResponseEntity<>("OK", HttpStatus.OK);
	}

	@GetMapping("/finish")
	@ResponseBody
	public ResponseEntity<GameStats> statistics() {
		GameStats stats = new GameStats();

		stats.setRedScore(game.getRedScore());
		stats.setBlueScore(game.getBlueScore());
		stats.setBlueLongestSeries(blueTeamLongestSeries);
		stats.setRedLongestSeries(redTeamLongestSeries);
		stats.setGameTime(game.getGameTime());

		finishGame();

		return new ResponseEntity<>(stats, HttpStatus.OK);
	}

}
