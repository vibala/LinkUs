package pfe.ece.linkus.Controller;

import com.mongodb.*;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import pfe.ece.LinkUS.Model.Message;
import pfe.ece.LinkUS.Model.TokenEntity;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.UserRepository;
import pfe.ece.LinkUS.Service.UserEntityService.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

//import pfe.ece.LinkUS.Service.UserService;

/**
 * Created by Vignesh on 12/19/2016.
 */
public class LoginTest {

    /***
     * We have already reagistered a test user (whose password is test and username is test@gmail.com) to execute our unitary tests in the collection "user" in the MongoDB
     */
    private static final String loginRequest = "http://localhost:9999/oauth/token";
    private static final String BASE_URL = "http://localhost:9999";
    private static final String username = "test@test.com";
    private static final String password = "test";
    private static final String grant_type = "password";
    private static final String scope = "read write";
    private static final String client_id = "clientapp";
    private static final String client_secret = "123456";
    private static final Logger LOGGER = Logger.getLogger(LoginTest.class);
    private static final MultiValueMap<String, String> mapProperties  = new LinkedMultiValueMap<>();
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;


    @Before
    public void onceExecutedBeforeAll() {
        //this.mockMVC = MockMvcBuilders.standaloneSetup(registerController).setValidator(userCreateFormValidator).build();
        MockitoAnnotations.initMocks(this);
        mapProperties.clear();
        mapProperties.add("username", username);
        mapProperties.add("password", password);
        mapProperties.add("grant_type", grant_type);
        mapProperties.add("scope", scope);
        mapProperties.add("client_id", client_id);
        mapProperties.add("client_secret", client_secret);
    }


    @Test
    public void testLoginProcessWithTheRightCredentialsButUserAccountIsDisabled(){
        Message m = executeLoginRequest("","");
        LOGGER.info(m.toString());
        assertEquals(400,m.getId());
        assertEquals("invalid_grant",m.getSubject());
        assertEquals("User is disabled",m.getText());
    }

    @Test
    public void testLoginProcessWithTheRightCredentialsButUserAccountIsActivated(){
        /*Simulation the action of updating the account */
        MongoClient mongoClient = new MongoClient("localhost",27017);
        DB db = mongoClient.getDB("mydb");
        DBCollection coll = db.getCollection("user");
        BulkWriteOperation builder = coll.initializeOrderedBulkOperation();
        builder.find(new BasicDBObject("email","test@test.com")).updateOne(new BasicDBObject("$set",new BasicDBObject("enabled",true)));
        builder.execute();
        /************************************************/
        /************************************************/
        /************************************************/
        Message m = executeLoginRequest("","");
        LOGGER.info(m.toString());
        assertEquals(100,m.getId());
        assertEquals("msg.NoException.Success",m.getSubject());
        assertNotNull(m.getText());
        // Parsing String to  Json
        assertEquals("access_token",m.getText().split(";")[0]);
        assertNotNull(m.getText().split(";")[1]);
        assertEquals("token_type",m.getText().split(";")[2]);
        assertEquals("bearer",m.getText().split(";")[3]);
        assertEquals("refresh_token",m.getText().split(";")[4]);
        assertNotNull(m.getText().split(";")[5]);
    }

    @Test
    public void testLoginProcessWithInvalidUsernameCredential() {
        Message m = executeLoginRequest("username","toto");
        LOGGER.info(m.toString());
        assertEquals(400,m.getId()); // 400 HTTP Code : Bad Request
        assertEquals("invalid_grant",m.getSubject());
        assertEquals("Bad credentials",m.getText());
    }

    @Test
    public void testLoginProcessWithInvalidPasswordCredential() {
        Message m = executeLoginRequest("password","toto");
        LOGGER.info(m.toString());
        assertEquals(400,m.getId()); // 400 HTTP Code : Bad Request
        assertEquals("invalid_grant",m.getSubject());
        assertEquals("Bad credentials",m.getText());
    }

    @Test
    public void testLoginProcessWithInvalidGrantTypeCredential() {
        Message m = executeLoginRequest("grant_type","access_token");
        LOGGER.info(m.toString());
        assertEquals(400,m.getId()); // 400 HTTP Code : Bad Request
        assertEquals("unsupported_grant_type",m.getSubject());
        assertEquals("Unsupported grant type: access_token",m.getText());
    }

    @Test
    public void testLoginProcessWithInvalidScopeCredential() {
        Message m = executeLoginRequest("scope","");
        LOGGER.info(m.toString());
        assertEquals(400,m.getId()); // 400 HTTP Code : Bad Request
        assertEquals("User is disabled",m.getSubject());
        assertEquals("Bad credentials",m.getText());
    }

    @Test
    public void testLoginProcessWithInvalidClientIDCredential() {
        Message m = executeLoginRequest("client_id","linkusapp");
        LOGGER.info(m.toString());
        assertEquals(401,m.getId()); // 400 HTTP Code : Bad Request
        assertEquals("unauthorized",m.getSubject());
        assertEquals("An authentification is required to access this resource",m.getText());
    }

    @Test
    public void testLoginProcessWithInvalidClientSecretCredential() {
        Message m = executeLoginRequest("client_secret","654321");
        LOGGER.info(m.toString());
        assertEquals(400,m.getId()); // 400 HTTP Code : Bad Request
        assertEquals("invalid_grant",m.getSubject());
        assertEquals("User is disabled",m.getText());
    }

    public Message executeLoginRequest(String key, String value) {

        Message message_to_be_returned = null;

        /*Adjust properties with the values sent in parameter*/
        if(mapProperties.get(key) != null && !mapProperties.get(key).isEmpty()){
            mapProperties.replace(key,new ArrayList<String>(){{add(value);}});
        }

        /*Small Http Request Client*/
        HttpHeaders requestHeaders = new HttpHeaders();
        final String authorization = "Basic "
                + new String(Base64Utils.encode("clientapp:123456".getBytes()));

        // Set the authorization value that sould be something like this Basic Y2xpZW50YXBwOjEyMzQ1Ng==
        requestHeaders.set("Authorization", authorization);
        // Set the content of the data sent to the server : in this case, we submit an user form
        requestHeaders.setContentType(APPLICATION_FORM_URLENCODED);
        // Set the form of the response we want to receive
        requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        // Put the properties in the param
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(mapProperties, requestHeaders);
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
            message_to_be_returned = new Message(100, "msg.NoException.Success", response.getBody().toString());
            return message_to_be_returned;
        } catch (HttpClientErrorException e) {
            LOGGER.info(e.getResponseBodyAsString());
            LOGGER.info(e.getRawStatusCode());
            LOGGER.info(e.getStatusText());
            LOGGER.info(e.getLocalizedMessage());
            switch(e.getRawStatusCode()){
                case 400:
                    JSONObject jsonObject = new JSONObject(e.getResponseBodyAsString());
                    message_to_be_returned = new Message(400,jsonObject.getString("error"), jsonObject.getString("error_description"));
                    break;
                case 401:
                    message_to_be_returned = new Message(401,"unauthorized", "An authentification is required to access this resource");
                    break;
            }



        } catch (ResourceAccessException e) {
            message_to_be_returned = new Message(417, "msg.ResourceAccessException.Failure", e.getMessage());
        } catch (Exception e) {
            message_to_be_returned = new Message(417, "msg.InvalidCredentialsException.Failure", e.getMessage());
        }

        return message_to_be_returned;
    }
}
