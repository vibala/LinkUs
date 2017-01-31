package com.start_up.dev.apilinkus.API;

import android.icu.text.DateFormat;
import android.os.AsyncTask;
import android.util.Log;

import com.start_up.dev.apilinkus.Model.Authentification;

import org.springframework.http.HttpEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Vignesh on 1/31/2017.
 */

public class APIPostFriendRequestDecision extends AsyncTask<String,Void,Boolean> {

    private APIPostFriendRequestDecision_Observer observer;

    public APIPostFriendRequestDecision(APIPostFriendRequestDecision_Observer observer){
        this.observer = observer;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        headers.add("Authorization", "Bearer " + Authentification.getAccess_token());
        headers.add("Content-Type", "application/json");

        try{
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            HttpEntity<String> request = new HttpEntity<String>(params[1], headers);

            String result = restTemplate.postForObject((String) params[0], request, String.class);
            if(result.contentEquals("200")){
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            Log.e("ServicePostAPIa", e.getMessage());
            return false;
        }

    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        this.observer.postFriendRequestDecision_NotifyWhenGetFinished(aBoolean);
    }
}
