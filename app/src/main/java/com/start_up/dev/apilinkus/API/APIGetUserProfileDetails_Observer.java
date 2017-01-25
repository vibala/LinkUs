package com.start_up.dev.apilinkus.API;

import org.json.JSONObject;

/**
 * Created by Vignesh on 1/24/2017.
 */

public interface APIGetUserProfileDetails_Observer {

    public void userDetails_GetResponse(JSONObject responseJSON);
    public void userDetails_NotifyWhenGetFinish(Integer result);
}
