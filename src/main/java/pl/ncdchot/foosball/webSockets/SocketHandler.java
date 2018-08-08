package pl.ncdchot.foosball.webSockets;

import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class SocketHandler extends TextWebSocketHandler {

	private List<WebSocketSession> sessionList = new ArrayList<>();

	public void sendMessageToAllClients(WebSocketMessage<String> message) {
		try {
			for (WebSocketSession client : sessionList) {
				client.sendMessage(message);
			}
		} catch (IOException e) {
			//TODO: Log this
			System.out.println("Failed to send message");
		}
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) {
		sessionList.add(session);
	}
}