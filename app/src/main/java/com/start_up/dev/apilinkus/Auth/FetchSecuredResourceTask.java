package com.start_up.dev.apilinkus.Auth;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.start_up.dev.apilinkus.R;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Vignesh on 12/16/2016.
 */

// ***************************************
// Private classes
// ***************************************
public class FetchSecuredResourceTask extends AsyncTask<String, Void, String> {

    private AsyncResponse delegate;
    private Context mContext;
    protected static final String
            TAG = FetchSecuredResourceTask.class.getSimpleName();

    public FetchSecuredResourceTask(Context mContext,AsyncResponse asyncResponse){
        this.delegate = asyncResponse;
        this.mContext = mContext;
    }

    @Override
    protected void onPreExecute() {
        // build the message object
        super.onPreExecute();
    }

    // http://stackoverflow.com/questions/15269822/how-to-pass-key-value-pair-using-resttemplate-in-java
    @Override
    protected String doInBackground(String... params) {
        final String url = mContext.getString(R.string.base_uri) + "/oauth/token";
        final String authorization = "Basic "
                + new String(Base64Utils.encode("clientapp:123456".getBytes()));
        final LoginRequest loginrequestInfo = new LoginRequest(params[0],params[1]);

        // Populate the HTTP Basic Authentitcation header with the username and password
        //HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set("Authorization",authorization);
        //requestHeaders.setAuthorization(authHeader);
        requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // create form parameters as a MultiValueMap
        final MultiValueMap<String, String> formVars = new LinkedMultiValueMap<>();
        formVars.add("username", loginrequestInfo.getUsername());
        formVars.add("password", loginrequestInfo.getPassword());
        formVars.add("grant_type", loginrequestInfo.getGrant_type() );
        formVars.add("scope", loginrequestInfo.getScope());
        formVars.add("client_secret", loginrequestInfo.getClient_secret() );
        formVars.add("client_id", loginrequestInfo.getClient_id() );

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
            Log.d(TAG, url);
            ResponseEntity<TokenEntity> response = restTemplate.exchange(url, HttpMethod.POST,request,TokenEntity.class);
            return response.getBody().toString();
        } catch (HttpClientErrorException e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            return null;
        } catch (ResourceAccessException e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            return null;
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            return null;
        }

    }

    @Override
    protected void onPostExecute(String tokenEntity) {
        delegate.processFinish(tokenEntity);
    }

}
