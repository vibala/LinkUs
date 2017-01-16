package com.start_up.dev.apilinkus.API;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Huong on 18/12/2016.
 */

public class APIGetAlbumsFilterRight extends APIGet {


    private APIGetAlbumsFilterRight_Observer activityObserver;

    public APIGetAlbumsFilterRight(APIGetAlbumsFilterRight_Observer activityObserver) {
        super();
        this.activityObserver = activityObserver;
    }

    @Override
    protected void onPostExecute(Integer result) {
        // Download complete. Lets update UI
        activityObserver.albumsFilterRight_NotifyWhenGetFinish(result);
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
            activityObserver.albumsFilterRight_GetResponse(responseArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
