package pfe.ece.LinkUS.Controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

/**
 * Created by Vignesh on 12/18/2016.
 */
@RestController
public class LogoutController {

    private TokenStore tokenStore;
    private final Logger logger = Logger.getLogger(LogoutController.class);

    /**
     * Contrôleur
     * @param tokenStore
     */
    @Autowired
    public LogoutController(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    /**
     * Cette méthode intercepte la requête de déconnexion et l'exécute (déletion de l'access token et du refresh token)
     * @param request
     */
    @RequestMapping(value = "/oauth/revoke-token", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void logout(WebRequest request) {
        logger.debug("LOGOUT -  I");
        String authHeader = request.getHeader("Authorization");
        logger.debug("LOGOUT -  authHeader : " + authHeader);
        if (authHeader != null) {
            String tokenValue = authHeader.replace("Bearer", "").trim();
            logger.debug("LOGOUT -  tokenValue : " + tokenValue);
            logger.debug("LOGOUT -  accessToken : " + tokenStore.readAccessToken(tokenValue).getValue());
            OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
            OAuth2RefreshToken refreshToken = accessToken.getRefreshToken();
            logger.debug("LOGOUT - accessToken : after");
            tokenStore.removeAccessToken(accessToken);
            tokenStore.removeRefreshToken(refreshToken);
            logger.debug("LOGOUT - tokenStore : end");
        }
    }
}
