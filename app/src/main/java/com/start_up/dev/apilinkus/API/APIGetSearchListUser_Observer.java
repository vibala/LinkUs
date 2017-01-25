package com.start_up.dev.apilinkus.API;

import org.json.JSONArray;

/**
 * Created by Huong on 18/12/2016.
 */

public interface APIGetSearchListUser_Observer {
        public void getSearchListUser_GetResponse(JSONArray responseArray);
        public void getSearchListUser_NotifyWhenGetFinish(Integer result);
}
