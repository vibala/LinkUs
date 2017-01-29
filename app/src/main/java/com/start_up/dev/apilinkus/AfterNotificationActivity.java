package com.start_up.dev.apilinkus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.start_up.dev.apilinkus.API.APILinkUS;
import com.start_up.dev.apilinkus.Adapter.MomentsAdapater;
import com.start_up.dev.apilinkus.Fragments.AlbumFragment;
import com.start_up.dev.apilinkus.Listener.RecyclerViewClickListener;
import com.start_up.dev.apilinkus.Model.Album;
import com.start_up.dev.apilinkus.Model.Moment;

import java.util.ArrayList;

/**
 * Created by Vignesh on 1/29/2017.
 */

public class AfterNotificationActivity extends AppCompatActivity implements RecyclerViewClickListener {

    private RecyclerView recyclerView;
    // A remplacer par les moments
    private ArrayList<Moment> moments = new ArrayList<>();
    private String TAG = AlbumFragment.class.getSimpleName();
    AlbumFragment.OnMomentSelectedListener mCallback;
    private String userId;
    private APILinkUS api;
    private MomentsAdapater adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_recyclerview_postnotif);
        adapter = new MomentsAdapater(this,moments,this);
        recyclerView = (RecyclerView)findViewById(R.id.moment_recyclerView_post_notif);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {

    }
}
