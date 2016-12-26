package pfe.ece.LinkUS.Config;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.context.request.NativeWebRequest;
import pfe.ece.LinkUS.Model.User;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by Vignesh on 12/12/2016.
 */

/***
 * Base class for the implementation of the adapter that bridges between the Spring MVC Controller which handles the provider user sign-in flo and
 * the application-specific user sign-in service.
 */
public final class SimpleSignInAdapter implements SignInAdapter {

    private final UserCookieGenerator userCookieGenerator = new UserCookieGenerator();

    /***
     * Set the sign in process while using the social network sign in
     * @param userId
     * @param connection
     * @param request
     * @return
     */
    public String signIn(String userId, Connection<?> connection, NativeWebRequest request) {
        SecurityContext.setCurrentUser(new User(userId));
        userCookieGenerator.addCookie(userId, request.getNativeResponse(HttpServletResponse.class));
        return null;
    }

}
