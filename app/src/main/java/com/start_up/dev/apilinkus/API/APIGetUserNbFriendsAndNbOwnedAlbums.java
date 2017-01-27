package com.start_up.dev.apilinkus.API;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Vignesh on 1/26/2017.
 */

public class APIGetUserNbFriendsAndNbOwnedAlbums extends APIGet {

    private APIGetUserNbFriendsAndNbOwnedAlbums_Observer activityObserver;

    public APIGetUserNbFriendsAndNbOwnedAlbums(APIGetUserNbFriendsAndNbOwnedAlbums_Observer activityObserver){
        this.activityObserver = activityObserver;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        activityObserver.userNbProchesAndOwnedAlbums_NotifyWhenGetFinish(integer);
    }

    @Override
    protected void parseResult(String result) {
        try {
            JSONObject responseJSON = new JSONObject(result);
            activityObserver.userNbProchesAndOwnedAlbums_GetResponse(responseJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
