package pfe.ece.LinkUS.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Vignesh on 12/8/2016.
 */
@Entity
@Table(name= "oauth_access_token")
public class OAuth2AuthenticationAccessToken{

    @Id
    @Column(name="token_id")
    private String tokenId;

    @Column(name="token")
    private String oAuth2AccessToken;

    @Column(name="authentication_id")
    private String authenticationId;

    @Column(name="user_name")
    private String userName;

    @Column(name="client_id")
    private String clientId;

    @Column(name="authentication")
    private String authentication;

    @Column(name="refresh_token")
    private String refreshToken;

    public OAuth2AuthenticationAccessToken() {}

    public OAuth2AuthenticationAccessToken(String tokenId, String oAuth2AccessToken, String authenticationId, String userName, String clientId, String authentication, String refreshToken) {
        this.tokenId = tokenId;
        this.oAuth2AccessToken = oAuth2AccessToken;
        this.authenticationId = authenticationId;
        this.userName = userName;
        this.clientId = clientId;
        this.authentication = authentication;
        this.refreshToken = refreshToken;
    }

    /*public OAuth2AuthenticationAccessToken(final String oAuth2AccessToken, final String authentication,final String authenticationId) {
        this.tokenId = oAuth2AccessToken;
        this.oAuth2AccessToken = oAuth2AccessToken;
        this.authenticationId = authenticationId;
        // An oauth2 auth token can contain 2 authentications : one for the client and one for the user
        this.authentication = authentication;
        this.userName = authentication;
        // The authorization request containing details of the client application.
        this.clientId = authentication.getOAuth2Request().getClientId();
        this.refreshToken = oAuth2AccessToken.getRefreshToken().getValue();
    }*/

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public void setoAuth2AccessToken(String oAuth2AccessToken) {
        this.oAuth2AccessToken = oAuth2AccessToken;
    }

    public void setAuthenticationId(String authenticationId) {
        this.authenticationId = authenticationId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setAuthentication(String authentication) {
        this.authentication = authentication;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getTokenId() {
        return tokenId;
    }

    public String getoAuth2AccessToken() {
        return oAuth2AccessToken;
    }

    public String getAuthenticationId() {
        return authenticationId;
    }

    public String getUserName() {
        return userName;
    }

    public String getClientId() {
        return clientId;
    }

    public String getAuthentication() {
        return authentication;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
