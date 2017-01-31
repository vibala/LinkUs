package pfe.ece.LinkUS.Service;

import org.springframework.stereotype.Service;
import pfe.ece.LinkUS.Model.Album;
import pfe.ece.LinkUS.Model.Enum.Right;
import pfe.ece.LinkUS.Model.IdRight;
import pfe.ece.LinkUS.Model.Instant;
import pfe.ece.LinkUS.Model.Moment;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by DamnAug on 07/01/2017.
 */
@Service
public class IdRightService {

    Logger LOGGER = Logger.getLogger("LinkUS.Service.IdRightService");

    public boolean addUserToIdRight(IdRight idRight, String userId) {

        if(!idRight.getUserIdList().contains(userId)) {
            LOGGER.info("Adding User : " + userId + " to right: " + idRight.getRight());
            idRight.getUserIdList().add(userId);
            return true;
        }
        return false;
    }

    public boolean addUsersToIdRight(IdRight idRight, List<String> userList) {
        boolean bool = true;
        for (String userId: userList) {
            if(!addUserToIdRight(idRight, userId)) {
                bool = false;
            }
        }
        return bool;
    }

    public boolean checkUserInIdRight(IdRight idRight, String userId) {

        if(idRight.getUserIdList().contains(userId)) {
            return true;
        }
        return false;
    }

    public boolean addFriendGroupToIdRight(IdRight idRight, String friendGroupId) {

        if(!idRight.getGroupIdList().contains(friendGroupId)) {
            LOGGER.info("Adding FriendGroup : " + friendGroupId + " to right: " + idRight.getRight());
            idRight.getGroupIdList().add(friendGroupId);
            return true;
        }
        return false;
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

    public boolean addIdRightToAlbum(Album album, IdRight idRight) {
        LOGGER.info("Adding IdRight: " + idRight.getRight() + "to album: " + album.getId());
        if(findByRight(album, idRight.getRight()) == null) {
            album.getIdRight().add(idRight);
            return true;
        }
        return false;
    }

    /**
     *
     *
     * IdRight for Moment
     *
     *
     */

    public boolean addIdRightToAllInstant(Moment moment, IdRight idRight) {

        boolean bool = true;
        for(Instant instant: moment.getInstantList()) {
            if(!addIdRightToInstant(instant, idRight)) {
                bool = false;
            }
        }
        return bool;
    }

    public boolean addAllIdRightToAllInstant(Moment moment) {
        boolean bool = true;

        for(Right right: Right.values()) {
            IdRight idRight = new IdRight(right.name());
            if(!addIdRightToAllInstant(moment, idRight)) {
                bool = false;
            }
        }
        return bool;
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

    public boolean addIdRightToInstant(Instant instant, IdRight idRight) {

        if(findByRight(instant, idRight.getRight()) == null) {
            LOGGER.info("Adding IdRight: " + idRight.getRight() + " to instant: " + instant.getId());
            instant.getIdRight().add(idRight);
            return true;
        }
        return false;
    }

    public List<String> getUsersFromAllIdRight(Instant instant) {

        List<String> usersList = new ArrayList<>();
        for(IdRight idRight: instant.getIdRight()) {
            usersList.addAll(getUsersFromIdRight(idRight));
        }
        return usersList;
    }

    public List<String> getUsersFromIdRight(IdRight idRight) {

        if(idRight != null){
            return idRight.getUserIdList();
        }
        return new ArrayList<>();
    }
}
