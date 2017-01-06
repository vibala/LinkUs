package com.start_up.dev.apilinkus.API;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Huong on 18/12/2016.
 */

public interface APIGetAlbumsOwned_Observer{
        public void albumsOwned_GetResponse(JSONArray responseArray);
        public void albumsOwned_GetResponse(JSONObject responseObject);
        public void albumsOwned_NotifyWhenGetFinish(Integer result);
}
