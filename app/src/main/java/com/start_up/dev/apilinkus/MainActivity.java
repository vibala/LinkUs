package com.start_up.dev.apilinkus;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.start_up.dev.apilinkus.API.APIGetUserProfileDetails_Observer;
import com.start_up.dev.apilinkus.API.APILinkUS;
import com.start_up.dev.apilinkus.Auth.AbstractAsyncActivity;
import com.start_up.dev.apilinkus.Model.Authentification;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONObject;

import io.fabric.sdk.android.Fabric;


public class MainActivity extends AbstractAsyncActivity implements APIGetUserProfileDetails_Observer {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "3vSE6aWBhqI2L3T5ruj24j3SO";
    private static final String TWITTER_SECRET = "C2tJRRZYKydLuu5WZZg56W74CcmJhd6QZZYL9mLRVEC0l39pRC";


    protected static final String
            TAG = MainActivity.class.getSimpleName();

    static final int REGISTER_USER_REQUEST = 1; // The request code
    private EditText passwordeditText;
    private CallbackManager callbackManager;
    private LoginButton facebookLoginButton;
    private TwitterLoginButton twitterLoginButton;
    private APILinkUS api;
    private boolean access_token_valid = true;

    // ***************************************
    // Activity methods
    // ***************************************
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Initialisation de l'url il a besoin du context pour chercher l'url qui se trouve dans urls.xml
        api = new APILinkUS(MainActivity.this);
        super.onCreate(savedInstanceState);

