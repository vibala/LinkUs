package pfe.ece.LinkUS.Service;

import org.springframework.stereotype.Service;
import pfe.ece.LinkUS.Model.Album;
import pfe.ece.LinkUS.Model.Enum.Right;
import pfe.ece.LinkUS.Model.IdRight;
import pfe.ece.LinkUS.Model.Instant;
import pfe.ece.LinkUS.Model.Moment;

import java.util.logging.Logger;

/**
 * Created by DamnAug on 07/01/2017.
 */
@Service
public class IdRightService {

    Logger LOGGER = Logger.getLogger("LinkUS.Controller.IdRightService");

    public void addUserToIdRight(IdRight idRight, String userId) {

        if(!idRight.getUserIdList().contains(userId)) {
            LOGGER.info("Adding User : " + idRight + "to right: " + idRight.getRight());
            idRight.getUserIdList().add(userId);
        }
    }

    /**
     *
     *
     * IdRight for albums
     *
     *
     */

    public IdRight findByRight(Album album, String rightName) {
        for(IdRight idRight: album.getIdRight()) {
            if(idRight.getRight().equals(rightName)) {
                return idRight;
            }
        }
        return null;
    }

    public void addIdRightToAlbum(Album album, IdRight idRight) {
        LOGGER.info("Adding IdRight: " + idRight + "to album: " + album.getId());
        if(findByRight(album, idRight.getRight()) == null) {
            album.getIdRight().add(idRight);
        }
    }

    /**
     *
     *
     * IdRight for Moment
     *
     *
     */

    public void addIdRightToAllInstant(Moment moment, IdRight idRight) {

        for(Instant instant: moment.getInstantList()) {
            addIdRightToInstant(instant, idRight);
        }
    }

    public void addAllIdRightToAllInstant(Moment moment) {
        for(Right right: Right.values()) {
            IdRight idRight = new IdRight(right.name());
            addIdRightToAllInstant(moment, idRight);
        }
    }
    /**
     *
     *
     * IdRight for instant
     *
     *
     */

    public IdRight findByRight(Instant instant, String rightName) {
        for(IdRight idRight: instant.getIdRight()) {
            if(idRight.getRight().equals(rightName)) {
                return idRight;
            }
        }
        return null;
    }

    public void addIdRightToInstant(Instant instant, IdRight idRight) {

        LOGGER.info("Adding IdRight: " + idRight + "to instant: " + instant.getId());
        if(findByRight(instant, idRight.getRight()) == null) {
            instant.getIdRight().add(idRight);
        }
    }
}
