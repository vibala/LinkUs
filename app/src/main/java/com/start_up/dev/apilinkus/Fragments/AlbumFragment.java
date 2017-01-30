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
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.start_up.dev.apilinkus.API.APIGetAlbumByAlbumId_Observer;
import com.start_up.dev.apilinkus.API.APILinkUS;
import com.start_up.dev.apilinkus.Adapter.MomentsAdapter;
import com.start_up.dev.apilinkus.HomeActivity;
import com.start_up.dev.apilinkus.Listener.RecyclerViewClickListener;
import com.start_up.dev.apilinkus.Model.Album;
import com.start_up.dev.apilinkus.Model.Moment;
import com.start_up.dev.apilinkus.R;
import com.start_up.dev.apilinkus.Tool.JsonDateDeserializer;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by Vignesh on 1/16/2017.
 */

public class AlbumFragment extends Fragment implements RecyclerViewClickListener,APIGetAlbumByAlbumId_Observer{

    private RecyclerView recyclerView;
    private MomentsAdapter adapter;
    // A remplacer par les moments
    private ArrayList<Moment> moments = new ArrayList<>();
    private String TAG = AlbumFragment.class.getSimpleName();
    private View momentView;
    OnMomentSelectedListener mCallback;
    private Album selectedAlbum;
    private String userId;
    private APILinkUS api;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        momentView = inflater.inflate(R.layout.content_moment_recyclerview_layout,container,false);
        api = new APILinkUS();
        return momentView;
    }


    @Override
    public void albumByAlbumId_GetResponse(JSONObject responseObject) {
        //Le gson ne gere pas le format date de base il faut contourner le bail avec une classe JsonFateDeserializer ou Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create(); (not tested)
        Gson gson=new GsonBuilder().registerTypeAdapter(Date.class, new JsonDateDeserializer()).create();

        Log.d("response",responseObject.toString());
        selectedAlbum = gson.fromJson(responseObject.toString(), Album.class);
        clearData();

        moments.clear();
        Log.d(TAG,"Moment size "  + moments.size());

        moments.addAll(selectedAlbum.getMoments());
        Log.d(TAG,"Moment n 1 "  + moments.get(0).getName());
    }

    @Override
    public void albumByAlbumId_NotifyWhenGetFinish(Integer result) {
        if(result == 1){
            Log.d(TAG,"Synchronisation ok");
            if(moments == null ||moments.isEmpty()){
               Toast.makeText(getContext(),"L'album ne contient aucun moments",Toast.LENGTH_SHORT).show();
            }else{
                Log.d(TAG,"Moments size " + moments.size());
                adapter.notifyDataSetChanged();
            }
        }else{
            try {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Failed to synchronize ! Please retry later", Toast.LENGTH_SHORT).show();
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userId = HomeActivity.userId;
        Log.d(TAG,"User id " + userId);
        if(savedInstanceState != null){
            selectedAlbum = (Album) savedInstanceState.getSerializable("selected_album");
            moments = selectedAlbum.getMoments();
        }else{
            Log.d(TAG,"Album id value " + getArguments().getString("albumId"));
            api.getAlbumByAlbumId(this,getArguments().getString("albumId"));
        }

        Button uploadButton = (Button) momentView.findViewById(R.id.upload_button);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //L'album peut ne pas avoir encore été téléchargé
                if(selectedAlbum!=null) {
                    mCallback.momentFragmentOnClickButtonUpload(selectedAlbum.getId());
                }
            }
        });
        recyclerView = (RecyclerView) momentView.findViewById(R.id.moment_recyclerView);
        // 2nd Arguments refer to the number of columns in the grid
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),1);
        recyclerView.setLayoutManager(layoutManager);
        // Item decorations can affect both measurement and drawing of individual item views
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(dpToPx(10),1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        Log.d(TAG,"Moments size in on activity created " + moments.size());
        adapter = new MomentsAdapter(getContext(),moments,this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("selected_album",selectedAlbum);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnMomentSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {
        mCallback.onMomentSelected(selectedAlbum.getMoments().get(position));
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


    public interface OnMomentSelectedListener{
        void onMomentSelected(Moment moment);
        void momentFragmentOnClickButtonUpload(String albumId);
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

}
