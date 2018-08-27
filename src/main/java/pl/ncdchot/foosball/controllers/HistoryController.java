package pl.ncdchot.foosball.controllers;

import java.util.List;

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
	public ResponseEntity<List<HistoryDTO>> getLeaderBoards() {
		List<HistoryDTO> historyList = userHistoryService.getAllHistory();
		return new ResponseEntity<>(historyList, HttpStatus.OK);
	}
}
