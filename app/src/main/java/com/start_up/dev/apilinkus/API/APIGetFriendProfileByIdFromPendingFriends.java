package com.start_up.dev.apilinkus.API;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Vignesh on 1/31/2017.
 */

public class APIGetFriendProfileByIdFromPendingFriends extends APIGet {

    private APIGetFriendProfileByIdFromPendingFriends_Observer observer;

    public APIGetFriendProfileByIdFromPendingFriends(APIGetFriendProfileByIdFromPendingFriends_Observer observer){
        this.observer = observer;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        Log.d("AGPFPBPRF","JE SUSSSSIS LA");
        this.observer.getFriendProfileByIdFromPendingFriends_NotifyWhenGetFinish(integer);
    }

    @Override
    protected void parseResult(String result) {
        try {
            JSONObject object = new JSONObject(result);
            this.observer.getFriendProfileByIdFromPendingFriends_GetResponse(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
