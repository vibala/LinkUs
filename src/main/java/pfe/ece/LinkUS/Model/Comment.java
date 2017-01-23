package pfe.ece.LinkUS.Model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by DamnAug on 14/10/2016.
 */
public class Comment {

    private String id;
    private String userId;
    private String text;
    private Date date;
    private boolean seen = false;

    public Comment() {
        setRandomId();
    }

    public void setRandomId() {
        if(getId().equals(null) || getId().equals("")) {
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comment comment = (Comment) o;

        if (!userId.equals(comment.userId)) return false;
        return date.equals(comment.date);

    }

    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + date.hashCode();
        return result;
    }
}
