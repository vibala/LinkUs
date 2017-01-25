package com.start_up.dev.apilinkus.API;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Huong on 18/01/2017.
 */

public class APIGetListGroupFriend extends APIGet {

        private APIGetListGroupFriend_Observer activityObserver;

        public APIGetListGroupFriend(APIGetListGroupFriend_Observer activityObserver) {
            super();
            this.activityObserver = activityObserver;
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Lets update UI
            activityObserver.getListGroupFriend_NotifyWhenGetFinish(result);
        }

        /**
         * Parsing the feed results and get the list
         *
         * @param result
         */
        @Override
        protected void parseResult(String result) {
            try {
                JSONArray responseArray = new JSONArray(result);
                activityObserver.getListGroupFriend_GetResponse(responseArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
