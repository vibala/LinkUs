package com.start_up.dev.apilinkus.API;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Vignesh on 2/1/2017.
 */
public class APIGetUsersWithRigthInAlbum extends APIGet {

    private APIGetUsersWithRigthInAlbum_Observer observer;

    public APIGetUsersWithRigthInAlbum(APIGetUsersWithRigthInAlbum_Observer observer){
        this.observer=observer;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        observer.usersWithRigthInAlbum_NotifyWhenGetFinish(integer);
    }

    @Override
    protected void parseResult(String result) {
        observer.usersWithRigthInAlbum_GetResponse(result);
    }
}
