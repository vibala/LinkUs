package com.start_up.dev.apilinkus.API;

import org.json.JSONArray;

/**
 * Created by Huong on 18/12/2016.
 */

public interface APIGetRequestPendingListFriend_Observer {
        public void getRequestPendingListFriend_GetResponse(JSONArray responseArray);
        public void getRequestPendingListFriend_NotifyWhenGetFinish(Integer result);
}
