package com.start_up.dev.apilinkus.API;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Huong on 18/01/2017.
 */

public class APIGetNotificationMoment extends APIGet {

        private APIGetNotificationMoment_Observer activityObserver;

        public APIGetNotificationMoment(APIGetNotificationMoment_Observer activityObserver) {
            super();
            this.activityObserver = activityObserver;
        }

        /**
         * Parsing the feed results and get the list
         *
         * @param result
         */
        @Override
        protected void parseResult(String result) {
            try {
                JSONObject responseObject = new JSONObject(result);
                activityObserver.getGetNotificationMoment_GetResponse(responseObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
