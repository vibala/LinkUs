package com.start_up.dev.apilinkus.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Vignesh on 1/28/2017.
 */

public class APIGetSubscriptionList extends APIGet {

    private APIGetSubscriptionList_Observer apiGetSubscriptionList_observer;

    public APIGetSubscriptionList(APIGetSubscriptionList_Observer apiGetSubscriptionList_observer){
        super();
        this.apiGetSubscriptionList_observer = apiGetSubscriptionList_observer;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        apiGetSubscriptionList_observer.subscriptionList_NotifyWhenGetFinish(integer);
    }

    @Override
    protected void parseResult(String result) {
        try {
            JSONArray responseArray = new JSONArray(result);
            apiGetSubscriptionList_observer.subscriptionList_GetResponse(responseArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
