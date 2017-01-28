package com.start_up.dev.apilinkus.API;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Vignesh on 1/28/2017.
 */

public interface APIGetSubscriptionList_Observer {
    public void subscriptionList_GetResponse(JSONArray responseArray);

    public void subscriptionList_NotifyWhenGetFinish(Integer result);

}