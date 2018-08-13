package pl.ncdchot.foosball.services;

import pl.ncdchot.foosball.database.model.Statistics;

public interface StatisticsService {
	Statistics createEmpty();
	void saveStats(Statistics stats);
}
