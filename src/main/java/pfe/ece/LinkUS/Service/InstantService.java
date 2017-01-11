package pfe.ece.LinkUS.Service;

import org.springframework.stereotype.Service;
import pfe.ece.LinkUS.Model.Instant;
import pfe.ece.LinkUS.Model.Moment;

import java.util.logging.Logger;

/**
 * Created by DamnAug on 07/01/2017.
 */
@Service
public class InstantService {

    Logger LOGGER = Logger.getLogger("LinkUS.Controller.InstantService");

    public void add(Moment moment, Instant instant) {
        LOGGER.info("Adding moment: " + moment);
        moment.getInstantList().add(instant);
    }

    public Instant find(Moment moment, String instantId) {
        for (Instant instant: moment.getInstantList()) {
            if(instant.getId().equals(instantId)){
                return instant;
            }
        }
        return null;
    }

    public void delete(Moment moment, Instant instant) {
        delete(moment, instant.getId());
    }

    public void delete(Moment moment, String instantId) {

        Instant foundInstant = null;
        for(Instant instant: moment.getInstantList()) {
            if(instant.getId().equals(instantId)) {
                foundInstant = instant;
            }
        }

        if(foundInstant != null) {
            moment.getInstantList().remove(foundInstant);
        }
    }
}
