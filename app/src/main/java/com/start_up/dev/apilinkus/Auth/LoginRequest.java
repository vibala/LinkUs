package com.start_up.dev.apilinkus.Auth;

/**
 * Created by Vignesh on 11/11/2016.
 */

public class LoginRequest {

    private String username;
    private String password;
    private String grant_type;
    private String scope;
    private String client_secret;
    private String client_id;

    public LoginRequest(String username, String password){
        this.username = username;
        this.password = password;
        this.client_id = "clientapp";
        this.client_secret = "123456";
        this.grant_type = "password";
        this.scope = "read write";
    }

    public LoginRequest(String username, String password,String grant_type, String scope, String client_secret, String client_id){
        this.username = username;
        this.password = password;
        this.client_id = client_id;
        this.client_secret = client_secret;
        this.grant_type = grant_type;
        this.scope = scope;
    }


    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getGrant_type() {
        return grant_type;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
