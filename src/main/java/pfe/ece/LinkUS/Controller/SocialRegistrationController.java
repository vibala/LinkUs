package pfe.ece.LinkUS.Controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pfe.ece.LinkUS.Config.TwitterConfig;
import pfe.ece.LinkUS.Exception.EmailExistsException;
import pfe.ece.LinkUS.Model.Message;
import pfe.ece.LinkUS.Model.TokenEntity;
import pfe.ece.LinkUS.Model.UserCreateForm;
import pfe.ece.LinkUS.Service.UserEntityService.UserService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


/**
 * Created by Vignesh on 12/12/2016.
 */

@RestController
public class SocialRegistrationController {

    private static final Logger LOGGER = Logger.getLogger(UserController.class);

    //private final Facebook facebook;
    private UserService userService;
    private TwitterConfig twitterConfig;

    @Autowired
    public SocialRegistrationController(UserService userService,TwitterConfig twitterConfig) {
        this.userService = userService;
        this.twitterConfig = twitterConfig;
    }

    @RequestMapping(value = "/facebook/login", method = RequestMethod.POST)
    public ResponseEntity<Message> getHomePageFromAFacebookUser(@RequestParam("token") String token, RedirectAttributes redirectAttributes) {
        LOGGER.info("SocialRegistrationController - getHomePageFromAFacebookUser");
        Message homemsg;

        if (token != null && !token.isEmpty()) {

            LOGGER.info("Access_token = " + token);
            String msg_personalise = "";
            String access_token = token;
            Facebook facebook = new FacebookTemplate(access_token);
            String[] fields = {"id", "email", "first_name", "last_name", "birthday", "gender"};
            User userProfile = facebook.fetchObject("me", User.class, fields);
            //  Check si l'user existe déjà dans la base
            if (!userService.getUserByEmail(userProfile.getEmail()).isPresent()) {
                UserCreateForm user = new UserCreateForm();
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                user.setFirstName(userProfile.getFirstName());
                user.setLastName(userProfile.getLastName());
                LOGGER.info("User email = " + userProfile.getEmail());
                LOGGER.info("User bday = " + userProfile.getBirthday());
                try {
                    user.setDateofBirth(formatter.parse("00/00/0000"));
                    user.setEmail(userProfile.getEmail());
                    user.setSexe(userProfile.getGender());
                    String password = UUID.randomUUID().toString();
                    user.setPassword(password);
                    user.setPasswordRepeated(password);
                    userService.registerNewUserAccount(user);
                    userService.saveRegisteredUser(userService.getUserByEmail(user.getEmail()).get());
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (EmailExistsException e) {
                    e.printStackTrace();
                }

                LOGGER.info("User email = " + userProfile.getEmail() + " User id " + userProfile.getId());
                msg_personalise = "Bienvenue à LinkusApp";

            } else {
                LOGGER.info("User already stored in DB");
                msg_personalise = "Content de vous revoir sur LinkusApp";
            }


            /*Small Http Request Cclient*/
            HttpHeaders requestHeaders = new HttpHeaders();
            final String authorization = "Basic "
                    + new String(Base64Utils.encode("clientapp:123456".getBytes()));

            requestHeaders.set("Authorization", authorization);
            requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            // create form parameters as a MultiValueMap
            final MultiValueMap<String, String> formVars = new LinkedMultiValueMap<>();
            formVars.add("username", userProfile.getEmail());
            formVars.add("password", userProfile.getFirstName() + "" + userProfile.getLastName());
            formVars.add("grant_type", "password");
            formVars.add("scope", "read write");
            formVars.add("client_secret", "123456");
            formVars.add("client_id", "clientapp");

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(formVars, requestHeaders);

            // Create a new RestTemplate instance [RestTemplate is a Spring rest client]
            RestTemplate restTemplate = new RestTemplate();
            // Create a list for the message converters
            List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
            // Add the Form Message converter
            messageConverters.add(new FormHttpMessageConverter());
            // Add the Jackson Message converter
            messageConverters.add(new MappingJackson2HttpMessageConverter());
            // Add the message converters to the restTemplate
            restTemplate.setMessageConverters(messageConverters);

            try {
                // Make the network requesta
                LOGGER.info("restTemplate EXCHANGE ! !!!!!!!!! User email = " + userProfile.getEmail() + " User id " + userProfile.getId());
                ResponseEntity<TokenEntity> response = restTemplate.exchange("http://localhost:9999/oauth/token", HttpMethod.POST, request, TokenEntity.class);
                homemsg = new Message(100, msg_personalise, response.getBody().toString());
                return new ResponseEntity<Message>(homemsg, HttpStatus.OK);

            } catch (HttpClientErrorException e) {
                LOGGER.info("HttpClientErrorException ! !!!!!!!!! User email = " + userProfile.getEmail() + " User id " + userProfile.getId());
                String message = new String("Error : A httpClientErrorException has been thrown");
                homemsg = new Message(e.getStatusCode().value(), "msg.Exception", message);
                return new ResponseEntity<Message>(homemsg, HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (ResourceAccessException e) {
                String message = new String("Error : You don't have access to this resource");
                homemsg = new Message(417, "msg.Exception", message);
                return new ResponseEntity<Message>(homemsg, HttpStatus.INTERNAL_SERVER_ERROR);


            }

        } else {
            // Implementation de l'accès à Facebook via les connectionHandler (2ème facon apres facebookTemplate)
        }

        homemsg = new Message(417, "Message de bienvenue", "Abscence de token");
        return new ResponseEntity<Message>(homemsg, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/twitter/login", method = RequestMethod.POST)
    public ResponseEntity<Message> getHomePageFromATwitterUser(@RequestParam("auth_token") String auth_token, @RequestParam("auth_secret") String auth_secret, RedirectAttributes redirectAttributes) {
        LOGGER.info("UserController - getHomePageFromATwitterUser");
        Message homemsg;

        if (auth_token != null && !auth_token.isEmpty() && auth_secret != null && !auth_secret.isEmpty()) {
            String consumerKey = twitterConfig.getConsumerKey();
            String consumerSecret = twitterConfig.getConsumerSecret();

            String accessToken = auth_token;
            String accessTokenSecret = auth_secret;
            String msg_personalise;

            LOGGER.info("Consumer key = " + consumerKey);
            LOGGER.info("Consumer secret = " + consumerSecret);

            TwitterTemplate twitter = new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);
            org.springframework.social.twitter.api.UserOperations userOperations = twitter.userOperations();
            LOGGER.info("Twitter username : " + userOperations.getUserProfile().getName());
            //RestTemplate restTemplate = twitter.getRestTemplate();
            //String response = restTemplate.getForObject("https://api.twitter.com/1.1/account/verify_credentials.json?include_email=true",String.class);
            //LOGGER.info("User info email : " + response);
            if(!userService.getUserByEmail(userOperations.getUserProfile().getName().concat("@twitter.com")).isPresent()){
                // Cet user n'est pas présent dans la base
                UserCreateForm usercreateform = new UserCreateForm();
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

                try {
                    usercreateform.setDateofBirth(formatter.parse("00/00/0000"));
                    usercreateform.setEmail(userOperations.getUserProfile().getName().concat("@twitter.com"));
                    usercreateform.setFirstName(userOperations.getUserProfile().getName());
                    usercreateform.setLastName(userOperations.getUserProfile().getName());
                    usercreateform.setSexe("XXXX");
                    String password = UUID.randomUUID().toString();
                    usercreateform.setPassword(password);
                    usercreateform.setPasswordRepeated(password);
                    //usercreateform.setPassword(String.valueOf(userOperations.getProfileId()).concat(userOperations.getUserProfile().getName()));
                    //usercreateform.setPasswordRepeated(String.valueOf(userOperations.getProfileId()).concat(userOperations.getUserProfile().getName()));
                    userService.registerNewUserAccount(usercreateform);
                    userService.saveRegisteredUser(userService.getUserByEmail(usercreateform.getEmail()).get());
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (EmailExistsException e) {
                    e.printStackTrace();
                }

                msg_personalise = "Bienvenue à LinkusApp";
            }else{
                msg_personalise = "Content de vous revoir sur LinkusApp";
            }


            /***************** HTTP CLIENT IMPL*/
             /*Small Http Request Cclient*/
            HttpHeaders requestHeaders = new HttpHeaders();
            final String authorization = "Basic "
                    + new String(Base64Utils.encode("clientapp:123456".getBytes()));

            requestHeaders.set("Authorization", authorization);
            requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            // create form parameters as a MultiValueMap
            final MultiValueMap<String, String> formVars = new LinkedMultiValueMap<>();
            formVars.add("username", userOperations.getUserProfile().getName().concat("@twitter.com"));
            formVars.add("password", String.valueOf(userOperations.getProfileId()).concat(userOperations.getUserProfile().getName()));
            formVars.add("grant_type", "password");
            formVars.add("scope", "read write");
            formVars.add("client_secret", "123456");
            formVars.add("client_id", "clientapp");

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(formVars, requestHeaders);
            // Create a new RestTemplate instance [RestTemplate is a Spring rest client]
            RestTemplate restTemplate = new RestTemplate();
            // Create a list for the message converters
            List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
            // Add the Form Message converter
            messageConverters.add(new FormHttpMessageConverter());
            // Add the Jackson Message converter
            messageConverters.add(new MappingJackson2HttpMessageConverter());
            // Add the message converters to the restTemplate
            restTemplate.setMessageConverters(messageConverters);

            try {
                ResponseEntity<TokenEntity> response = restTemplate.exchange("http://localhost:9999/oauth/token", HttpMethod.POST, request, TokenEntity.class);
                homemsg = new Message(100, msg_personalise, response.getBody().toString());
                return new ResponseEntity<Message>(homemsg, HttpStatus.OK);

            } catch (HttpClientErrorException e) {
                String message = new String("Error : A httpClientErrorException has been thrown");
                homemsg = new Message(e.getStatusCode().value(), "msg.Exception", message);
                return new ResponseEntity<Message>(homemsg, HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (ResourceAccessException e) {
                String message = new String("Error : You don't have access to this resource");
                homemsg = new Message(417, "msg.Exception", message);
                return new ResponseEntity<Message>(homemsg, HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }else{
            // Implementez la connection handler
        }

        homemsg = new Message(417, "Message de bienvenue", "Abscence de token");
        return new ResponseEntity<Message>(homemsg, HttpStatus.BAD_REQUEST);


    }

    /**
     * @TODO : Finir d'impléménter l'authentification avec Google
     */
    /*@RequestMapping(value = "/google/login", method = RequestMethod.POST)
    public ResponseEntity<Message> getHomePageFromAGoogleUser(@RequestParam("token") String idToken){
        // idToken comes from the client app (shown above)
        FirebaseOptions options = null;
        System.out.println("getHomePageFromAGoogleUser - I " + idToken);
        try {
            options = new FirebaseOptions.Builder()
                    .setServiceAccount(
                            new FileInputStream("linkUS-42b42-firebase-adminsdk-l2ld3-41684b1ba5.json"))
                    .setDatabaseUrl("https://linkus-42b42.firebaseio.com")
                    .build();
            System.out.println("getHomePageFromAGoogleUser - II");
            // Initialize the default app
            FirebaseApp defaultApp = FirebaseApp.initializeApp(options);
            System.out.println("getHomePageFromAGoogleUser - III" + defaultApp.getName());

            // Retrieve services by passing the defaultApp variable...
            FirebaseAuth defaultAuth = FirebaseAuth.getInstance(defaultApp);
            System.out.println("getHomePageFromAGoogleUser - IV" + defaultAuth.toString());
            FirebaseDatabase defaultDatabase = getInstance(defaultApp);
            System.out.println("getHomePageFromAGoogleUser - V " + defaultDatabase.toString() );

            if (StringUtils.isBlank(idToken)) {System.out.println("ttttttttttttttttttttt");
                throw new IllegalArgumentException("FirebaseTokenBlank");
            }

            try {

                Task<FirebaseToken> authTask =
                        defaultAuth.verifyIdToken(idToken)
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        System.out.println("Exception : " + e.getMessage());
                                    }
                                })
                                .addOnSuccessListener(new OnSuccessListener<FirebaseToken>() {
                                       @Override
                                       public void onSuccess(FirebaseToken decodedToken) {
                                             String uid = decodedToken.getUid();
                                             String uemail = decodedToken.getEmail();
                                             DatabaseReference  mDatabase = defaultDatabase.getReference();
                                             System.out.println("Userid " + uid + " User email " + uemail);
                                             mDatabase.child("user_details").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                               @Override
                                               public void onDataChange(DataSnapshot dataSnapshot) {
                                                   String name = null, surname = null, email = null, phone = null, bd = null, gender = null;
                                                   System.out.println("test1 " + dataSnapshot.getChildrenCount());
                                                   System.out.println("test1 " + dataSnapshot.);
                                                   for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                       if (child.getKey().equals("firstName")) {
                                                           name = child.getValue().toString();
                                                           System.out.println("Name : " + name);
                                                       }
                                                       if (child.getKey().equals("lastName")) {
                                                           surname = child.getValue().toString();
                                                           System.out.println("Surname " + surname);
                                                       }
                                                       if (child.getKey().equals("birthday")) {
                                                           bd = child.getValue().toString();
                                                           System.out.println("Birthday " + bd);
                                                       }
                                                       if (child.getKey().equals("email")) {
                                                           email = child.getValue().toString();
                                                       }
                                                       if (child.getKey().equals("gender")) {
                                                           gender = child.getValue().toString();
                                                       }
                                                       if (child.getKey().equals("phone")) {
                                                           phone = child.getValue().toString();
                                                       }
                                                   }

                                                   System.out.println("test2");
                                               }


                                               @Override
                                               public void onCancelled(DatabaseError databaseError) {
                                               }
                                           });
                                       }
                                });

                Tasks.await(authTask);
            } catch(ExecutionException | InterruptedException e ){
                throw new FirebaseTokenInvalidException(e.getMessage());
            }


        } catch (FileNotFoundException e) {
           e.printStackTrace();
        }




        return null;
    }*/
}

