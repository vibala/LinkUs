package com.start_up.dev.apilinkus.API;

import android.os.AsyncTask;
import android.util.Log;

import com.start_up.dev.apilinkus.Model.Moment;
import com.start_up.dev.apilinkus.ProfileActivity;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by Huong on 18/12/2016.
 */

public class APIPostAddFriendByMail extends AsyncTask {
    private String mail;
    private RestTemplate restTemplate = new RestTemplate();

    public APIPostAddFriendByMail(String mail) {
        this.mail=mail;
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

            HttpEntity<String> request = new HttpEntity<String>(mail, headers);

            result= restTemplate.postForObject((String) params[0], request, String.class);
        }catch (Exception e){
            Log.e("ServicePostAPIa", e.getMessage());
            Log.e("ServicePostAPIb", result);
        }
        return result;

    }
}
