package pl.ncdchot.foosball.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/status")
public class StatusController {
	@PostMapping("/serverStatus")
	public ResponseEntity<Void> sendServerStatus() {
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
