package pfe.ece.LinkUS.Service;

import org.springframework.stereotype.Service;
import pfe.ece.LinkUS.Model.Enum.Right;
import pfe.ece.LinkUS.Model.IdRight;
import pfe.ece.LinkUS.Model.Instant;
import pfe.ece.LinkUS.Model.Moment;

import java.util.logging.Logger;

/**
 * Created by DamnAug on 07/01/2017.
 */
@Service
public class InstantService {

    Logger LOGGER = Logger.getLogger("LinkUS.Controller.InstantService");

    public void addInstantToMoment(Moment moment, Instant instant) {
        LOGGER.info("Adding instant: " + instant + "to moment: " + moment.getId());
        moment.getInstantList().add(instant);
    }

    public Instant findInstantInMoment(Moment moment, String instantId) {
        for (Instant instant: moment.getInstantList()) {
            if(instant.getId().equals(instantId)){
                return instant;
            }
        }
        return null;
    }

    public void deleteInstantFromMoment(Moment moment, Instant instant) {
        deleteInstantFromMoment(moment, instant.getId());
    }

    public void deleteInstantFromMoment(Moment moment, String instantId) {

        Instant foundInstant = null;
        for(Instant instant: moment.getInstantList()) {
            if(instant.getId().equals(instantId)) {
                foundInstant = instant;
            }
        }

        if(foundInstant != null) {
            LOGGER.info("Removing instant: " + instantId + "from moment: " + moment.getId());
            moment.getInstantList().remove(foundInstant);
        }
    }

    public void addUserToAllInstantAllIdRight(Moment moment, String userId) {

        for (Instant instant: moment.getInstantList()) {
            addUserToInstantAllIdRight(instant, userId);
        }
    }

    /**
     * On crée le IdRight dans tous les cas et si il existe on ne l'ajoute pas à instant
     *
     * @param instant
     * @param userId
     */
    public void addUserToInstantAllIdRight(Instant instant, String userId) {

        IdRightService idRightService = new IdRightService();
        for(Right right: Right.values()) {
            IdRight idRight = new IdRight(right.name());
            idRightService.addUserToIdRight(idRight, userId);
            idRightService.addIdRightToInstant(instant, idRight);
        }

    }

    public void addUserToAllInstantIdRight(Moment moment, String right, String userId) {

        for (Instant instant: moment.getInstantList()) {
            addUserToInstantIdRight(instant, right, userId);
        }
    }

    /**
     * On crée le IdRight dans tous les cas et si il existe on ne l'ajoute pas  à instant
     *
     * @param instant
     * @param right
     * @param userId
     */
    public void addUserToInstantIdRight(Instant instant, String right, String userId) {

        IdRightService idRightService = new IdRightService();
        for(Right rightStr: Right.values()) {
            if(rightStr.equals(right)) {
                IdRight idRight = new IdRight(rightStr.name());
                idRightService.addUserToIdRight(idRight, userId);
                idRightService.addIdRightToInstant(instant, idRight);
            }
        }
    }

    public void checkAllInstantDataRight(Moment moment, String userId) {

        for(Instant instant: moment.getInstantList()) {
            if(!checkInstantDataRight(instant, userId)) {
                deleteInstantFromMoment(moment, instant);
            }
        }
    }

    public boolean checkInstantDataRight(Instant instant, String userId) {

        // Si le user ne possède pas le droit LECTURE on delete
        for (IdRight idRight: instant.getIdRight()) {
            if (idRight.getRight().equals(Right.LECTURE) && !idRight.getRight().contains(userId)) {
                return false;
            }
        }
        return true;
    }
}
