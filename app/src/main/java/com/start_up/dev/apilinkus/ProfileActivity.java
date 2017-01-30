package com.start_up.dev.apilinkus;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.start_up.dev.apilinkus.Auth.Message;
import com.start_up.dev.apilinkus.Model.Authentification;
import com.twitter.sdk.android.core.TwitterCore;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Vignesh on 11/9/2016.
 */

public class ProfileActivity extends AppCompatActivity {

    protected static final String
            TAG = ProfileActivity.class.getSimpleName();

    private TextView profileTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_profile_activity);
        setTitle("Profile View");
        profileTextView = (TextView) findViewById(R.id.profileView);
        final Button  goTuto = (Button) findViewById(R.id.registrationButton);
        goTuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                //Start details activity
                startActivity(intent);
            }
        });

        new FetchProfileRessourceTask().execute(Authentification.getAccess_token(),Authentification.getToken_type(),Authentification.getRefresh_token());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        isStoragePermissionGranted();
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }


    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.action_logout:
                //Inutile de differencier car jusque la on a deja detruit les token
                if(Authentification.getMode_auth().equals("facebook")){
                    LoginManager.getInstance().logOut();
                }else if(Authentification.getMode_auth().equals("twitter")){
                    TwitterCore.getInstance().logOut();
                }
                new revokeToken().execute(Authentification.getAccess_token(),Authentification.getToken_type(),Authentification.getRefresh_token());
                System.out.println("TODO"); //TODO Vignesh implementer la fonction logout pour l'auth normal

                Intent intent = new Intent(this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // call this to finish the current activity
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private class revokeToken extends AsyncTask<String,Void,Message> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Message doInBackground(String... strings) {
            //final String url = getString(R.string.base_uri) + "/user/"+ strings[0];
            final String url = getString(R.string.base_uri) + "/oauth/revoke-token";
            final String authorization = "Bearer " + strings[0];
            Log.d("Authorization", authorization);

            // Create the request header
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
                ResponseEntity<Message> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(requestHeaders), Message.class);
                return response.getBody();
            } catch (HttpClientErrorException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
            } catch (ResourceAccessException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
            } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            }


        return null;
        }
    }
    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        /* La ca retourne au bouton home de l'utilisateur, pas vrmt ce qu'on veut TBC
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);*/
    }

    private void displayProfileInformation(Message welcomemsg){
        if(welcomemsg!=null)
            profileTextView.setText("Subject: " + welcomemsg.getSubject() + "\nText: " + welcomemsg.getText());
    }

    // ***************************************
    // Private classes
    // ***************************************
    private class FetchProfileRessourceTask extends AsyncTask<String,Void,Message>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Message doInBackground(String... strings) {
            //final String url = getString(R.string.base_uri) + "/user/"+ strings[0];
            final String url = getString(R.string.base_uri) + "/home";
            final String authorization = "Bearer " + strings[0];
            Log.d("Authorization",authorization);

            // Create the request header
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.set("Authorization",authorization);
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
                ResponseEntity<Message> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(requestHeaders), Message.class);
                return response.getBody();
            } catch (HttpClientErrorException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
            } catch (ResourceAccessException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
            }

            return null;
        }


        @Override
        protected void onPostExecute(Message userEntity) {
            displayProfileInformation(userEntity);
        }
    }

}