package pl.ncdchot.foosball.webSockets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import pl.ncdchot.foosball.services.implementations.GameServiceImpl;

public class SocketHandler extends TextWebSocketHandler {
	private List<WebSocketSession> sessionList = new ArrayList<>();
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private static final Logger LOG = Logger.getLogger(GameServiceImpl.class);

	public void sendMessageToAllClients(Object o) {
		WebSocketMessage<String> message = getAsWebSocketMessage(o);
		List<WebSocketSession> toRemove = new ArrayList<>();
		for (WebSocketSession client : sessionList) {
			try {
				client.sendMessage(message);
			} catch (IOException e) {
				LOG.warn("Failed to send message");
				toRemove.add(client);
			}
		}
		for (WebSocketSession client : toRemove) {
			sessionList.remove(client);
		}
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) {
		sessionList.add(session);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
		sessionList.remove(session);
	}

	private WebSocketMessage<String> getAsWebSocketMessage(Object o) {
		WebSocketMessage<String> obj = null;
		try {
			obj = new TextMessage(MAPPER.writeValueAsString(o));
		} catch (JsonProcessingException e) {
			LOG.warn("Coundn't parse object to JSON");
		}
		return obj;
	}
}