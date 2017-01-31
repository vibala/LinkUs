package com.start_up.dev.apilinkus.API;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Vignesh on 1/31/2017.
 */

public interface APIGetFriendProfileByIdFromPendingFriends_Observer {
    void getFriendProfileByIdFromPendingFriends_NotifyWhenGetFinish(Integer integer);
    void getFriendProfileByIdFromPendingFriends_GetResponse(JSONObject responseObject);
    void getFriendProfileByIdFromPendingFriends_GetResponse(JSONArray responseArray);
}


