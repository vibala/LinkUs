package com.start_up.dev.apilinkus.API;

import org.json.JSONArray;

/**
 * Created by Huong on 18/12/2016.
 */

public interface APIGetListFriend_Observer {
        public void getListFriend_GetResponse(JSONArray responseArray);
        public void getListFriend_NotifyWhenGetFinish(Integer result);
}
