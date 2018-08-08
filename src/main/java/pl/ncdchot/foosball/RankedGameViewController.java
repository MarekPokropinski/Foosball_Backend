package pl.ncdchot.foosball;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.ncdchot.foosball.webSockets.SocketHandler;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping(path = "/rankedGame")
public class RankedGameViewController extends GameViewController {

	private int playerCount;
	private RankedGameState rankedGame;

	static final private long timeLimit = 3; // Time limit in minutes

	@Autowired
	RankedGameViewController(SocketHandler websock) {
		rankedGame = new RankedGameState();
		game = rankedGame;
		this.websock = websock;

		scoreLimit = 10;
	}

	@PostMapping(value = "/start")
	@ResponseBody
	public ResponseEntity<GameState> startGameEndpoint(@RequestBody long[] playersIds) {

		startGame();

		playerCount = playersIds.length;

		if (playerCount == 2) {
			rankedGame.setBlueTeamIds(new long[] { playersIds[0] });
			rankedGame.setRedTeamIds(new long[] { playersIds[1] });
		} else if (playerCount == 4) {
			rankedGame.setBlueTeamIds(new long[] { playersIds[0], playersIds[1] });
			rankedGame.setRedTeamIds(new long[] { playersIds[2], playersIds[3] });
		} else {
			return new ResponseEntity<>(game, HttpStatus.BAD_REQUEST);
		}

		scheduler.schedule(() -> this.finishGame(), timeLimit, TimeUnit.MINUTES); // Schedule game finish after given time

		sendGameWithWebsocket();
		return new ResponseEntity<>(game, HttpStatus.OK);
	}

	@PostMapping("/goal")
	@ResponseBody
	public ResponseEntity<String> goal(@RequestBody TeamColor team) {

		if (game.isFinished()) {
			return new ResponseEntity<>("Game is already finished",HttpStatus.BAD_REQUEST);
		}

		incrementScore(team);

		if (game.getBlueScore() >= scoreLimit || game.getRedScore() >= scoreLimit) {
			finishGame();
		}
		sendGameWithWebsocket();

		return new ResponseEntity<>("OK",HttpStatus.OK);
	}
}
