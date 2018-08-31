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

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public void changeStatus(boolean status) {
		String statusText = status ? "on" : "off";
		try {
			restTemplate.postForObject(SENSORS_URL, new StatusDTO(statusText), StatusDTO.class);
		} catch (RestClientException e) {
			LOG.warn("no connection with sensors");
		}
	}
}
