package com.start_up.dev.apilinkus.API;

import org.json.JSONArray;

/**
 * Created by Huong on 18/12/2016.
 */

public interface APIGetPendingListFriend_Observer {
        public void getPendingListFriend_GetResponse(JSONArray responseArray);
        public void getPendingListFriend_NotifyWhenGetFinish(Integer result);
}
