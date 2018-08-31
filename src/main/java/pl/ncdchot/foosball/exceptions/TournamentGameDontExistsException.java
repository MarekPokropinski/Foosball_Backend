package pl.ncdchot.foosball.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Tournament game not found")
public class TournamentGameDontExistsException extends Exception {
	private static final long serialVersionUID = 620968454308937088L;
}
