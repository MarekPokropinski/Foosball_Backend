package pl.ncdchot.foosball.services.implementations;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Service;

import pl.ncdchot.foosball.database.model.GameType;
import pl.ncdchot.foosball.database.model.Rules;
import pl.ncdchot.foosball.exceptions.UserNotExistException;

@Service
public class NormalGameServiceImpl extends GameWithHistoryServiceImpl {
    private static final Logger LOG = Logger.getLogger(NormalGameServiceImpl.class);

    @Override
    public long startGame(long[] redTeamUsers, long[] blueTeamUsers, Rules rules) throws UserNotExistException {
        LOG.info("Started ranked game");
        return super.startGame(redTeamUsers, blueTeamUsers, rules, GameType.NORMAL);
    }
}
