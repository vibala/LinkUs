package pfe.ece.LinkUS.Model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by DamnAug on 14/10/2016.
 */
@Document(collection ="album")
public class Album {

    @Id
    private String id;
    private String name;
    private String ownerId;
    private String countryName;
    private String placeName;
    private Date beginDate;
    private Date endDate;
    private ArrayList<Moment> moments = new ArrayList<>();
    private ArrayList<IdRight> idRight = new ArrayList<>();
    private boolean active;
    private String imageUrl;


    public Album() {
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

    public void setName(String name) {
        this.name = name;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setMoments(ArrayList<Moment> moments) {
        this.moments = moments;
    }

    public void setIdRight(ArrayList<IdRight> idRight) {
        this.idRight = idRight;
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

    public String getOwnerId() {
        return ownerId;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getPlaceName() {
        return placeName;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public ArrayList<Moment> getMoments() {
        return moments;
    }

    public ArrayList<IdRight> getIdRight() {
        return idRight;
    }

    public IdRight getSpecificIdRight(String filter) {
        IdRight idRightFiltered = new IdRight();
        //On filtre sur les droits de lectures
        for(IdRight right: idRight) {
            if (right.getRight().equals(filter)) {
                idRightFiltered=right;
                break;
            }
        }
        return idRightFiltered;
    }
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Album album = (Album) o;

        return id.equals(album.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
