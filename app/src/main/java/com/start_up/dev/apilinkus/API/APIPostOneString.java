package com.start_up.dev.apilinkus.API;

import android.os.AsyncTask;
import android.util.Log;

import com.start_up.dev.apilinkus.HomeActivity;
import com.start_up.dev.apilinkus.ProfileActivity;

import org.springframework.http.HttpEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


/**
 * Created by Huong on 18/12/2016.
 */
//Post que des string
public class APIPostOneString extends AsyncTask<Object, Void, Boolean> {
    private String param;
    private APIPostOneString_Observer obs;
    private String typePost;
    public APIPostOneString(String param,APIPostOneString_Observer obs,String typePost) {
        this.param=param;
        this.obs=obs;
        this.typePost=typePost;
    }

    @Override
    protected Boolean doInBackground(Object... params) {

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        headers.add("Authorization", "Bearer " + HomeActivity.access_token);
        headers.add("Content-Type", "application/json");
        String result = "";
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            HttpEntity<String> request = new HttpEntity<String>(param, headers);

            result = restTemplate.postForObject((String) params[0], request, String.class);
        } catch (Exception e) {
            Log.e("ServicePostAPIa", e.getMessage());
            Log.e("ServicePostAPIb", result);
            return false;
        }
        return true;

    }
    @Override
    protected void onPostExecute(Boolean result) {
        // Download complete. Lets update UI
        obs.postOneString_NotifyWhenGetFinish(result,typePost,param);
    }
}
