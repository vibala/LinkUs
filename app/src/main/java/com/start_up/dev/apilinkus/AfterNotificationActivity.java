package com.start_up.dev.apilinkus;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.start_up.dev.apilinkus.API.APILinkUS;
import com.start_up.dev.apilinkus.API.APIPostMomentsInAlbum_Observer;
import com.start_up.dev.apilinkus.Adapter.MomentsAdapter;
import com.start_up.dev.apilinkus.Fragments.AlbumFragment;
import com.start_up.dev.apilinkus.Listener.RecyclerViewClickListener;
import com.start_up.dev.apilinkus.Model.Moment;
import com.start_up.dev.apilinkus.Tool.JsonDateDeserializer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by Vignesh on 1/29/2017.
 */

public class AfterNotificationActivity extends AppCompatActivity implements RecyclerViewClickListener,APIPostMomentsInAlbum_Observer {

    private RecyclerView recyclerView;
    // A remplacer par les moments
    private ArrayList<Moment> moments = new ArrayList<>();
    private String TAG = AlbumFragment.class.getSimpleName();
    AlbumFragment.OnMomentSelectedListener mCallback;
    private APILinkUS api;
    private MomentsAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_recyclerview_postnotif);

        /*Planting the toolbar*/
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        View view = getLayoutInflater().inflate(R.layout.custom_action_bar_v2, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);

        TextView toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Moments");

        getSupportActionBar().setCustomView(view, params);
        getSupportActionBar().setDisplayShowCustomEnabled(true); //show custom title
        getSupportActionBar().setDisplayShowTitleEnabled(false); //hide the default title


        /**/
        api = new APILinkUS();
        if(getIntent() != null){
            Bundle bundle = getIntent().getExtras();
            if(bundle != null){
                api.findMomentsInAlbum(bundle.getString("albumId"),bundle.getStringArrayList("listMomentId"),this);
            }else{
                Toast.makeText(this,"Failed to retrieve the album details",Toast.LENGTH_SHORT).show();
            }
        }



        /*Planting the recyclerView*/
        recyclerView = (RecyclerView)findViewById(R.id.moment_recyclerView_post_notif);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(dpToPx(10),1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new MomentsAdapter(this,moments,this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed()
    {
        //super.onBackPressed();  super.onBackPressed(); if you do not want the default action (finishing the current activity) to be executed.
        Intent intent = new Intent(this,HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // call this to finish the current activity
    }


    public void clearData(){
        Iterator<Moment> it = moments.iterator();
        int i = 0;
        while(it.hasNext()){
            it.next();
            it.remove();
            adapter.notifyItemRemoved(i);
            adapter.notifyItemRangeChanged(i, moments.size());
            i++;
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    public void recyclerViewListClicked(View v, int position) {
            Log.d(TAG,"Position du moment sélectionné " + position);
    }

    @Override
    public void getMomentInAlbum_GetResponse(JSONArray responseArray) {
        Gson gson=new GsonBuilder().registerTypeAdapter(Date.class, new JsonDateDeserializer()).create();
        if(responseArray != null){
            clearData();
            for(int i = 0; i < responseArray.length(); i++) {
                JSONObject responseObject = responseArray.optJSONObject(i);
                Moment moment = gson.fromJson(responseObject.toString(), Moment.class);
                moments.add(moment);
            }
            adapter.notifyDataSetChanged();
        }else{
            Toast.makeText(this,"Failed to retrieve the moments",Toast.LENGTH_SHORT).show();
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

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}
