package com.start_up.dev.apilinkus.API;

import android.os.AsyncTask;
import android.util.Log;

import com.start_up.dev.apilinkus.HomeActivity;
import com.start_up.dev.apilinkus.Model.Moment;
import com.start_up.dev.apilinkus.Model.Subscription;
import com.start_up.dev.apilinkus.ProfileActivity;

import org.springframework.http.HttpEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Vignesh on 1/24/2017.
 */

public class APIPostAddSubscription extends AsyncTask {

    private RestTemplate restTemplate = new RestTemplate();
    private Subscription subscription;

    public APIPostAddSubscription(Subscription subscription){
        this.subscription = subscription;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        headers.add("Authorization", "Bearer " + HomeActivity.access_token);
        headers.add("Content-Type", "application/json");
        String result="";

        System.out.println("zzzzzzz");
        try{
            RestTemplate restTemplate = new RestTemplate();

            System.out.println("ccccccccccc"+restTemplate);
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            System.out.println("cccccccc demi ");
            HttpEntity<Subscription> request = new HttpEntity<Subscription>(subscription, headers);

            System.out.println("ddddddddddddddd"+request);
            result= restTemplate.postForObject((String) params[0], request, String.class);

            System.out.println("eeeeeeeeeeeee"+result);
        }catch (Exception e){
            Log.e("ServicePostAPIa", e.getMessage());
            Log.e("ServicePostAPIb", result);
        }
        return result;

    }
}
