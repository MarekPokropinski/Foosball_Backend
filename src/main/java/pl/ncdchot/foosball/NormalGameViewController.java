package pl.ncdchot.foosball;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

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

	final static int SCORE_LIMIT = 10;

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1); // scheduler for timer
	private ScheduledFuture<?> timerHandle;

	@Autowired
	NormalGameViewController(SocketHandler websock) {
		game = new NormalGameState();
		this.websock = websock;
	}

	private void sendGameWithWebsocket() {
		websock.sendMessageToAllClients(getAsWebSocketMessage(game));
	}
	
	private void finishGame() {
		game.setFinished(true);
		timerHandle.cancel(true);
	}

	@GetMapping(value = "/start")
	@ResponseBody
	public ResponseEntity<GameState> startGame() {

		game.resetScore();
		game.setFinished(false);
		game.restartTimer();

		// Send current state with websocket every 5 seconds for time sync
		timerHandle = scheduler.scheduleAtFixedRate(() -> sendGameWithWebsocket(), 5, 5, TimeUnit.SECONDS);

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

		if (game.getBlueScore() >= SCORE_LIMIT || game.getRedScore() >= SCORE_LIMIT) {
			finishGame();
		}
		sendGameWithWebsocket();

		return new ResponseEntity<>(HttpStatus.OK);
	}
}
