package pl.ncdchot.foosball;

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

	@Autowired
	protected SocketHandler websock;

	@Autowired
	private ObjectMapper mapper;

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
