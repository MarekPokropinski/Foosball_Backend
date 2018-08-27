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
	private long lastEspResponseTime = System.currentTimeMillis();
	private final int timeoutTime = 8000;

	@PostMapping("/espStatus")
	public ResponseEntity<String> recieveEspStatus(@RequestParam String status) {
		LOG.info(String.format("Response from ESP: %s", status));
		if (status.equals("OK")) {
			lastEspResponseTime = System.currentTimeMillis();
		}
		return ResponseEntity.ok("Status Received");
	}

	@PostMapping("/serverStatus")
	public ResponseEntity<Void> sendServerStatus() {
		LOG.info("Asked for status");
		LOG.info("Time" + String.valueOf(System.currentTimeMillis() - lastEspResponseTime));
		if (System.currentTimeMillis() - lastEspResponseTime < timeoutTime) {
			LOG.info("Esp OK");
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			LOG.info("Esp DEAD");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}
