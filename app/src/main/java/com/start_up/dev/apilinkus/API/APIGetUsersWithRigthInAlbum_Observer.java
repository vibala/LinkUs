package com.start_up.dev.apilinkus.API;

import android.app.Activity;

import org.json.JSONObject;

/**
 * Created by Vignesh on 2/1/2017.
 */
public interface APIGetUsersWithRigthInAlbum_Observer {
    void usersWithRigthInAlbum_GetResponse(String resultObject);
    void usersWithRigthInAlbum_NotifyWhenGetFinish(Integer result);
}
