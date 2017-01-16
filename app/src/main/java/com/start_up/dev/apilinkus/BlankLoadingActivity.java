package com.start_up.dev.apilinkus;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.start_up.dev.apilinkus.Auth.AsyncResponse;
import com.start_up.dev.apilinkus.Auth.FetchSecuredFacebookResourceTask;
import com.start_up.dev.apilinkus.Auth.FetchSecuredResourceTask;
import com.start_up.dev.apilinkus.Auth.FetchSecuredTwitterResourceTask;
import com.twitter.sdk.android.core.TwitterCore;

/**
 * Created by Vignesh on 12/15/2016.
 */
/*Cette activité se chargera en tâche de fond de récupérer les tokens et de le passer ensuite au ProfileActivity */
public class BlankLoadingActivity extends AppCompatActivity {

    private static final String
            TAG = BlankLoadingActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_blank_loading_activity);
        setTitle("LinkUs");
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent i = getIntent();
        Bundle b = i.getExtras();

        if(b!= null){
            if(b.containsKey("twitter_token_standard") && b.containsKey("twitter_token_secret")){
                String twitter_token_standard = b.getString("twitter_token_standard");
                String twitter_token_secret = b.getString("twitter_token_secret");

                FetchSecuredTwitterResourceTask task = new FetchSecuredTwitterResourceTask(BlankLoadingActivity.this, new AsyncResponse() {
                    @Override
                    public void processFinish(String token_entity) {
                        if(token_entity != null && !token_entity.isEmpty() && !token_entity.contains("Error")){
                            Intent intent = new Intent(BlankLoadingActivity.this,ProfileActivity.class);
                            String[] results = token_entity.split(";");
                            Log.i(TAG,"Access_token : " + results[1]);
                            intent.putExtra("access_token",results[1]);
                            intent.putExtra("token_type",results[3]);
                            intent.putExtra("refresh_token",results[5]);
                            intent.putExtra("mode_auth","twitter");
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }else{
                            Log.i(TAG,"The server might be down");
                            Intent intent = new Intent(BlankLoadingActivity.this,MainActivity.class);
                            intent.putExtra("msg.Error","The server might be down");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish(); // call this to finish the current activity

                        }

                        TwitterCore.getInstance().logOut();
                    }
                });

                task.execute(twitter_token_standard,twitter_token_secret);

            }else if(b.containsKey("username") && b.containsKey("password")){
                /*En amont, on a fait les tests nécessaires pour s'assurer que l'utilisateur ne soument pas un username ou un pwd vide*/
                FetchSecuredResourceTask task = new FetchSecuredResourceTask(BlankLoadingActivity.this, new AsyncResponse() {
                    @Override
                    public void processFinish(String token_entity) {
                        if(token_entity != null && !token_entity.isEmpty() && !token_entity.contains("Error")){
                            Intent intent = new Intent(BlankLoadingActivity.this,ProfileActivity.class);
                            String[] results = token_entity.split(";");
                            Log.i(TAG,"Access_token : " + results[1]);
                            intent.putExtra("access_token",results[1]);
                            intent.putExtra("token_type",results[3]);
                            intent.putExtra("refresh_token",results[5]);
                            intent.putExtra("mode_auth","normal");
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }else{
                            //LOGOUT TODO Vignesh logout session normal
                            Log.i(TAG,"The server might be down");
                            Intent intent = new Intent(BlankLoadingActivity.this,MainActivity.class);
                            intent.putExtra("msg.Error","Your credentials are incorrect");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish(); // call this to finish the current activity
                        }

                    }
                });
                task.execute(b.getString("username"),b.getString("password"));

            }else if(b.containsKey("facebook_access_token")){
                // Facebook renvoie l'utilisateur vers cette activité une fois qu'il a réussi à s'authentifier
                String facebook_access_token_value = b.getString("facebook_access_token_value");
                FetchSecuredFacebookResourceTask task = new FetchSecuredFacebookResourceTask(BlankLoadingActivity.this, new AsyncResponse() {
                    @Override
                    public void processFinish(String token_entity) {
                        if(token_entity != null && !token_entity.isEmpty() &&  !token_entity.contains("Error")){
                            Intent intent = new Intent(BlankLoadingActivity.this,ProfileActivity.class);
                            String[] results = token_entity.split(";");
                            Log.i(TAG,"Access_token : " + results[1]);
                            intent.putExtra("access_token",results[1]);
                            intent.putExtra("token_type",results[3]);
                            intent.putExtra("refresh_token",results[5]);
                            intent.putExtra("mode_auth","facebook");
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }else{
                            Log.i(TAG,"The server might be down");
                            Intent intent = new Intent(BlankLoadingActivity.this,MainActivity.class);
                            intent.putExtra("msg.Error","The server might be down");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish(); // call this to finish the current activity
                        }

                        LoginManager.getInstance().logOut();
                    }
                });
                task.execute(facebook_access_token_value);

            }
        }
    }


}