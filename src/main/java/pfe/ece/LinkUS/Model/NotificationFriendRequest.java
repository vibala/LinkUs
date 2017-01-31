package pfe.ece.LinkUS.Model;

import org.bson.types.ObjectId;
import pfe.ece.LinkUS.Model.Enum.NotificationType;

/**
 * Created by DamnAug on 30/01/2017.
 */
public class NotificationFriendRequest extends Notification {

    String fromFriendId;
    String message="Vous avez reçu une demande d'ami";

    public NotificationFriendRequest() {
        super();
    }

    public void setRandomId() {
        if(getId()== null || getId().equals("")) {
            ObjectId objectId = new ObjectId();
            setId(objectId.toString());
        }
    }

    public String getFromFriendId() {
        return fromFriendId;
    }

    public void setFromFriendId(String fromFriendId) {
        this.fromFriendId = fromFriendId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
