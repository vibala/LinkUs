package com.start_up.dev.apilinkus.API;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.start_up.dev.apilinkus.Model.Moment;
import com.start_up.dev.apilinkus.ProfileActivity;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Huong on 06/11/2016.
 */

public class APIPostMoment extends AsyncTask {

    private Moment moment;
    private RestTemplate restTemplate = new RestTemplate();
    public APIPostMoment(Moment m) {
        this.moment=m;
    }

    @Override
    protected Object doInBackground(Object... params) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        headers.add("Authorization", "Bearer " + ProfileActivity.access_token);
        headers.add("Content-Type", "application/json");
         String result="";
     try{
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        HttpEntity<Moment> request = new HttpEntity<Moment>(moment, headers);

        result= restTemplate.postForObject((String) params[0], request, String.class);
        }catch (Exception e){
            Log.e("ServicePostAPIa", e.getMessage());
            Log.e("ServicePostAPIb", result);
        }
              return result;
        /*// Create the request header
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set("Authorization", authorization);
        requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));


        // Create a new RestTemplate instance [RestTemplate is a Spring rest client]
        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        // Add the Form Message converter
        messageConverters.add(new FormHttpMessageConverter());
        // Add the Jackson Message converter
        messageConverters.add(new MappingJackson2HttpMessageConverter());
        // Add the message converters to the restTemplate
        restTemplate.setMessageConverters(messageConverters);
        try {
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.postForObject((String) params[0], moment, String.class);
        }catch (Exception e){
            Log.e("ServicePostAPI", e.getMessage(), e);
        }
        return null;       */
    }


}
