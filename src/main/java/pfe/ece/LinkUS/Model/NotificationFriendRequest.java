package pfe.ece.LinkUS.Model;

import org.bson.types.ObjectId;

/**
 * Created by DamnAug on 30/01/2017.
 */
public class NotificationFriendRequest extends Notification {

    String friendId;
    String type;
    String message;

    public NotificationFriendRequest() {
        super();
    }

    public void setRandomId() {
        if(getId()== null || getId().equals("")) {
            ObjectId objectId = new ObjectId();
            setId(objectId.toString());
        }
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
