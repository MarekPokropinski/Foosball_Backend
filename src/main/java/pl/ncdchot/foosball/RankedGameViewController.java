package pl.ncdchot.foosball;

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

	@Autowired
	RankedGameViewController(SocketHandler websock) {
		rankedGame = new RankedGameState();
		game = rankedGame;
		this.websock = websock;

		scoreLimit = 10;
	}

	@PostMapping(value = "/start")
	@ResponseBody
	public ResponseEntity<GameState> startGameEndpoint(@RequestBody long[] redTeamIds,
			@RequestBody long[] blueTeamIds) {

		startGame();

		playerCount = redTeamIds.length + blueTeamIds.length;

		if (redTeamIds.length != blueTeamIds.length) {
			return new ResponseEntity<>(game, HttpStatus.BAD_REQUEST);
		}

		if (playerCount != 2 && playerCount != 4) {
			return new ResponseEntity<>(game, HttpStatus.BAD_REQUEST);
		}

		rankedGame.setBlueTeamIds(blueTeamIds);
		rankedGame.setRedTeamIds(redTeamIds);

		sendGameWithWebsocket();
		return new ResponseEntity<>(game, HttpStatus.OK);
	}

	@PostMapping("/goal")
	@ResponseBody
	public ResponseEntity<HttpStatus> goal(@RequestBody TeamColor team) {

		if (game.isFinished()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		incrementScore(team);

		if (game.getBlueScore() >= scoreLimit || game.getRedScore() >= scoreLimit) {
			finishGame();
		}
		sendGameWithWebsocket();

		return new ResponseEntity<>(HttpStatus.OK);
	}
}
