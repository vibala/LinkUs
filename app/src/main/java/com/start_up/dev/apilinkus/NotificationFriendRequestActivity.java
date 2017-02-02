package com.start_up.dev.apilinkus;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.start_up.dev.apilinkus.API.APIGetFriendProfileByIdFromPendingFriends_Observer;
import com.start_up.dev.apilinkus.API.APILinkUS;
import com.start_up.dev.apilinkus.API.APIPostFriendRequestDecision_Observer;
import com.start_up.dev.apilinkus.Adapter.NotifFriendRequestAdapter;
import com.start_up.dev.apilinkus.Model.Authentification;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Vignesh on 1/31/2017.
 */

public class NotificationFriendRequestActivity extends AppCompatActivity implements
        NotifFriendRequestAdapter.OnClickListener,APIGetFriendProfileByIdFromPendingFriends_Observer,
            APIPostFriendRequestDecision_Observer {
    private RecyclerView recyclerView;
    private final String TAG = NotificationFriendRequestActivity.class.getSimpleName();
    private NotifFriendRequestAdapter adapter;
    private HashMap<String,String> friendsFromPendingList;
    private APILinkUS api;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_home_fragment);
        friendsFromPendingList = new HashMap<>();
        api = new APILinkUS();
        if(getIntent() != null){
            Bundle bundle = getIntent().getExtras();
            if(bundle != null){
                if(Authentification.getAccess_token()!= null){
                    api.findFriendProfileByIdFromPendingFriends(bundle.getString("fromFriendId"),this);
                }else{ // On va lui demander de se reconnecter
                    Intent intent = new Intent(this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish(); // call this to finish the current activity
                }
            }
        }

        //Log.d("NFRA","zzzzz * 1");
        //api.findFriendProfileByIdFromPendingFriends("58927750274e851e4420e0fb",this); // Pour mes tests

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this,1);
        adapter = new NotifFriendRequestAdapter(friendsFromPendingList,this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10)));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        Log.d("NFRA","zzzzz * 4");
    }

    @Override
    public void onBackPressed(){
        //super.onBackPressed();  super.onBackPressed(); if you do not want the default action (finishing the current activity) to be executed.
        Intent intent = new Intent(this,HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // call this to finish the current activity
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void AcceptPropositionListener(String friendId) {
        api.postFriendRequestDecision(friendId,true,this);
    }

    @Override
    public void DeclinePropositionListener(String friendId) {
        api.postFriendRequestDecision(friendId,false,this);
    }

    @Override
    public void getFriendProfileByIdFromPendingFriends_NotifyWhenGetFinish(Integer integer) {
        if(integer == 1){
            Log.d(TAG,"Fetched successfully friend profile");
            adapter.notifyDataSetChanged();
        }else{
            Log.d(TAG,"Failed to fetch friend profile");
        }
    }

    @Override
    public void getFriendProfileByIdFromPendingFriends_GetResponse(JSONObject responseObject) {
        System.out.println("Response object : " + responseObject);
        friendsFromPendingList.clear();
        try {
            if(responseObject.toString() != null){
                String ln = responseObject.getString("lastName");
                String fn = responseObject.getString("firstName");
                friendsFromPendingList.put(responseObject.getString("id"),ln + " " + fn.replace(fn.charAt(0),String.valueOf(fn.charAt(0)).toUpperCase().charAt(0)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getFriendProfileByIdFromPendingFriends_GetResponse(JSONArray responseArray) {

    }

    @Override
    public void postFriendRequestDecision_NotifyWhenGetFinished(Boolean b) {
        if(b == true){
            Toast.makeText(this,"Your request decision is executed",Toast.LENGTH_SHORT).show();
            if(getIntent() != null){
                Bundle bundle = getIntent().getExtras();
                if(bundle != null){
                    //friendsFromPendingList.put(bundle.getString("fromFriendId"),)
                    api.findFriendProfileByIdFromPendingFriends(bundle.getString("fromFriendId"),this);
                }
            }
        }else{
            Toast.makeText(this,"Your request decision cannot be executed! Please retry later",Toast.LENGTH_SHORT).show();
        }
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration{

        private int spacing;
        private int spanCount;

        public GridSpacingItemDecoration(int spacing,int spanCount){
            this.spacing = spacing;
            this.spanCount = spanCount;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            outRect.left = spacing;
            outRect.right = spacing;
            if (position < spanCount) { // top edge
                outRect.top = spacing;
            }

            outRect.bottom = spacing;
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


}
