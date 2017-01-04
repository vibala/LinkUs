package pfe.ece.LinkUS.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Vignesh on 12/12/2016.
 */
public class FacebookUserCreateForm {

    @JsonProperty("facebook_id")
    private String ufacebook_id;

    @JsonProperty("first_name")
    private String ufirst_name;

    @JsonProperty("last_name")
    private String ulast_name;

    @JsonProperty("gender")
    private String ugender;

    @JsonProperty("birthday")
    private String ubirth_day;

    @JsonProperty("email")
    private String uemail;

    @JsonProperty("enabled")
    private boolean enabled = true;

    @JsonProperty("role")
    private Role role = Role.USER;

    @JsonProperty("facebook_token")
    private String ufbtoken;

    public FacebookUserCreateForm(){}

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getEmail() {
        return uemail;
    }

    public void setEmail(String email) {
        this.uemail = email;
    }

    public String getBirth_day() {
        return ubirth_day;
    }

    public void setBirth_day(String birth_day) {
        this.ubirth_day = birth_day;
    }

    public String getGender() {
        return ugender;
    }

    public void setGender(String gender) {
        this.ugender = gender;
    }

    public String getLast_name() {
        return ulast_name;
    }

    public void setLast_name(String last_name) {
        this.ulast_name = last_name;
    }

    public String getFirst_name() {
        return ufirst_name;
    }

    public void setFirst_name(String first_name) {
        this.ufirst_name = first_name;
    }

    public String getFacebook_id() {
        return ufacebook_id;
    }

    public void setFacebook_id(String facebook_id) {
        this.ufacebook_id = facebook_id;
    }

    public String getUfbtoken() {
        return ufbtoken;
    }

    public void setUfbtoken(String ufbtoken) {
        this.ufbtoken = ufbtoken;
    }
}
