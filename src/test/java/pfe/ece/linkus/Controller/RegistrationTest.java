package pfe.ece.linkUS.Controller;


import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import pfe.ece.LinkUS.Model.Message;
import pfe.ece.LinkUS.Model.UserCreateForm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * Created by Vignesh on 12/19/2016.
 */
@Ignore
public class RegistrationTest {

    private static final String REGISTRATION_URL = "http://localhost:9999/user/registration";
    private static final Logger LOGGER = Logger.getLogger(LoginTest.class);

    @Test
    public void testRegisterANewUser() throws Exception{

        /*Création du formulaire*/
        UserCreateForm form = new UserCreateForm();
        form.setFirstName("test");
        form.setLastName("test");
        form.setEmail("test@test.com");
        form.setPassword("test");
        form.setPasswordRepeated("test");
        form.setSexe("Male");
        form.setDateofBirth(new Date());

        Message message_returned_by_executeRegistratationRequest = executeRegistrationRequest(form);
        assertEquals(200,message_returned_by_executeRegistratationRequest.getId());
        assertEquals("message.regSucc",message_returned_by_executeRegistratationRequest.getSubject());
        assertEquals("Hello we need to verify your mail "+ form.getEmail() +" for the Linkus account",message_returned_by_executeRegistratationRequest.getText());

    }

    @Test
    public void testRegisterAnExistingUser() throws Exception{

        /*Création du formulaire*/
        UserCreateForm form = new UserCreateForm();
        form.setFirstName("test");
        form.setLastName("test");
        form.setEmail("test@test.com");
        form.setPassword("test");
        form.setPasswordRepeated("test");
        form.setSexe("Male");
        form.setDateofBirth(new Date());

        Message message_returned_by_executeRegistratationRequest = executeRegistrationRequest(form);
        assertEquals(409,message_returned_by_executeRegistratationRequest.getId());
        assertEquals("email.exists",message_returned_by_executeRegistratationRequest.getSubject());
        assertEquals("User with this email already exists",message_returned_by_executeRegistratationRequest.getText());

    }


    public Message executeRegistrationRequest(UserCreateForm form) {

        Message message_to_be_returned = null;

        /*Small Http Request Client*/
        HttpHeaders requestHeaders = new HttpHeaders();
        final String authorization = "Basic "
                + new String(Base64Utils.encode("clientapp:123456".getBytes()));

        // Set the authorization value that sould be something like this Basic Y2xpZW50YXBwOjEyMzQ1Ng==
        requestHeaders.set("Authorization", authorization);
        // Set the content of the data sent to the server : in this case, we submit an user form
        requestHeaders.setContentType(APPLICATION_JSON);
        // Set the form of the response we want to receive
        requestHeaders.setAccept(Collections.singletonList(APPLICATION_JSON));
        // Put the properties in the param
        HttpEntity<UserCreateForm> request = new HttpEntity<UserCreateForm>(form, requestHeaders);
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
            ResponseEntity<Message> response = restTemplate.exchange(REGISTRATION_URL, HttpMethod.POST, request, Message.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            LOGGER.info(e.getResponseBodyAsString());
            LOGGER.info(e.getRawStatusCode());
            LOGGER.info(e.getStatusText());

            switch(e.getRawStatusCode()){
                case 400:
                    JSONObject jsonObject = new JSONObject(e.getResponseBodyAsString());
                    message_to_be_returned = new Message(400,jsonObject.getString("error"), jsonObject.getString("error_description"));
                    break;
                case 401:
                    message_to_be_returned = new Message(401,"unauthorized", "An authentification is required to access this resource");
                    break;
                case 409:
                    JSONObject object = new JSONObject(e.getResponseBodyAsString());
                    message_to_be_returned = new Message(409,object.getString("subject"),object.getString("text"));
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
