package com.start_up.dev.apilinkus.API;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.core.JsonParseException;
import com.start_up.dev.apilinkus.HomeActivity;
import com.start_up.dev.apilinkus.Model.Authentification;
import com.start_up.dev.apilinkus.Model.Instant;
import com.start_up.dev.apilinkus.Model.Moment;
import com.start_up.dev.apilinkus.ProfileActivity;

import org.springframework.http.HttpEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Huong on 06/11/2016.
 */

public class APIPostMoment extends AsyncTask<Object,Void,Boolean> {

    private Moment moment;
    private APIPostMoment_Observer observer;
    private final String TAG = APIPostMoment.class.getSimpleName();
    private Context mContext;
    private ProgressDialog dialog;


    public APIPostMoment(Moment m,APIPostMoment_Observer observer,Context context) {
        this.moment=m;
        this.observer = observer;
        this.mContext = context;
        this.dialog = new ProgressDialog(mContext);
    }

    @Override
    protected void onPreExecute() {
        this.dialog.setMessage("Moment uploading ....");
        this.dialog.show();
    }

    @Override
    protected Boolean doInBackground(Object... params) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        headers.add("Authorization", "Bearer " + Authentification.getAccess_token());
        headers.add("Content-Type", "application/json");
        String result = "";
        try {
            RestTemplate restTemplate = new RestTemplate();

            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            HttpEntity<Moment> request = new HttpEntity<Moment>(moment, headers);

            result = restTemplate.postForObject((String) params[0], request, String.class);

            Log.d(TAG, "Result post moment " + result);

        }catch (Exception e){

            e.printStackTrace();
            Log.e("zzzzz ServicePostAPIa", e.getMessage());
            Log.e("zzzzz ServicePostAPIb", result);
            return false;
        }
              return true;

    }

    @Override
    protected void onPostExecute(Boolean o) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        observer.postMomentToServer_NotifyWhenGetFinish(o);
    }
}
