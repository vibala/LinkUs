package pfe.ece.LinkUS.Model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * Created by DamnAug on 15/11/2016.
 */
public class Subscription {

    @Id
    private String id;
    private String type;
    private String userId;
    private Date dateBegin;
    private Date dateEnd;
    private int free;

    public Subscription(String type, String userId) {
        this.type = type;
        this.userId = userId;
    }

    public Subscription(String type, String userId, Date dateBegin) {
        this.type = type;
        this.userId = userId;
        this.dateBegin = dateBegin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Subscription that = (Subscription) o;

        if (!type.equals(that.type)) return false;
        return userId.equals(that.userId);

    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + userId.hashCode();
        return result;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getDateDebut() {
        return dateBegin;
    }

    public void setDateDebut(Date dateBegin) {
        this.dateBegin = dateBegin;
    }

    public Date getDateFin() {
        return dateEnd;
    }

    public void setDateFin(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public int getFree() {
        return free;
    }

    public void setFree(int free) {
        this.free = free;
    }
}
