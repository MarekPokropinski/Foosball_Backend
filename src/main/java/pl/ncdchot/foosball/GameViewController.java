package pl.ncdchot.foosball;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
	// Statistics
	protected int redTeamLongestSeries;
	protected int blueTeamLongestSeries;
	private int currentSeries;
	private TeamColor lastGoalBy;
	List<Goal> goals;

	@Autowired
	protected SocketHandler websock;

	@Autowired
	private ObjectMapper mapper;

	protected final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1); // scheduler for timers
	protected ScheduledFuture<?> timerHandle;

	public abstract ResponseEntity<String> goalEndpoint(TeamColor team);

	public abstract ResponseEntity<GameStats> finishGameEndpoint();

	protected void startGame() {
		game.resetScore();
		game.setFinished(false);
		game.restartTimer();

		redTeamLongestSeries = 0;
		blueTeamLongestSeries = 0;
		currentSeries = 0;
		lastGoalBy = TeamColor.RED;
		goals = new ArrayList<>();

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

		if (team == lastGoalBy) {
			currentSeries++;
		} else {
			currentSeries = 1;
		}

		if (team.equals(TeamColor.BLUE)) {
			blueTeamLongestSeries = Math.max(currentSeries, blueTeamLongestSeries);
		} else {
			redTeamLongestSeries = Math.max(currentSeries, redTeamLongestSeries);
		}

		lastGoalBy = team;

		goals.add(new Goal(game.getGameTime(), team));

		if (checkScoreLimit()) {
			finishGame();
		}

	}

	private boolean checkScoreLimit() {
		if (game.getBlueScore() >= scoreLimit || game.getRedScore() >= scoreLimit) {
			return true;
		}
		return false;
	}

	void getGameStats(GameStats stats) {
		stats.setRedScore(game.getRedScore());
		stats.setBlueScore(game.getBlueScore());
		stats.setBlueLongestSeries(blueTeamLongestSeries);
		stats.setRedLongestSeries(redTeamLongestSeries);
		stats.setGameTime(game.getGameTime());
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
