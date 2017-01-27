package com.start_up.dev.apilinkus.API;

import org.json.JSONObject;

/**
 * Created by Vignesh on 1/26/2017.
 */

public interface APIGetUserNbFriendsAndNbOwnedAlbums_Observer {
    void userNbProchesAndOwnedAlbums_GetResponse(JSONObject responseJSON);
    void userNbProchesAndOwnedAlbums_NotifyWhenGetFinish(Integer result);
}
