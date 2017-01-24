package pfe.ece.LinkUS.Model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;
import pfe.ece.LinkUS.Model.Enum.Role;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by DamnAug on 12/10/2016.
 */
@Document(collection = "user")
public class User implements Serializable {

    @Id
    private String id;

    @NotEmpty
    @Field("lastName")
    private String lastName;

    @NotEmpty
    @Field("firstName")
    private String firstName;

    @NotEmpty
    @Field("email")
    private String email;

    @NotEmpty
    @Field("password")
    private String passwordHash;

    @NotEmpty
    @Field("sexe")
    private String sexe;

    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE)
    private Date dateofBirth;

    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE)
    private Date dateofRegistration;

    @Field("enabled")
    private boolean enabled;

    @NotEmpty
    @Field("role")
    private Role role;

    @Field("friendList")
    private ArrayList<String> friendList = new ArrayList<>();

    @Field("friendPendingList")
    private ArrayList<String> friendPendingList = new ArrayList<>();

    @Field("profilImgUrl")
    private String profilImgUrl;

    @Field("friendGroupList")
    private ArrayList<String> friendGroupIdList = new ArrayList<>();

    @Field("configUser")
    private ConfigUser configUser = new ConfigUser();


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

    public boolean isEnabled() {
        return enabled;
    }

    public User() {
        // Default Constructor
        setRandomId();
        setEnabled(false);
    }

    public void setRandomId() {
        if(getId()== null || getId().equals("")) {
            ObjectId objectId = new ObjectId();
            setId(objectId.toString());
        }
    }

    public User (String user_id){
        this.setId(user_id);
        this.enabled = false;
    }

    //
    //
    //
    // GETTERS AND SETTERS
    //
    //
    //
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public Date getDateofBirth() {
        return dateofBirth;
    }

    public void setDateofBirth(Date dateofBirth) {
        this.dateofBirth = dateofBirth;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<String> getFriendList() {
        return friendList;
    }

    public void setFriendList(ArrayList<String> friendList) {
        this.friendList = friendList;
    }

    public List<String> getFriendPendingList() {
        return friendPendingList;
    }

    public void setFriendPendingList(ArrayList<String> friendPendingList) {
        this.friendPendingList = friendPendingList;
    }

    public Date getDateofRegistration() {
        return dateofRegistration;
    }

    public void setDateofRegistration(Date dateofRegistration) {
        this.dateofRegistration = dateofRegistration;
    }

    public String getProfilImgUrl() {
        return profilImgUrl;
    }

    public void setProfilImgUrl(String profilImgUrl) {
        this.profilImgUrl = profilImgUrl;
    }

    public ArrayList<String> getFriendGroupIdList() {
        return friendGroupIdList;
    }

    public void setFriendGroupIdList(ArrayList<String> friendGroupIdList) {
        this.friendGroupIdList = friendGroupIdList;
    }

    public ConfigUser getConfigUser() {
        return configUser;
    }

    public void setConfigUser(ConfigUser configUser) {
        this.configUser = configUser;
    }
}
