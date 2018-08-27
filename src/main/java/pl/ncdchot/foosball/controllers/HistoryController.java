package pl.ncdchot.foosball.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.ncdchot.foosball.modelDTO.HistoryDTO;
import pl.ncdchot.foosball.services.UserHistoryService;

@RestController
@RequestMapping("/history")
public class HistoryController {

	@Autowired
	private UserHistoryService userHistoryService;

	@GetMapping("/leaderboards")
	public ResponseEntity<HistoryDTO[]> getLeaderBoards() {
		userHistoryService.getAllHistory();
		return new ResponseEntity<>(new HistoryDTO[0], HttpStatus.OK);
	}
}
