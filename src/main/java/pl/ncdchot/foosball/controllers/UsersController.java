package pl.ncdchot.foosball.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pl.ncdchot.foosball.services.ManagementSystemService;

@RestController
public class UsersController {
	private ManagementSystemService tsService;

	@Autowired
	UsersController(ManagementSystemService tsService) {
		this.tsService = tsService;
	}

	@GetMapping("/getByNick")
	ResponseEntity<Long> getIdByNick(@RequestParam String nick) {
		Optional<Long> optId = tsService.getUserIdByNick(nick);
		return optId.isPresent() ? new ResponseEntity<>(optId.get(), HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
}