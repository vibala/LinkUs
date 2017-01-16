package com.start_up.dev.apilinkus.API;

import android.os.AsyncTask;
import android.util.Log;


import com.start_up.dev.apilinkus.Auth.Message;
import com.start_up.dev.apilinkus.Model.Album;
import com.start_up.dev.apilinkus.ProfileActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Huong on 12/12/2016.
 */

public abstract class  APIGet extends AsyncTask<String, Void, Integer> {
    private static final String TAG = APIGet.class.getSimpleName();



    @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            final String authorization = "Bearer " + ProfileActivity.access_token;
            Log.d("Authorization", authorization);

           /* // Create the request header
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
                // Make the network request
                Log.d(TAG, url);
                ResponseEntity<Album[]> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<ArrayList<Album>>(requestHeaders), Album[].class);
                System.out.println(response);
                parseResult(response.getBody().toString());
                System.out.println(response.getBody().toString());
                return 1;
            } catch (HttpClientErrorException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
            } catch (ResourceAccessException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
            } catch (Exception e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
            }
            return 0;*/
            try {
                // Create Apache HttpClient
                URL url = new URL(params[0]);
                HttpURLConnection conn  = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("content-type", "application/json");
                conn.setRequestProperty("Authorization", authorization);
                conn.setRequestMethod("GET");
                int statusCode = conn.getResponseCode();

                // 200 represents HTTP OK
                if (statusCode == 200) {
                    String response = streamToString(conn.getInputStream());
                    parseResult(response);
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed
                }
            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }

        return result;
    }


    private String streamToString(InputStream stream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        String line;
        String result = "";
        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

        // Close stream
        if (null != stream) {
            stream.close();
        }
        return result;
    }

    protected abstract void parseResult(String result);
}
