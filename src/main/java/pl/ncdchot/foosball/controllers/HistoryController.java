package pl.ncdchot.foosball.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.ncdchot.foosball.game.GameSummary;
import pl.ncdchot.foosball.modelDTO.HistoryDTO;
import pl.ncdchot.foosball.services.GameService;
import pl.ncdchot.foosball.services.UserService;

@RestController
@RequestMapping("/history")
public class HistoryController {
	@Autowired
	private UserService userService;

	@Autowired
	private GameService gameService;

	@GetMapping("/leaderboards")
	public ResponseEntity<List<HistoryDTO>> getLeaderboards() {
		List<HistoryDTO> historyList = userService.getAllHistory();
		return new ResponseEntity<>(historyList, HttpStatus.OK);
	}

	@GetMapping("/history")
	public ResponseEntity<List<GameHistoryDTO>> getLastGames() {
		List<GameHistoryDTO> lastGames = gameService.getLastGames();
		return new ResponseEntity<>(lastGames, HttpStatus.OK);
	}
}
