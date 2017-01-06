package com.start_up.dev.apilinkus.API;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Huong on 18/12/2016.
 */

public interface APIGetAlbumsFilterRight_Observer {
    public void albumsFilterRight_GetResponse(JSONArray responseArray);
    public void albumsFilterRight_GetResponse(JSONObject responseObject);
    public void albumsFilterRight_NotifyWhenGetFinish(Integer result);
}
