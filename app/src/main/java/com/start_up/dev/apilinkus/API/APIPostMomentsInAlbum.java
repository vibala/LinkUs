package com.start_up.dev.apilinkus.API;

import android.os.AsyncTask;
import android.util.Log;

import com.start_up.dev.apilinkus.Model.Authentification;
import com.start_up.dev.apilinkus.Model.Moment;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.http.HttpEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Vignesh on 1/30/2017.
 */

public class APIPostMomentsInAlbum extends AsyncTask<String,Void,String> {

    private APIPostMomentsInAlbum_Observer observer;
    private final String TAG = APIPostMomentsInAlbum.class.getSimpleName();
    private List<String> momentIdList;

    public APIPostMomentsInAlbum(APIPostMomentsInAlbum_Observer observer, List<String> momentIdList){
        this.observer = observer;
        this.momentIdList = momentIdList;
    }

    @Override
    protected String doInBackground(String[] params) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        headers.add("Authorization", "Bearer " + Authentification.getAccess_token());
        headers.add("Content-Type", "application/json");
        String result="";
        try{
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            HttpEntity<List<String>> request = new HttpEntity<List<String>>(momentIdList,headers);

            result= restTemplate.postForObject((String) params[0], request, String.class);
        }catch (Exception e){
            Log.e("ServicePostAPIa", e.getMessage());
            Log.e("ServicePostAPIb", result);
        }
        return result;

    }

    @Override
    protected void onPostExecute(String b) {
        JSONArray responseArray = null;
        try {
            responseArray = new JSONArray(b);
            observer.getMomentInAlbum_GetResponse(responseArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



}
