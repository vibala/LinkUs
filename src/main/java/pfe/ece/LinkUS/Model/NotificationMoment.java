package pfe.ece.LinkUS.Model;

import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by DamnAug on 24/01/2017.
 */
public class NotificationMoment extends Notification {

    private String albumId;
    private String momentId;

    private Date creationDate = new Date();

    public NotificationMoment() {
        super();
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


    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
