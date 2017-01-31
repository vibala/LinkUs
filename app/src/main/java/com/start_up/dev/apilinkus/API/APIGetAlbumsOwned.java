package com.start_up.dev.apilinkus.API;

import android.app.Activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Huong on 18/12/2016.
 */

public class APIGetAlbumsOwned extends APIGet {

    private APIGetAlbumsOwned_Observer activityObserver;
    private Activity parent_activity;

    public APIGetAlbumsOwned(APIGetAlbumsOwned_Observer activityObserver, Activity parent_activity) {
        super();
        this.parent_activity = parent_activity;
        this.activityObserver = activityObserver;
    }

    @Override
    protected void onPostExecute(Integer result) {
        // Download complete. Lets update UI
        activityObserver.albumsOwned_NotifyWhenGetFinish(result,parent_activity);
    }

    /**
     * Parsing the feed results and get the list
     *
     * @param result
     */
    @Override
    protected void parseResult(String result) {
        try {
            JSONArray responseArray = new JSONArray(result);
            activityObserver.albumsOwned_GetResponse(responseArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
