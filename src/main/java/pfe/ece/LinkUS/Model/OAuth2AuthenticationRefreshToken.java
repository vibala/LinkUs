package pfe.ece.LinkUS.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Vignesh on 12/8/2016.
 */
@Entity
@Table(name="oauth_refresh_token")
public class OAuth2AuthenticationRefreshToken{

    @Id
    @Column(name="token_id")
    private String tokenId;

    @Column(name="token")
    private String oAuth2RefreshToken;

    @Column(name="authentication")
    private String authentication;

    public OAuth2AuthenticationRefreshToken(String tokenId, String oAuth2RefreshToken, String authentication) {
        this.tokenId = tokenId;
        this.oAuth2RefreshToken = oAuth2RefreshToken;
        this.authentication = authentication;
    }

    /*public OAuth2AuthenticationRefreshToken(OAuth2RefreshToken oAuth2RefreshToken, OAuth2Authentication authentication) {
        this.oAuth2RefreshToken = oAuth2RefreshToken;
        this.authentication = authentication;
        this.tokenId = oAuth2RefreshToken.getValue();
    }*/

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public void setoAuth2RefreshToken(String oAuth2RefreshToken) {
        this.oAuth2RefreshToken = oAuth2RefreshToken;
    }

    public void setAuthentication(String authentication) {
        this.authentication = authentication;
    }

    public String getTokenId() {
        return tokenId;
    }

    public String getoAuth2RefreshToken() {
        return oAuth2RefreshToken;
    }

    public String getAuthentication() {
        return authentication;
    }
}
