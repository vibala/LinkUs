package pfe.ece.LinkUS.Model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;

/**
 * Created by DamnAug on 15/11/2016.
 */
public class Subscription {

    private String id;
    private String type;
    private String userId;
    private Date dateBegin;
    private Date dateEnd;
    private int free;

    public Subscription(String id, String type, String userId) {
        this.id = id;
        this.type = type;
        this.userId = userId;
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

    public void setDescriptionFree(int free) {
        this.free = free;
    }
}
