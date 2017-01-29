package com.start_up.dev.apilinkus.API;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Vignesh on 1/23/2017.
 */

public class APIGetUserProfileDetails extends APIGet {

    private APIGetUserProfileDetails_Observer activityObserver;
    private ProgressDialog dialog;
    private Context mContext;

    public APIGetUserProfileDetails(APIGetUserProfileDetails_Observer activityObserver,Context mContext){
        super();
        this.mContext = mContext;
        this.activityObserver = activityObserver;
        this.dialog = new ProgressDialog(mContext);
    }

    @Override
    protected void onPreExecute() {
        this.dialog.setMessage("Profile loading");
        this.dialog.show();
    }

    @Override
    protected void onPostExecute(Integer integer) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        activityObserver.userDetails_NotifyWhenGetFinish(integer);
    }

    /**
     * Parsing the feed results and get the list
     *
     * @param result
     */
    @Override
    protected void parseResult(String result) {
        try {
            JSONObject responseJSON = new JSONObject(result);
            Log.d("ZZZZZZZZZZZZZZ",responseJSON.toString());
            activityObserver.userDetails_GetResponse(responseJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
