package pl.ncdchot.foosball.services.implementations;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import pl.ncdchot.foosball.modelDTO.StatusDTO;
import pl.ncdchot.foosball.services.SensorsService;

public class SensorsServiceImpl implements SensorsService {

	private static final Logger LOG = Logger.getLogger(SensorsServiceImpl.class);
	@Value("${sensors.endpoint}")
	private static String SENSORS_ENDPOINT;

	@Autowired
	RestTemplate restTemplate;

	@Override
	public void turnOn(boolean isOn) {
		String status = isOn ? "on" : "off";
		try {
			restTemplate.postForObject(SENSORS_ENDPOINT, new StatusDTO(status), StatusDTO.class);
		} catch (RestClientException e) {
			LOG.warn("no connection with sensors");
		}
	}

}
