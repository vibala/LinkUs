package pfe.ece.LinkUS.Model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.annotation.Id;

/**
 * Created by DamnAug on 16/01/2017.
 */
public class ConfigUser {

    @Id
    private String id;
    private boolean news = true;
    private boolean receiveNotification = true;
    private boolean onlyFriendCanContactMe = false;

    public ConfigUser() {
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
