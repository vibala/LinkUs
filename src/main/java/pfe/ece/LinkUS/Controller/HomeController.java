package pfe.ece.LinkUS.Controller;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import pfe.ece.LinkUS.Exception.EmailExistsException;
import pfe.ece.LinkUS.Model.Message;
import pfe.ece.LinkUS.Model.User;
import pfe.ece.LinkUS.Model.UserCreateForm;
import pfe.ece.LinkUS.Model.Validator.UserCreateFormValidator;
import pfe.ece.LinkUS.Service.UserEntityService.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * Created by Vignesh on 11/6/2016.
 */
@Controller
public class HomeController {

    private final Logger LOGGER = Logger.getLogger(HomeController.class);
    private final UserService userService;
    private final UserCreateFormValidator userCreateFormValidator;

    @Autowired
    public HomeController(UserService userService, UserCreateFormValidator userCreateFormValidator) {
        this.userService = userService;
        this.userCreateFormValidator = userCreateFormValidator;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(userCreateFormValidator);
    }


    @RequestMapping(value="/user", method=RequestMethod.POST)
    public ResponseEntity<Void> createUser(@RequestBody @Valid UserCreateForm form, BindingResult bindingResult, UriComponentsBuilder ucb){
        LOGGER.info("UserController - createUser");
        List<Message> messages = new ArrayList<Message>();

        if(form==null){
            LOGGER.error("User is null");
        } else {
            // Email en lowercase toujours
            form.setEmail(form.getEmail().toLowerCase());
        }

        // contents as before
        if (bindingResult.hasErrors()) {
            List<ObjectError> errorsValidation = bindingResult.getAllErrors();
            for (ObjectError error: errorsValidation) {
                // Code 417 : Exception failed
                messages.add(new Message(417,error.getCode(),error.getDefaultMessage()));
            }

            return new ResponseEntity(messages, HttpStatus.CONFLICT);
        }

        try {
            createUserAccount(form);
            HttpHeaders headers = new HttpHeaders();
            headers.add("1", "uno");
            Optional<User> user = userService.getUserByEmail(form.getEmail());
            headers.setLocation(ucb.path("/user/{id}").buildAndExpand(user.get().getId()).toUri());

        } catch (DataIntegrityViolationException e) {
            bindingResult.reject("email.exists", "Email already exists");
            messages.add(new Message(417,bindingResult.getAllErrors().get(0).getCode(),bindingResult.getAllErrors().get(0).getDefaultMessage()));
            return new ResponseEntity(messages,HttpStatus.CONFLICT);
        }

        return new ResponseEntity(new Message(100, "Congratulations!", "You have accessed a Basic Auth protected resource."),HttpStatus.CREATED);
    }

    public User createUserAccount(UserCreateForm accountDTO){

        User registered = null;
        try{
            userService.registerNewUserAccount(accountDTO);
        }catch(EmailExistsException e){
            return null;
        }

        return registered;
    }

    @RequestMapping(value="/home", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Message> getHomePage(){
        LOGGER.info("UserController - getHomePage");
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = ((OAuth2Authentication) a).getUserAuthentication().getName().toLowerCase();
        User user = userService.getUserByEmail(userEmail).
                orElseThrow(() -> new UsernameNotFoundException(String.format("User with email=%s was not found: ", userEmail)));


        Message homemsg = new Message(100,"Message de bienvenue","Bienvenue " + user.getFirstName() + "[ID: " + user.getId() + "] à notre application");
        return new ResponseEntity<Message>(homemsg,HttpStatus.FOUND);
    }

    @RequestMapping(value="/getmessagedebienvenue",method = RequestMethod.GET,produces = "application/json")
    public @ResponseBody Message getMessageDeBienvenue(){
        LOGGER.info("Accessing protected resource");
        return new Message(100, "Congratulations!", "You have accessed a Basic Auth protected resource.");
    }
}
