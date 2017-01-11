package pfe.ece.LinkUS.Service;

import org.springframework.stereotype.Service;
import pfe.ece.LinkUS.Model.Album;
import pfe.ece.LinkUS.Model.IdRight;
import pfe.ece.LinkUS.Model.Right;

import java.util.logging.Logger;

/**
 * Created by DamnAug on 07/01/2017.
 */
@Service
public class IdRightService {

    Logger LOGGER = Logger.getLogger("LinkUS.Controller.IdRightService");


    public IdRight findByRight(Album album, String rightName) {
        for(IdRight idRight: album.getIdRight()) {
            if(idRight.getRight().equals(rightName)) {
                return idRight;
            }
        }
        return null;
    }

    public void add(Album album, IdRight idRight) {
        LOGGER.info("Adding IdRight: " + idRight);
        if(findByRight(album, idRight.getRight()) == null) {
            album.getIdRight().add(idRight);
        }
    }

    public void addUserToAll(Album album, String userId){

        for(IdRight idRight: album.getIdRight()) {
            addUser(idRight, userId);
        }
    }

    public void addUser(IdRight idRight, String userId) {

        if(!idRight.getUserIdList().contains(userId)) {
            LOGGER.info("Adding User : " + idRight + "to right: " + idRight.getRight());
            idRight.getUserIdList().add(userId);
        }
    }
}
