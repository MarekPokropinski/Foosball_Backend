package pl.ncdchot.foosball.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pl.ncdchot.foosball.database.model.User;
import pl.ncdchot.foosball.exceptions.UserNotExist;
import pl.ncdchot.foosball.services.ManagementSystemService;
import pl.ncdchot.foosball.services.UserService;

@RestController
public class UsersController {
	private ManagementSystemService managementSystemService;
	private UserService userService;

	@Autowired
	UsersController(ManagementSystemService tsService, UserService userService) {
		this.managementSystemService = tsService;
		this.userService = userService;
	}

	@GetMapping("/getByNick")
	ResponseEntity<Long> getIdByNick(@RequestParam String nick) {
		Optional<Long> optId = managementSystemService.getUserIdByNick(nick);
		return optId.isPresent() ? new ResponseEntity<>(optId.get(), HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/getNickById")
	ResponseEntity<String> getNick(@RequestParam Long id) {
		Optional<User> optUser = userService.getUser(id);
		try {
			Long externalId = optUser.orElseThrow(() -> new UserNotExist(id)).getExternalID();
			String nick = managementSystemService.getExternalUserByExternalId(externalId).getNick();
			return new ResponseEntity<>(nick, HttpStatus.OK);
		} catch (UserNotExist e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}