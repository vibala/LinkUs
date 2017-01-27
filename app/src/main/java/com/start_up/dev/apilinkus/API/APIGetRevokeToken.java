package com.start_up.dev.apilinkus.API;

import android.util.Log;

/**
 * Created by Vignesh on 1/27/2017.
 */

public class APIGetRevokeToken extends APIGet {

    private final String TAG = APIGetRevokeToken.class.getSimpleName();

    @Override
    protected void onPostExecute(Integer integer) {
        Log.d(TAG,"Result : " + integer);
    }

    @Override
    protected void parseResult(String result) {

    }
}
