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

    private static final int EXPIRATION_MAIL_ONE_DAY  = 60 * 24;
    private static final int EXPIRATION_MAIL_ONE_HOUR = 60 * 1;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="token_id")
    private Long id;

    @Column(name="token_value")
    private String token;

    @Column(name="user_name")
    private String username;

    @Column(name="creation_date")
    @Type(type="timestamp")
    private Timestamp creationDate;

    @Column(name="expiry_date")
    @Type(type="timestamp")
    private Timestamp expiryDate;

    public VerificationToken(String token, String username,Timestamp creationDate,Timestamp expiryDate){
        super();
        this.username = username;
        this.token = token;
        this.creationDate = creationDate;
        this.expiryDate = expiryDate;
    }

    public VerificationToken(){}

    public VerificationToken(String object){
        this.creationDate = new Timestamp(getCreationDate().getTime()); // Same configuration for the following cases
        if(object.contentEquals("MAIL")){
            this.expiryDate = new Timestamp(calculateExpiryDate(EXPIRATION_MAIL_ONE_DAY).getTime());
        }else if(object.contentEquals("PASSWORD")){
            this.expiryDate = new Timestamp(calculateExpiryDate(EXPIRATION_MAIL_ONE_HOUR).getTime());
        }
    }

    private Date getCreationDate(){
        Calendar cal = Calendar.getInstance();
        return cal.getTime();
    }

    private Date calculateExpiryDate(int expiryTimeInMinutes){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE,expiryTimeInMinutes);
        return cal.getTime();
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

    public boolean check_token_is_expired(){
        Date current_datetime = Calendar.getInstance().getTime();
        Date expired_datetime = getExpiryDate();
        if (current_datetime.compareTo(expired_datetime) > 0) {
            return true;
        } else if (current_datetime.compareTo(expired_datetime) < 0) {
            return false;
        } else if (current_datetime.compareTo(expired_datetime) == 0) {
            return true;
        }

        return false;
    }
}
