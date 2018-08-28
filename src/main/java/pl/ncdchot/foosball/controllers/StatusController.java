package pl.ncdchot.foosball.controllers;

import org.jboss.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/status")
public class StatusController {

	private static final Logger LOG = Logger.getLogger(StatusController.class);

	@PostMapping("/serverStatus")
	public ResponseEntity<Void> sendServerStatus() {
			return new ResponseEntity<>(HttpStatus.OK);
	}
}
