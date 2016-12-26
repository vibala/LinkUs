package com.start_up.dev.apilinkus.Service;

import com.google.firebase.iid.FirebaseInstanceIdService;
import com.start_up.dev.apilinkus.API.APILinkUS;

/**
 * Created by Huong on 10/12/2016.
 */

/**
 * onTokenRefresh in FirebaseInstanceIdService is only called when a new token is generated.
 * If your app was previously installed and generated a token then onTokenRefresh would not be called.
 * Try uninstalling and reinstalling the app to force the generation of a new token, this would cause onTokenRefresh to be called.
 */
public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseIDService";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.


        sendRegistrationToServer();
    }

    /**
     * Send token Notification so the server persist it
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     */
    private void sendRegistrationToServer() {
        new APILinkUS().sendTokenNotification();


    }
}
