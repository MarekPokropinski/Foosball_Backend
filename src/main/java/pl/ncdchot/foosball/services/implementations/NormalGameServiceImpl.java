package pl.ncdchot.foosball.services.implementations;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Service;
import pl.ncdchot.foosball.database.model.GameType;
import pl.ncdchot.foosball.database.model.Rules;
import pl.ncdchot.foosball.exceptions.UserNotExist;

@Service
public class NormalGameServiceImpl extends GameWithHistoryServiceImpl {
    private static final Logger LOG = Logger.getLogger(NormalGameServiceImpl.class);

    @Override
    public long startGame(long[] users, Rules rules) throws UserNotExist {
        return super.startGame(users, rules, GameType.NORMAL);
    }
}
