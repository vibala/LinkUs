package com.start_up.dev.apilinkus.API;

import android.os.AsyncTask;
import android.util.Log;

import com.start_up.dev.apilinkus.HomeActivity;
import com.start_up.dev.apilinkus.MainActivity;
import com.start_up.dev.apilinkus.Model.Album;
import com.start_up.dev.apilinkus.ProfileActivity;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Vignesh on 1/27/2017.
 */

public class APIPostCreateAlbum extends AsyncTask {

    private Album album;
    private RestTemplate restTemplate = new RestTemplate();

    public APIPostCreateAlbum(Album album) {
        this.album=album;
    }

    @Override
    protected Object doInBackground(Object... params) {

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        headers.add("Authorization", "Bearer " + HomeActivity.access_token);
        headers.add("Content-Type", "application/json");
        String result="";
        try{
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            HttpEntity<Album> request = new HttpEntity<Album>(album, headers);
            ResponseEntity<String> response = restTemplate.postForEntity((String) params[0], request, String.class);
            HttpStatus status = response.getStatusCode();
            result = String.valueOf(status.value());
        }catch (Exception e){
            Log.e("ServicePostAPIa", e.getMessage());
            Log.e("ServicePostAPIb", result);
        }
        return result;

    }
}
