package com.start_up.dev.apilinkus.API;

import android.os.AsyncTask;
import android.util.Log;

import com.start_up.dev.apilinkus.Model.Instant;
import com.start_up.dev.apilinkus.Model.Moment;
import com.start_up.dev.apilinkus.ProfileActivity;

import org.springframework.http.HttpEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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

        System.out.println("zzzzzzz");
     try{
        RestTemplate restTemplate = new RestTemplate();

         System.out.println("ccccccccccc"+restTemplate);
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

         System.out.println("cccccccc demi ");
        HttpEntity<Moment> request = new HttpEntity<Moment>(moment, headers);

         System.out.println("ddddddddddddddd"+request);
        result= restTemplate.postForObject((String) params[0], request, String.class);

         System.out.println("eeeeeeeeeeeee"+result);
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
