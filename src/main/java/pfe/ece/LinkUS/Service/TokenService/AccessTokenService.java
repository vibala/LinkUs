package pfe.ece.LinkUS.Service.TokenService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import pfe.ece.LinkUS.Exception.UserNotFoundException;
import pfe.ece.LinkUS.Model.User;
import pfe.ece.LinkUS.Service.UserEntityService.UserService;


/**
 * Created by Vignesh on 12/18/2016.
 */
@Service
public class AccessTokenService {

    @Autowired
    private UserService userService;

    private final Logger logger = Logger.getLogger(AccessTokenService.class);

    public String getUserIdOftheAuthentifiedUser() {

        String userId = getUserIdWithToken();
        if(userId == null) {
            throw new UserNotFoundException();
        }
        return userId;
    }

    public String getUserIdWithToken(){

        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = ((OAuth2Authentication) a).getUserAuthentication().getName();
        User user = userService.getUserByEmail(userEmail).
                orElseThrow(() -> new UsernameNotFoundException(String.format("User with email=%s was not found",userEmail)));

        return user.getId();
    }
}
