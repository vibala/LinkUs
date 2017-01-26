package pfe.ece.LinkUS.Model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by DamnAug on 20/01/2017.
 */
public class UserAlarm {

    @Id
    private String id;
    private Date date = new Date();
    private String userId;
    private String type;
    private HashMap<Character, Boolean> alarm;

    public UserAlarm() {
        setRandomId();
    }

    public void setRandomId() {
        if(getId()== null || getId().equals("")) {
            ObjectId objectId = new ObjectId();
            setId(objectId.toString());
        }
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public HashMap<Character, Boolean> getAlarm() {
        return alarm;
    }

    public void setAlarm(HashMap<Character, Boolean> alarm) {
        this.alarm = alarm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserAlarm userAlarm = (UserAlarm) o;

        if (!userId.equals(userAlarm.userId)) return false;
        if (!type.equals(userAlarm.type)) return false;
        return alarm.equals(userAlarm.alarm);

    }

    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + alarm.hashCode();
        return result;
    }
}
