package pfe.ece.LinkUS.Model;

import javax.persistence.*;

/**
 * Created by Vignesh on 12/15/2016.
 */
@Entity
@Table(name = "notification_tokens")
public class NotificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String username;

    private String token;

    public NotificationToken(){}

    public NotificationToken(long id){this.id = id;}

    public NotificationToken(String username,String token){
        this.username = username;
        this.token = token;
    }



}
