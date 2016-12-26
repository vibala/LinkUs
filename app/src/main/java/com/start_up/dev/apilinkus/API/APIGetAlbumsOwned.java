package com.start_up.dev.apilinkus.API;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Huong on 18/12/2016.
 */

public class APIGetAlbumsOwned extends APIGet {

    private APIGetAlbumsOwned_Observer activityObserver;

    public APIGetAlbumsOwned(APIGetAlbumsOwned_Observer activityObserver) {
        super();
        this.activityObserver = activityObserver;
    }

    @Override
    protected void onPostExecute(Integer result) {
        // Download complete. Lets update UI
        activityObserver.albumsOwned_NotifyWhenGetFinish(result);
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
