package com.start_up.dev.apilinkus.API;

import android.app.ProgressDialog;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Vignesh on 1/25/2017.
 */

public class APIGetAlbumByAlbumId extends APIGet {

    private APIGetAlbumByAlbumId_Observer albumByAlbumId_observer;
    private ProgressDialog dialog;

    public APIGetAlbumByAlbumId(APIGetAlbumByAlbumId_Observer activity_observer,Context context){
        this.albumByAlbumId_observer = activity_observer;
        this.dialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        this.dialog.setMessage("Fetching details about the selected album ! Please wait...");
        //this.dialog.show();
    }

    @Override
    protected void onPostExecute(Integer result) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
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
