package com.start_up.dev.apilinkus.API;

import android.os.AsyncTask;
import android.util.Log;

import com.start_up.dev.apilinkus.HomeActivity;
import com.start_up.dev.apilinkus.Model.Authentification;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Vignesh on 1/26/2017.
 */

public class APIPostShareAlbumWith extends AsyncTask<Object,Void,Boolean> {

    private APIPostShareAlbumWith_Observer observer;

    public APIPostShareAlbumWith(APIPostShareAlbumWith_Observer observer){
        this.observer = observer;
    }

    @Override
    protected Boolean doInBackground(Object[] params) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        headers.add("Authorization", "Bearer " + Authentification.getAccess_token());
        headers.add("Content-Type", "application/json");
        String result="";
        try{
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            HttpEntity<String> request = new HttpEntity<String>(headers);

            ResponseEntity<String> response = restTemplate.postForEntity((String) params[0], request, String.class);
            HttpStatus status = response.getStatusCode();
            result = String.valueOf(status.value());

        }catch (Exception e){
            Log.e("ServicePostAPIa", e.getMessage());
            Log.e("ServicePostAPIb", result);
            return false;
        }
        return true;

    }

    @Override
    protected void onPostExecute(Boolean o) {
        observer.postShareAlbumWith_Notify(o);
    }
}
