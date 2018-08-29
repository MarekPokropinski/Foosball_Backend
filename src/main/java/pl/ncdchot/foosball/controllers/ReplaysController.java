package pl.ncdchot.foosball.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.ncdchot.foosball.modelDTO.ReplayDTO;
import pl.ncdchot.foosball.services.ReplaysService;

@RestController
public class ReplaysController {
	
	private ReplaysService replaysService;
	
	@Autowired
	ReplaysController(ReplaysService replaysService) {
		this.replaysService = replaysService;
	}

	@GetMapping("/getReplays")
	public ResponseEntity<ArrayList<ReplayDTO>> getReplays(long gameId) {
		ArrayList<ReplayDTO> list = replaysService.getReplays(gameId);
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
}
