package pfe.ece.LinkUS.Model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.ArrayList;

/**
 * Created by DamnAug on 05/01/2017.
 */
public class Moment {

    private String id;
    private String name;
    private ArrayList<Instant> instantList = new ArrayList();
    private ArrayList<KeyValue> descriptionsList = new ArrayList<>();
    private boolean news = true;
    private String mainInstant;

    public Moment() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Instant> getInstantList() {
        return instantList;
    }

    public void setInstantList(ArrayList<Instant> instantList) {
        this.instantList = instantList;
    }

    public boolean isNews() {
        return news;
    }

    public void setNews(boolean news) {
        this.news = news;
    }

    public String getMainInstant() {
        return mainInstant;
    }

    public void setMainInstant(String mainInstant) {
        this.mainInstant = mainInstant;
    }

    public ArrayList<KeyValue> getDescriptionsList() {
        return descriptionsList;
    }

    public void setDescriptionsList(ArrayList<KeyValue> descriptionsList) {
        this.descriptionsList = descriptionsList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Moment moment = (Moment) o;

        return instantList.equals(moment.instantList);

    }

    @Override
    public int hashCode() {
        return instantList.hashCode();
    }
}
