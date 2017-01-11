package pfe.ece.LinkUS.Controller;


import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pfe.ece.LinkUS.Model.Message;

import java.util.Optional;

/**
 * Created by Vignesh on 11/3/2016.
 */

@Controller
public class LoginController {

    private static final Logger LOGGER = Logger.getLogger(LoginController.class);

    @RequestMapping(value = "/login", method= RequestMethod.GET)
    public ResponseEntity<Message> getLoginMessage(@RequestParam Optional<String> error){
        LOGGER.debug("Getting login page");
        Message msg = null;
        if(SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                // When anonymous authentication is enabled
                !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)){
            msg = new Message(100, "Login operation", "Sucessfully logged in");
        }
        msg = new Message(407, "Error", "The email or password you have entered is invalid, try again");
        return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);

    }

    @RequestMapping(value = "/logged", method = RequestMethod.GET)
    public ResponseEntity<Message> getUserId(){
        // Retrieve the id from the current user object
        //String id = ((CurrentUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        Message msg = new Message(100,"UserID","2");
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }
}
