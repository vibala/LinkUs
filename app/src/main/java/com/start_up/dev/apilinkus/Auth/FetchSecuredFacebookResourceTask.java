package com.start_up.dev.apilinkus.Auth;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.facebook.login.LoginManager;
import com.start_up.dev.apilinkus.API.APILinkUS;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * Created by Vignesh on 12/12/2016.
 */

public class FetchSecuredFacebookResourceTask extends AsyncTask<String,Void,String> {

    private AsyncResponse delegate;
    private Context mContext;
    private static final String
            TAG = FetchSecuredFacebookResourceTask.class.getSimpleName();
    public FetchSecuredFacebookResourceTask(Context mContext,AsyncResponse asyncResponse){
        this.delegate = asyncResponse;
        this.mContext = mContext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... tokens) {
        final String url = APILinkUS.BASE_URL + "/facebook/login?token=" + tokens[0];
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaa"+url);
        // Create the request header
        HttpHeaders requestHeaders = new HttpHeaders();
        // Set the content accept
        requestHeaders.setAccept(Collections.singletonList(APPLICATION_JSON));
        // Create a new RestTemplate instance [RestTemplate is a Spring rest client]
        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        // Add the Form Message converter
        messageConverters.add(new FormHttpMessageConverter());
        // Add the Jackson Message converter
        messageConverters.add(new MappingJackson2HttpMessageConverter());
        // Add the message converters to the restTemplate
        restTemplate.setMessageConverters(messageConverters);
        // Entity
        HttpEntity<Object> request = new HttpEntity<Object>(requestHeaders);

        try {
            // Make the network request
            ResponseEntity<Message> response = restTemplate.exchange(url, HttpMethod.POST, request, Message.class);
            return response.getBody().getText();

        }catch(HttpClientErrorException e){
            Log.i(TAG,e.getMessage());
            return null;
        }catch(ResourceAccessException e){
            Log.i(TAG,e.getMessage());
            return null;
        }catch(Exception e){
            Log.i(TAG,e.getMessage());
            return null;
        }
    }

    @Override
    protected void onPostExecute(String tokenEntity) {
        /*Le serveur spring renvoie l'access token + le refresh token au format string à la csv*/
        // Exemple : "access_token;fa67275e-93ff-472a-b51a-1b2683f0fb21;token_type;bearer;refresh_token;959c680f-feb7-46a1-b6d9-623fb3469a62;expires_in;1799;scope;read write
        if(tokenEntity==null){
            LoginManager.getInstance().logOut();
        }
        delegate.processFinish(tokenEntity);
    }







}
