package com.start_up.dev.apilinkus.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Vignesh on 1/25/2017.
 */

public class APIGetAlbumByAlbumId extends APIGet {

    private APIGetAlbumByAlbumId_Observer albumByAlbumId_observer;

    public APIGetAlbumByAlbumId(APIGetAlbumByAlbumId_Observer activity_observer){
        super();
        this.albumByAlbumId_observer = activity_observer;
    }

    @Override
    protected void onPostExecute(Integer result) {
        albumByAlbumId_observer.albumByAlbumId_NotifyWhenGetFinish(result);
    }

    /**
     * Parsing the feed results and get the list
     *
     * @param result
     */
    @Override
    protected void parseResult(String result) {
        try {
            JSONObject responseObject = new JSONObject(result);
            albumByAlbumId_observer.albumByAlbumId_GetResponse(responseObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
