package com.start_up.dev.apilinkus.API;

import android.app.Activity;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Huong on 18/12/2016.
 */

public interface APIGetAlbumsOwned_Observer{
        void albumsOwned_GetResponse(JSONArray responseArray);
        void albumsOwned_GetResponse(JSONObject responseObject);
        void albumsOwned_NotifyWhenGetFinish(Integer result,Activity parent_activity);
}
