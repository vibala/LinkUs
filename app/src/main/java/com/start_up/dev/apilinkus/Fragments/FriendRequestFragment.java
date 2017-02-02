package com.start_up.dev.apilinkus.Fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.start_up.dev.apilinkus.API.APIGetFriendProfileByIdFromPendingFriends_Observer;
import com.start_up.dev.apilinkus.API.APILinkUS;
import com.start_up.dev.apilinkus.API.APIPostFriendRequestDecision_Observer;
import com.start_up.dev.apilinkus.Adapter.NotifFriendRequestAdapter;
import com.start_up.dev.apilinkus.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Vignesh on 2/1/2017.
 */

public class FriendRequestFragment extends Fragment
        implements NotifFriendRequestAdapter.OnClickListener,
                    APIGetFriendProfileByIdFromPendingFriends_Observer,
                            APIPostFriendRequestDecision_Observer{

    private View myView;
    private RecyclerView recyclerView;
    private HashMap<String,String> friendsFromPendingList;
    private APILinkUS api;
    private final String TAG = FriendRequestFragment.class.getSimpleName();
    private NotifFriendRequestAdapter friendRequestAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.content_home_fragment,container,false);
        recyclerView = (RecyclerView) myView.findViewById(R.id.recycler_view);
        return myView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        api = new APILinkUS();
        if(savedInstanceState!= null){
            friendsFromPendingList = (HashMap<String, String>) savedInstanceState.getSerializable("friendsFromPendingList");
        }else{
            friendsFromPendingList = new HashMap<>();
            api.findFriendsNameandIdByIdFromPendingFriends(this);
        }
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(),1);
        friendRequestAdapter = new NotifFriendRequestAdapter(friendsFromPendingList,this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(15)));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(friendRequestAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("friendsFromPendingList",friendsFromPendingList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
        if(integer==1){
            Log.d(TAG,"Fetched successfuly pending friends profile");
            friendRequestAdapter.notifyDataSetChanged();
            Log.d(TAG,"Friend Request adapter " + friendRequestAdapter.getItemCount());
        }else{
            Toast.makeText(getContext(),"Failed to fetch your pending friends profile",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getFriendProfileByIdFromPendingFriends_GetResponse(JSONObject responseObject) {

    }

    @Override
    public void getFriendProfileByIdFromPendingFriends_GetResponse(JSONArray responseArray) {
        Log.d(TAG,"Response Array " + responseArray.toString());
        friendsFromPendingList.clear();
        for(int i = 0; i < responseArray.length(); i++){
            try {
                JSONObject responseObject = responseArray.getJSONObject(i);
                String firstName = responseObject.getString("firstName");
                friendsFromPendingList.put(responseObject.getString("id"),responseObject.getString("lastName")+ " " + firstName.replace(firstName.charAt(0),String.valueOf(firstName).toUpperCase().charAt(0)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.d(TAG,"friendsFromPendingList size "+ friendsFromPendingList.size());
    }

    @Override
    public void postFriendRequestDecision_NotifyWhenGetFinished(Boolean b) {
        if(b == true){
            Toast.makeText(getContext(),"Your request decision is executed",Toast.LENGTH_SHORT).show();
            api.findFriendsNameandIdByIdFromPendingFriends(this);
        }else{
            Toast.makeText(getContext(),"Your request decision cannot be executed! Please retry later",Toast.LENGTH_SHORT).show();
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
