package pl.ncdchot.foosball.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import pl.ncdchot.foosball.database.model.User;
import pl.ncdchot.foosball.exceptions.UserByCardIDNotExistException;
import pl.ncdchot.foosball.exceptions.UserByNickNoExistException;
import pl.ncdchot.foosball.exceptions.UserNotExistException;
import pl.ncdchot.foosball.modelDTO.UserDTO;
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
	public ResponseEntity<Long> getIdByNick(@RequestParam String nick) {
		Optional<Long> optId = managementSystemService.getUserIdByNick(nick);
		return optId.isPresent() ? new ResponseEntity<>(optId.get(), HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/getNickById")
	public ResponseEntity<String> getNick(@RequestParam Long id) {
		Optional<User> optUser = userService.getUser(id);
		try {
			Long externalId = optUser.orElseThrow(() -> new UserNotExistException(id)).getExternalID();
			String nick = managementSystemService.getExternalUserByExternalId(externalId).getNick();
			return new ResponseEntity<>(nick, HttpStatus.OK);
		} catch (UserNotExistException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/getByCardID")
	@ResponseBody
	public ResponseEntity<UserDTO> getByCardId(@RequestParam String cardID) throws UserByCardIDNotExistException {
		try {
			UserDTO user = managementSystemService.getUserByCardID(cardID);
			return ResponseEntity.ok(user);
		} catch (UserByCardIDNotExistException exception) {
			throw exception;
		}
	}

	@GetMapping("/getByNickOrId")
	@ResponseBody
	public ResponseEntity<UserDTO> getUserByNickOrCard(@RequestParam String value)
			throws UserByNickNoExistException, UserNotExistException, UserByCardIDNotExistException {
		try {
			UserDTO user = managementSystemService.getUserByNickOrCardID(value);
			return ResponseEntity.ok(user);
		} catch (UserByCardIDNotExistException e) {
			throw e;
		} catch (UserNotExistException e) {
			throw e;
		} catch (UserByNickNoExistException e) {
			throw e;
		}
	}
}