        // il faut laisser passer SI une des deux conditions est vérifiée:
        /*1 - le MDP est bon et lui donner un bon token*
                OU
        2- le laisser passer si envoit un Token qui correspond bien au token dans la BD*/
        if(Authentification.getAccess_token()!= null){
            api.getUserProfileDetails(this,this);
            if(access_token_valid == true){
                Log.d(TAG,"Access token valid true");
                access_token_valid = false; // reset
                Intent intent = new Intent(this,HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // call this to finish the current activity
            }
        }

        /*Social conf settings */
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig));
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.login_sign_in_activity);
        setTitle("LinkUS App");

        /*Facebook Login Button************************************************/
        //  redirige les appels vers le SDK Facebook et vos rappels enregistrés.
        callbackManager = CallbackManager.Factory.create();
        // Retrieve login button
        facebookLoginButton = (LoginButton)findViewById(R.id.facebook_sign_in_button);
        facebookLoginButton.setReadPermissions("public_profile","user_birthday","email");
        twitterLoginButton = (TwitterLoginButton)findViewById(R.id.twitter_sign_in_button);
        passwordeditText = (EditText) findViewById(R.id.passwordLogin);


        // Initiate the request to the protected service
        final Button submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //new FetchSecuredResourceTask().execute();
                EditText editText = (EditText) findViewById(R.id.emailLogin);
                String username = editText.getText().toString();
                String password = passwordeditText.getText().toString();
                if(username.isEmpty() || username == null ){
                    Toast.makeText(MainActivity.this, "Username field is empty ! Please fill correctly the sign-in form", Toast.LENGTH_SHORT).show();
                }else if(password.isEmpty() || password == null){
                    Toast.makeText(MainActivity.this, "Password field is empty ! Please fill correctly the sign-in form", Toast.LENGTH_SHORT).show();
                }else{
                    Intent i = new Intent(MainActivity.this,BlankLoadingActivity.class);
                    i.putExtra("username",username);
                    i.putExtra("password",password);
                    startActivity(i);
                    finish(); // call this to finish the current task*/
                }
            }
        });
        final Button registerButton = (Button) findViewById(R.id.registrationButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RegistrationActivity.class);
                startActivityForResult(intent,REGISTER_USER_REQUEST);
            }
        });



    }

    /*******************************************************************************************/
    /*******************************************************************************************/
    /*******************************************************************************************/

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent_calling_a_new_activity = getIntent();
        if(intent_calling_a_new_activity != null){
            Bundle extra = intent_calling_a_new_activity.getExtras();
            if(extra != null && !extra.isEmpty() && extra.getString("msg.Error") != null){
                //Toast.makeText(this,extra.getString("msg.Error"),Toast.LENGTH_SHORT).show();
            }
        }

        /*******************************************************************************************/
        /**TWITTER AUTH IMPLEMENTATION**/
        /*******************************************************************************************/

        // Adding callback to the button
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                login(result);
            }

            @Override
            public void failure(TwitterException e) {
                Log.i(TAG,"Logging with Twitter failure",e);
            }
        });

        /*******************************************************************************************/
        /**FACEBOOK AUTH IMPLEMENTATION**/
        /*******************************************************************************************/

        // Callback Registration
        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            // Creation d'une instance du ProfileTracker qui va démarrer le suivi des mises à jour du profil
            //(i.e) ( la méthode ProfileTracker.onCurrentProfileChanged() va être appellée quand le profile du client
            // sera sur le point d'être recupéré
            ProfileTracker mProfileTracker;
            AccessTokenTracker mAccessTokenTracker;

            @Override
            public void onSuccess(final LoginResult loginResult) {
                Log.i(TAG,"Access Token : " + loginResult.getAccessToken());
                Profile profile;

                mAccessTokenTracker = new AccessTokenTracker() {
                    @Override
                    protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                        if(currentAccessToken != null){
                            // LOGGED IN
                        }else{
                            // LOGGED OUT
                            Toast.makeText(MainActivity.this,"Current Access Token : NULL ! Need to login with Facebook",Toast.LENGTH_SHORT);
                            Log.i(TAG,"Current Access Token : NULL ! Need to login with Facebook");

                        }
                    }
                };

                /*Pour que ca fonctionne il faut peut etre ne pas delogguer dans tous les case une fois qu on a envoyé le token FB a Spring. Le token fb est
                 peut etre utilisé pour savoir si le profile a changé...LoginManager.getInstance().logOut(); dans BlankLoading Activity*/
                if((profile = Profile.getCurrentProfile())==null){
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                            Log.i(TAG,"First Name : " + currentProfile.getFirstName());
                            Log.i(TAG,"Last Name : " + currentProfile.getLastName());
                            Log.i(TAG,"Token : " + AccessToken.getCurrentAccessToken().getToken());
                            Intent i = new Intent(MainActivity.this,BlankLoadingActivity.class);
                            //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // call this to clear the back stack
                            String facebook_access_token_value=AccessToken.getCurrentAccessToken().getToken();
                            i.putExtra("facebook_access_token","true");
                            i.putExtra("facebook_access_token_value",facebook_access_token_value);
                            startActivity(i);
                            finish(); // call this to finish the current task*/
                            mProfileTracker.stopTracking();
                        }
                    };
                }else{
                    Log.i(TAG,"First Name : " + profile.getFirstName());
                    Log.i(TAG,"Last Name : " + profile.getLastName());
                    Log.i(TAG,"Token : " + AccessToken.getCurrentAccessToken().getToken());
                    Intent i = new Intent(MainActivity.this,BlankLoadingActivity.class);
                    String facebook_access_token_value=AccessToken.getCurrentAccessToken().getToken();
                    i.putExtra("facebook_access_token","true");
                    i.putExtra("facebook_access_token_value",facebook_access_token_value);
                    startActivity(i);
                    finish(); // call this to finish the current task
                }
            }

            @Override
            public void onCancel() {
                Log.d(TAG,"Facebook login was canceled");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e(TAG,"Facebook login failed: " + error.getMessage());
            }
        });
    }


    /*******************************************************************************************/
    /*******************************************************************************************/
    /*******************************************************************************************/

    // The login function retrieving the result object
    public void login(Result<TwitterSession> result){
        // If login completes successfully, a TwitterSession is provided in the success result
        // Creating a twitter session with result's data
        TwitterSession session = result.data;

        // Getting the username from session
        final String username = session.getUserName();
        Log.i(TAG,"Username from twitter session : " + username);

        //  Getting the authToken which represents an authorization toke  and its secret (accessTokenSecret)
        TwitterAuthToken authToken = session.getAuthToken();

        // Getting the token
        String token = authToken.token;
        // Getting the secret key
        String secret = authToken.secret;
        Log.i(TAG,"accessToken  from twitter session : " + token);
        Log.i(TAG,"accessToken  from twitter session : " + secret);

        Intent i = new Intent(MainActivity.this,BlankLoadingActivity.class);
        i.putExtra("twitter_token_standard",token);
        i.putExtra("twitter_token_secret",secret);
        startActivity(i);
        finish(); // call this to finish the current task

    }

    /*******************************************************************************************/
    /*******************************************************************************************/
    /*******************************************************************************************/


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);

        if(FacebookSdk.isFacebookRequestCode(requestCode)){
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }else if(requestCode == REGISTER_USER_REQUEST){
            // Make sure the request was successful
            if(resultCode == RESULT_OK && data != null){
                // The user has been registered in the database
                Bundle extra = data.getExtras();
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Hi " + extra.getString("firstname") + "! We have sent an email to your adress ! Please confirm your request ");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alertDialog.show();
            }else if(resultCode == RESULT_CANCELED && data != null){
                Bundle extra = data.getExtras();
                Toast.makeText(this,extra.getString("reason"),Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode == TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE){
            twitterLoginButton.onActivityResult(requestCode,resultCode,data);
        }
    }

    @Override
    public void userDetails_GetResponse(JSONObject responseJSON) {

    }

    @Override
    public void userDetails_NotifyWhenGetFinish(Integer result) {
        if(result==1){
            access_token_valid = true;
        }else{
            access_token_valid = false;
        }
    }

    /*******************************************************************************************/
    /*******************************************************************************************/
    /*******************************************************************************************/


}