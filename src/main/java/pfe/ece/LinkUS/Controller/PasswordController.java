package pfe.ece.LinkUS.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import pfe.ece.LinkUS.Component.OnSendingCompleteEvent;
import pfe.ece.LinkUS.Exception.UpdatePasswordException;
import pfe.ece.LinkUS.Model.Message;
import pfe.ece.LinkUS.Model.User;
import pfe.ece.LinkUS.Model.VerificationToken;
import pfe.ece.LinkUS.Service.TokenService.AccessTokenService;
import pfe.ece.LinkUS.Service.TokenService.VerificationTokenService;
import pfe.ece.LinkUS.Service.UserEntityService.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Created by Vignesh on 12/30/2016.
 */
@RestController
public class PasswordController {

    private Logger LOGGER = Logger.getLogger("LinkUS.Controller.PasswordController");

    @Autowired
    private  AccessTokenService accessTokenService;
    @Autowired
    private UserService userService;
    @Autowired
    private VerificationTokenService verificationTokenService;
    @Autowired
    ApplicationEventPublisher eventPublisher;


    @RequestMapping(value = "/forgotPassword",method = RequestMethod.POST)
    public ResponseEntity<Message> changePasswordFirstStep(@RequestBody String mail,HttpServletRequest request){
        mail=mail.replace("\"","");

        // Message to be dispatched to be sent back
        Message message = null;

        // Check if mail exists in the DB
        Optional<User> user = userService.getUserByEmail(mail);

        if(!user.isPresent() || user.get() == null || user.get().getId() == null){
            message = new Message(400,"check.Email","Invalid email adress");
            return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
        }

        // Mail exists in the DB
        String[] recipient_adresses = new String[1];
        recipient_adresses[0] = user.get().getEmail();

        // Generate a token
        String token = UUID.randomUUID().toString();
        verificationTokenService.createVerificationToken(token,user.get().getEmail(),"PASSWORD");


        // Setting the confirmation URL
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int portNumber = request.getServerPort();
        String contextPath = request.getContextPath();
        String confirmationURL = scheme + "://" + serverName + ":" + portNumber + contextPath + "/setNewPassword?token="+token;

        // Setting the body text
        String bodyText = "We heard that you lost your Linkus password\n" +
                "Sorry about that!"+
                "But don't worry! You can use the following link within the next hour to reset your password: "+
                confirmationURL;

        eventPublisher.publishEvent(new OnSendingCompleteEvent(this,recipient_adresses,"Please reset your password",bodyText));
        message = new Message(200,"check.Email","A mail has been sent to you in order to reset your password");
        return new ResponseEntity<>(message,HttpStatus.OK);
    }

    @RequestMapping(value = "/setNewPassword", method = RequestMethod.GET)
    public String setNewPassword(@RequestParam("token") String verification_token_param, Model model){
        VerificationToken verificationToken = verificationTokenService.getVerificationToken(verification_token_param);
        if(verificationToken == null){
            // Tentative d'usurpation d'identité)
            LOGGER.warning("Tentative d'usurpation d'identité");
            return "tokenUnknown";
        }else{
            boolean token_expired = verificationToken.check_token_is_expired();
            if(token_expired){
                // TOKEN _EXPIRE
                LOGGER.info("Token expiré --- " + "Expiry date " + verificationToken.getExpiryDate().toString());
                model.addAttribute("expiryDate",verificationToken.getExpiryDate().toString()); // Input Hidden in HTML
                return "tokenExpired";
            }
        }

        String username = verificationToken.getUsername(); // Retrieving username
        String expiryDate = verificationToken.getExpiryDate().toString(); // Retrieving expirateDate
        model.addAttribute("expiryDate",expiryDate); // Input Hidden in HTML
        model.addAttribute("token",verification_token_param); // Input Hidden in HTML
        return "resetPassword";
    }

    // Hint : http://stackoverflow.com/questions/33731070/spring-mvc-requestparam-causing-missingservletrequestparameterexception-with/33732140#33732140
    // Hint : http://stackoverflow.com/questions/33796218/content-type-application-x-www-form-urlencodedcharset-utf-8-not-supported-for
    @RequestMapping(value = "/saveNewpassword", method = RequestMethod.POST,consumes = {"application/x-www-form-urlencoded"})
    public String changePasswordSecondStep(@RequestBody MultiValueMap params, Model model) throws UpdatePasswordException {

        String param1 = params.get("new_password").toString();
        String param2 = params.get("conf_password").toString();
        // String param3 = params.get("username").toString();
        String param4 = params.get("token").toString();

        String password1 = param1.substring(1,param1.length()-1);
        String password2 = param2.substring(1,param2.length()-1);
        String token = param4.substring(1,param4.length()-1);
        String username = verificationTokenService.getVerificationToken(token).getUsername();

        //Message msg = null;

        if(password1.contentEquals(password2)){
            // OK
           Optional<User> user = userService.getUserByEmail(username);
            if(user.isPresent() && user.get() != null){
                // Saving the new password in the database
                userService.saveNewPassword(user.get(),password1);
                // Deleting the token sent to client
                verificationTokenService.deleteVerificationToken(token);
                //msg = new Message(200,"update.Password","Password is updated");
                return "passwordChangeSuccessful";
            }else {
                model.addAttribute("message","Password is not updated due to internal server error !");
                //throw new UpdatePasswordException("update.Password", "Password is not updated due to internal server error");
            }
        }else{
            // KO
            model.addAttribute("message","Password is not updated due to the fact that the 2 passwords are not similar !");
            //throw new UpdatePasswordException("update.Password", "Password is not updated due to the fact that the 2 passwords are not similar");
        }

        //return new ResponseEntity<Message>(msg, HttpStatus.OK);
        return "passwordChangeFailed";

    }
}
