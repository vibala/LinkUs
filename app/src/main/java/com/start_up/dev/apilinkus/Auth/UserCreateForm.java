package com.start_up.dev.apilinkus.Auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Vignesh on 11/8/2016.
 * This class will function as a data transfer object (DTO) between the application layer and service layer
 */
public class UserCreateForm {

    @JsonProperty("lastName")
    private String user_lastName;

    @JsonProperty("firstName")
    private String user_firstName;

    @JsonProperty("email")
    private String user_email;

    @JsonProperty("password")
    private String user_password;

    @JsonProperty("passwordRepeated")
    private String user_passwordRepeated;

    @JsonProperty("sexe")
    private String sexe;

    @JsonProperty("dateofBirth")
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date user_dateofBirth;

    final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public UserCreateForm(String ln, String fn, String email, String pwdv1, String pwdv2, String sexe, String dob){
        setUser_lastName(ln);
        setUser_firstName(fn);
        setUser_email(email);
        setUser_password(pwdv1);
        setUser_passwordRepeated(pwdv2);
        setSexe(sexe);
        try {
            setUser_dateofBirth(formatter.parse(dob));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public Date getUser_dateofBirth() {
        return user_dateofBirth;
    }

    public void setUser_dateofBirth(Date user_dateofBirth) {
        this.user_dateofBirth = user_dateofBirth;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_passwordRepeated() {
        return user_passwordRepeated;
    }

    public void setUser_passwordRepeated(String user_passwordRepeated) {
        this.user_passwordRepeated = user_passwordRepeated;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_firstName() {
        return user_firstName;
    }

    public void setUser_firstName(String user_firstName) {
        this.user_firstName = user_firstName;
    }

    public String getUser_lastName() {
        return user_lastName;
    }

    public void setUser_lastName(String user_lastName) {
        this.user_lastName = user_lastName;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }


}
