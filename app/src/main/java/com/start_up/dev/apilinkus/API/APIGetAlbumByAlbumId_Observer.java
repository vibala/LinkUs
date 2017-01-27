package com.start_up.dev.apilinkus.API;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Vignesh on 1/25/2017.
 */

public interface APIGetAlbumByAlbumId_Observer {
    public void albumByAlbumId_GetResponse(JSONObject responseObject);
    public void albumByAlbumId_NotifyWhenGetFinish(Integer result);
}
