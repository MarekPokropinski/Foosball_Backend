package pl.ncdchot.foosball.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "User with this ID not exist")
public class UserNotExistException extends Throwable {
	private static final long serialVersionUID = -8851281773946507170L;
	private long id;

	public UserNotExistException(long userID) {
		this.id = userID;
	}

	@Override
	public String getMessage() {
		return String.format("UserDTO with id: %s not exit", id);
	}
}
