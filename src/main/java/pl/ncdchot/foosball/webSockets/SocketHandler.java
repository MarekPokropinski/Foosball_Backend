package pl.ncdchot.foosball.webSockets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class SocketHandler extends TextWebSocketHandler {

	private List<WebSocketSession> sessionList = new ArrayList<>();

	public void sendMessageToAllClients(WebSocketMessage<String> message) {
		
		List<WebSocketSession> toRemove = new ArrayList<>();
		
		for (WebSocketSession client : sessionList) {
			try {
				client.sendMessage(message);
			} catch (IOException e) {
				//TODO: Log this
				System.out.println("Failed to send message");
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
}