package pl.ncdchot.foosball.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Game not found")
public class GameNotFoundException extends Exception {
	private static final long serialVersionUID = 620968454308937088L;
}
