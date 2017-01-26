package pfe.ece.LinkUS.Model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * Created by DamnAug on 24/01/2017.
 */
public class Notification {

    @Id
    private String id;
    private String userId;
    private String albumId;
    private String momentId;
    private String type;
    private Date creationDate = new Date();
    private Object object;

    public Notification() {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getMomentId() {
        return momentId;
    }

    public void setMomentId(String momentId) {
        this.momentId = momentId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
