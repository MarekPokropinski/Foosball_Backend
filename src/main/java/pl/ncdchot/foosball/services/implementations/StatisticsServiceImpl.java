package pl.ncdchot.foosball.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.ncdchot.foosball.database.model.Statistics;
import pl.ncdchot.foosball.database.repository.StatsRepository;
import pl.ncdchot.foosball.services.StatisticsService;


@Service
public class StatisticsServiceImpl implements StatisticsService {

	@Autowired
	StatsRepository statsRepository;

	@Override
	public Statistics createEmpty() {
		Statistics stats = new Statistics();
		statsRepository.save(stats);

		return stats;
	}

	@Override
	public void saveStats(Statistics stats) {
		statsRepository.save(stats);
	}

}
