package pfe.ece.LinkUS.Model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;

import java.io.Serializable;

/**
 * Created by DamnAug on 16/01/2017.
 */
public class ConfigUser implements Serializable{



    private String id;
    private boolean news = true;
    private boolean receiveNotification = true;
    private boolean onlyFriendCanContactMe = false;

    public ConfigUser() {
        setRandomId();
    }

    public void setRandomId() {
        if(getId()== null || getId().equals("")) {
            ObjectId objectId = new ObjectId();
            setId(objectId.toString());
        }
    }

    @Override
    public String toString() {
        String str = "";
        ObjectMapper mapper = new ObjectMapper();
        try {
            str = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return str;
    }

    public boolean isNews() {
        return news;
    }

    public void setNews(boolean news) {
        this.news = news;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isReceiveNotification() {
        return receiveNotification;
    }

    public void setReceiveNotification(boolean receiveNotification) {
        this.receiveNotification = receiveNotification;
    }

    public boolean isOnlyFriendCanContactMe() {
        return onlyFriendCanContactMe;
    }

    public void setOnlyFriendCanContactMe(boolean onlyFriendCanContactMe) {
        this.onlyFriendCanContactMe = onlyFriendCanContactMe;
    }
}
