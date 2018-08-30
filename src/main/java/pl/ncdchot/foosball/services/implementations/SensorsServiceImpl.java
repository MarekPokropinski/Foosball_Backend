package pl.ncdchot.foosball.services.implementations;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import pl.ncdchot.foosball.modelDTO.StatusDTO;
import pl.ncdchot.foosball.services.SensorsService;

@Service
public class SensorsServiceImpl implements SensorsService {

	private static final Logger LOG = Logger.getLogger(SensorsServiceImpl.class);
	@Value("${sensors.url}")
	private String SENSORS_URL;

	private static final String SEND_ENDPOINT = "/send";

	@Autowired
	RestTemplate restTemplate;

	@Override
	public void turnOn(boolean isOn) {
		String status = isOn ? "on" : "off";
		try {
			System.out.println(SENSORS_URL);
			restTemplate.postForObject(SENSORS_URL, new StatusDTO(status), StatusDTO.class, SEND_ENDPOINT);
		} catch (RestClientException e) {
			LOG.warn("no connection with sensors");
		}
	}

}
