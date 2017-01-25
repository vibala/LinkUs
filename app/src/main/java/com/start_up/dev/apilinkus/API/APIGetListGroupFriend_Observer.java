package com.start_up.dev.apilinkus.API;

import org.json.JSONArray;

/**
 * Created by Huong on 18/12/2016.
 */

public interface APIGetListGroupFriend_Observer {
        public void getListGroupFriend_GetResponse(JSONArray responseArray);
        public void getListGroupFriend_NotifyWhenGetFinish(Integer result);
}
