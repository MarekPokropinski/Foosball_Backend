package pl.ncdchot.foosball;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import pl.ncdchot.foosball.webSockets.SocketHandler;

@Controller
public abstract class GameViewController {

	protected GameState game;

	protected int scoreLimit;

	@Autowired
	protected SocketHandler websock;

	@Autowired
	private ObjectMapper mapper;

	protected final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2); // scheduler for timers
	protected ScheduledFuture<?> timerHandle;

	protected void startGame() {
		game.resetScore();
		game.setFinished(false);
		game.restartTimer();

		// Send current state with websocket every 5 seconds for time sync
		if (timerHandle != null && !timerHandle.isCancelled()) {
			timerHandle.cancel(true);
		}
		timerHandle = scheduler.scheduleAtFixedRate(() -> sendGameWithWebsocket(), 5, 5, TimeUnit.SECONDS);
	}

	protected void finishGame() {
		game.setFinished(true);
		sendGameWithWebsocket();
		timerHandle.cancel(true);
	}

	protected void sendGameWithWebsocket() {
		websock.sendMessageToAllClients(getAsWebSocketMessage(game));
	}

	protected void incrementScore(TeamColor team) {
		if (team.equals(TeamColor.RED)) {
			game.incrementRed();
		} else {
			game.incrementBlue();
		}
	}

	protected WebSocketMessage<String> getAsWebSocketMessage(Object o) {

		WebSocketMessage<String> obj = null;
		try {
			obj = new TextMessage(mapper.writeValueAsString(o));
		} catch (JsonProcessingException e) {
			System.out.println("Coundn't parse objest to JSON");
		}
		return obj;
	}
}
