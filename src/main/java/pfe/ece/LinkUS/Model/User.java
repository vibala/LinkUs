package pfe.ece.LinkUS.Model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.bcrypt.BCrypt;

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

    @Field("age")
    private int age;

    @Field("friendList")
    private ArrayList<String> friendList = new ArrayList<>();

    @Field("friendPendingList")
    private ArrayList<String> friendPendingList = new ArrayList<>();


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
        this.enabled = false;
    }

    public User (String user_id){
        this.setId(user_id);
    }

    public User(String lastName, String firstName, String email, String plain_password, String sexe,Date dateofBirth,String role) {
        this();
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.passwordHash = BCrypt.hashpw(plain_password, BCrypt.gensalt());
        this.sexe = sexe;
        this.dateofBirth = dateofBirth;
        this.role = Role.valueOf(role);
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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
}
