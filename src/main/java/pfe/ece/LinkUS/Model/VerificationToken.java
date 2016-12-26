package pfe.ece.LinkUS.Model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Vignesh on 12/9/2016.
 */
@Entity
@Table(name= "verification_tokens")
public class VerificationToken {

    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="token_id")
    private Long id;

    @Column(name="token_value")
    private String token;

    @Column(name="user_name")
    private String username;

    @Column(name="expiry_date")
    @Type(type="timestamp")
    private Timestamp expiryDate;

    public VerificationToken(){
        this.expiryDate = new Timestamp(calculateExpiryDate(EXPIRATION).getTime());
    }

    public VerificationToken(String token, String username){
        super();
        this.username = username;
        this.token = token;
    }

    private Date calculateExpiryDate(int expiryTimeInMinutes){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE,expiryTimeInMinutes);
        return cal.getTime();
    }


    public static int getEXPIRATION() {
        return EXPIRATION;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

}
