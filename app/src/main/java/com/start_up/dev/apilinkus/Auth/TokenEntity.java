package com.start_up.dev.apilinkus.Auth;

/**
 * Created by Vignesh on 11/11/2016.
 */

public class TokenEntity {

    private String access_token;
    private String token_type;
    private String refresh_token;
    private String expires_in;
    private String scope;


    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("");
        sb.append("access_token").append(";").append(getAccess_token()).append(";");
        sb.append("token_type").append(";").append(getToken_type()).append(";");
        sb.append("refresh_token").append(";").append(getRefresh_token()).append(";");
        sb.append("expires_in").append(";").append(getExpires_in()).append(";");
        sb.append("scope").append(";").append(getScope());

        return sb.toString();
    }
}