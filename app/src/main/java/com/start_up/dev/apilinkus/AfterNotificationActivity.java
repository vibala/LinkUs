package com.start_up.dev.apilinkus;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.start_up.dev.apilinkus.API.APIGetNotificationMoment_Observer;
import com.start_up.dev.apilinkus.API.APILinkUS;
import com.start_up.dev.apilinkus.Adapter.MomentsAdapter;
import com.start_up.dev.apilinkus.Fragments.AlbumFragment;
import com.start_up.dev.apilinkus.Fragments.SlideshowDialogFragment;
import com.start_up.dev.apilinkus.Listener.RecyclerViewClickListener;
import com.start_up.dev.apilinkus.Model.DBHandler;
import com.start_up.dev.apilinkus.Model.Instant;
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

public class AfterNotificationActivity extends AppCompatActivity implements RecyclerViewClickListener,APIGetNotificationMoment_Observer {

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

        // On met a jour les token depuis la BD locale
        new DBHandler(this).updateAuthentificationFromDB();
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

        adapter=new MomentsAdapter(getApplicationContext(),moments,this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.moment_recyclerView_post_notif);
        // 2nd Arguments refer to the number of columns in the grid
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),1);
        recyclerView.setLayoutManager(layoutManager);
        // Item decorations can affect both measurement and drawing of individual item views
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new MomentsAdapter(getApplicationContext(),moments,this);
        recyclerView.setAdapter(adapter);

        /**/
        api = new APILinkUS();
        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                api.findSpecificMomentsInAlbum(bundle.getString("id"), this);
            } else {
                Toast.makeText(this, "Failed to retrieve the album details", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed(){
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
      /*  Log.d(TAG,"Position du moment sélectionné " + position);
        ArrayList<Instant> instants = moments.get(position).getInstantList();

        if(instants == null || instants.isEmpty()){
          Toast.makeText(this, "Le moment ne contient aucun instants enregistrés", Toast.LENGTH_SHORT);

        }else{
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);

            Bundle bundle = new Bundle();
            Log.d(TAG, "Moment name = " + moments.get(position).getName());
            bundle.putSerializable("instants", instants);

            // Set the current tag
            String CURRENT_TAG = "Instants";

            // Check if the fragment to be shown is already present in the fragment backstack
            Fragment fragment = new SlideshowDialogFragment();
            fragment.setArguments(bundle);

            // Ajoutez le nouveau fragment (Dans ce cas précis, un fragment est déjà affiché à cet emplacement, il faut donc le remplacer et non pas l'ajouter)
            fragmentTransaction.replace(R.id.frame, fragment);

            // Ajoutez la transaction à la backstack pour la dépiler quand l'utilisateur appuiera sur back
            fragmentTransaction.addToBackStack(CURRENT_TAG);

            // Faites le commit
            fragmentTransaction.commit();
        }*/
    }

    @Override
    public void getGetNotificationMoment_GetResponse(JSONObject responseObject) {
        Gson gson=new GsonBuilder().registerTypeAdapter(Date.class, new JsonDateDeserializer()).create();
        Moment moment = gson.fromJson(responseObject.toString(), Moment.class);

        clearData();
        moments.add(moment);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();

            }
        });


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
