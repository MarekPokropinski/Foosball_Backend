package pl.ncdchot.foosball.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "User with this card id not exist")
public class UserByCardIDNotExistException extends Exception {
	private static final long serialVersionUID = -1915358376439711344L;
	private String cardID;

	public UserByCardIDNotExistException(String cardId) {
		this.cardID = cardId;
	}

	@Override
	public String getMessage() {
		return String.format("UserDTO with cardID: %s not exit", cardID);
	}

}