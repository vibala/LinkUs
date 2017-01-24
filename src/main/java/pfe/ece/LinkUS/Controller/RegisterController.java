package pfe.ece.LinkUS.Controller;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import pfe.ece.LinkUS.Exception.EmailExistsException;
import pfe.ece.LinkUS.Model.*;
import pfe.ece.LinkUS.Model.Validator.UserCreateFormValidator;
import pfe.ece.LinkUS.Service.AlbumService;
import pfe.ece.LinkUS.Service.SubscriptionService;
import pfe.ece.LinkUS.Service.TokenService.VerificationTokenService;
import pfe.ece.LinkUS.Service.UserEntityService.UserService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;

/**
 * Created by Vignesh on 12/9/2016.
 */
@Controller
public class RegisterController {

    @Autowired
    ApplicationEventPublisher eventPublisher;
    @Autowired
    private UserService userService;
    @Autowired
    private  UserCreateFormValidator userCreateFormValidator;
    @Autowired
    private  VerificationTokenService verificationTokenService;
    @Autowired
    private  AlbumService albumService;
    @Autowired
    private SubscriptionService subscriptionService;

    private static final Logger LOGGER = Logger.getLogger(RegisterController.class);

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(userCreateFormValidator);
    }

    @RequestMapping(value= "/user/registration",method= RequestMethod.POST)
    public ResponseEntity<Void> registerUserAccount(@RequestBody @Valid UserCreateForm form, BindingResult bindingResult, WebRequest request){
        LOGGER.info("UserController - createUser");
        List<Message> messages = new ArrayList<Message>();
        String username = "";

        if(form==null){
            LOGGER.error("User is null");
        }

        // contents as before
        if (bindingResult.hasErrors()) {
            List<ObjectError> errorsValidation = bindingResult.getAllErrors();
            for (ObjectError error: errorsValidation) {
                LOGGER.error("Error1 : " + error.getObjectName());
                LOGGER.error("Error2: " + error.toString());
                // Code 417 : Exception failed
                messages.add(new Message(417,error.getCode(),error.getDefaultMessage()));
            }

            return new ResponseEntity(messages.get(0), HttpStatus.CONFLICT);
        }

        try{

            username = form.getEmail();

            String appUrl = request.getContextPath();

            if((userService.getUserByEmail(username).isPresent() && userService.getUserByEmail(username).get().isEnabled())
                    || (userService.getUserByEmail(username).isPresent() &&
                    !userService.getUserByEmail(username).get().isEnabled() && verificationTokenService.existsTokenAssociatedToUsername(username))){
                messages.add(new Message(417,"msg.Failure","A user with this email address already exists in the DB"));
                return new ResponseEntity(messages.get(0), HttpStatus.CONFLICT);

            }else{
                eventPublisher.publishEvent(new OnRegistrationCompleteEvent(username,request.getLocale(),appUrl,1));
            }
        }catch(MailSendException e ){
            messages.add(new Message(417,"msg.Failure",e.getMessage()));
            return new ResponseEntity(messages.get(0), HttpStatus.CONFLICT);
        }catch(Exception e){
            messages.add(new Message(417,"msg.Failure",e.toString()));
            return new ResponseEntity(messages.get(0), HttpStatus.CONFLICT);
        }

        User registeredUser = createUserAccount(form);
        LOGGER.info("confirmRegistration (Album create) - STEP II - Album creation");
        albumService.createAlbumForNewRegisteredUser(registeredUser.getId());
        LOGGER.info("confirmRegistration (Album create) - STEP III - Subscriptions creation");
        subscriptionService.addUserToAllSubscription(registeredUser);

        return new ResponseEntity(new Message(200, "message.regSucc", "Hello we need to verify your mail " + username + " for the Linkus account"),HttpStatus.CREATED);
    }

    /*Retourne une page HTML au client lorsque ce dernier clique sur l'url présent dans le mail de confirmation*/
    @RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
    public ModelAndView confirmRegistration(HttpServletRequest arg0, HttpServletResponse arg1,WebRequest request,@RequestParam("token") String token){
        LOGGER.info("confirmRegistration - STEP I");
        VerificationToken verificationToken = verificationTokenService.getVerificationToken(token);
        if(verificationToken==null){
            MessageTypeMailConfirmation m = new MessageTypeMailConfirmation(417,"message.auth.invalidToken","Invalid Verification Token",false);
            return new ModelAndView("confirmationMessage","messages_confirmation", Arrays.asList(m));
        }

        Calendar cal = Calendar.getInstance();
        if((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0){
            MessageTypeMailConfirmation m = new MessageTypeMailConfirmation(417,"message.auth.expiredToken","Token expired! You need to repeat the registration process",false);
            return new ModelAndView("confirmationMessage","messages_confirmation", Arrays.asList(m));
        }

        String username = verificationToken.getUsername();
        Optional<User> user = userService.getUserByEmail(username);
        if(!user.isPresent()){
            MessageTypeMailConfirmation m = new MessageTypeMailConfirmation(417,"message.auth.internalError","No emailadress is attached to this token",false);
            return new ModelAndView("confirmationMessage","messages_confirmation", Arrays.asList(m));
        }

        User realUser = user.get();
        if(realUser.isEnabled()){
            MessageTypeMailConfirmation m = new MessageTypeMailConfirmation(417,"message.auth.invalidToken","Email confirmation already done",false);
            return new ModelAndView("confirmationMessage","messages_confirmation", Arrays.asList(m));

        }

        LOGGER.info("From ConfirmationMailStep2 in RgisterController:" + realUser.getEmail());
        String appUrl = request.getContextPath();
        realUser.setEnabled(true);
        userService.saveRegisteredUser(realUser); // Set enabled := true
        verificationTokenService.deleteVerificationToken(token);
        MessageTypeMailConfirmation m = new MessageTypeMailConfirmation(100,"message.regSucc","Registration process completed ! You can login to the app",true);
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(realUser.getEmail(),request.getLocale(),appUrl,2));

        return new ModelAndView("confirmationMessage","messages_confirmation", Arrays.asList(m));
    }

    public User createUserAccount(UserCreateForm accountDTO){
        User registered = null;
        try{
            registered = userService.registerNewUserAccount(accountDTO);
            LOGGER.info("User is registered");
            return registered;
        }catch(EmailExistsException e){
            LOGGER.info("User is not registered");
            return null;
        }
    }
}
