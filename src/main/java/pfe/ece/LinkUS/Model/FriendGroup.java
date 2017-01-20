package pfe.ece.LinkUS.Model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DamnAug on 14/10/2016.
 */
public class FriendGroup {

    @Id
    private String id;
    private String ownerId;
    private String name;
    private List<String> members = new ArrayList<>();
    private String groupImgUrl;

    public String toString(){
        String str = "";
        ObjectMapper mapper = new ObjectMapper();
        try {
            str = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return str;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public List<String> getMembers() {
        return members;
    }

    public String getGroupImgUrl() {
        return groupImgUrl;
    }

    public void setGroupImgUrl(String groupImgUrl) {
        this.groupImgUrl = groupImgUrl;
    }

    /**
     * Check on ownerId & name
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FriendGroup that = (FriendGroup) o;

        if (!ownerId.equals(that.ownerId)) return false;
        return name.equals(that.name);

    }

    @Override
    public int hashCode() {
        int result = ownerId.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
