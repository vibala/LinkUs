package com.start_up.dev.apilinkus.Fragments;

import android.app.Activity;
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
import com.start_up.dev.apilinkus.Adapter.MomentsAdapater;
import com.start_up.dev.apilinkus.HomeActivity;
import com.start_up.dev.apilinkus.Listener.RecyclerViewClickListener;
import com.start_up.dev.apilinkus.Model.Album;
import com.start_up.dev.apilinkus.Model.Instant;
import com.start_up.dev.apilinkus.Model.Moment;
import com.start_up.dev.apilinkus.R;
import com.start_up.dev.apilinkus.Service.DateUtil;
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
    private MomentsAdapater adapter;
    // A remplacer par les moments
    private ArrayList<Moment> moments;
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

    public void refresh_album_fragment(){
        // Clean recyclerview
        clearData();

        // Repaint (en vrai on appelle le refresh dans albumByAlbumId_NotifyWhenGetFinish)
        //api.getAlbumByAlbumId(this,selectedAlbum.getId()); (Décommenter cette ligne !!!!!!!!!!!!!!!!!!  UNE FOIS QUE T'AS LANCE LE PROGRAMME LA PREMIERE FOIS )

        /*RETIER CELA UNE FOIS QUE T'AS VU QUE CA MARCHE DE TON COTE - HISTOIRE D AFFICHAGE DE 3 MOMENTS SUCCESSIFS*/
        /*Etape 2 : Création de deux moments*/
        Moment moment_first = new Moment();
        moment_first.setId("A001M001");
        moment_first.setName("Visit of the palace Taj Mahal");

        Moment moment_second = new Moment();
        moment_second.setId("A001M002");
        moment_second.setName("Dromedary ride in the desert of Rajasthan");

        Moment moment_third = new Moment();
        moment_third.setId("A001M001");
        moment_third.setName("Visit of the palace Taj TEST");

        ArrayList<Instant> instants_first = new ArrayList<>();
        ArrayList<Instant> instants_second = new ArrayList<>();
        ArrayList<Instant> instants_third = new ArrayList<>();

        Instant instant_first = new Instant();
        instant_first.setName("Visiting Taj Mahal with Cxxx");
        instant_first.setUrl("http://whc.unesco.org/uploads/thumbs/site_0252_0008-750-0-20151104113424.jpg");
        instants_first.add(instant_first);
        instant_first.setPublishDate(DateUtil.getCurrentDate());
        moment_first.setInstantList(instants_first);
        moments.add(moment_first);

        Instant instant_second = new Instant();
        instant_second.setName("Fun Ride with dromedaries");
        instant_second.setPublishDate(DateUtil.getCurrentDate());
        instants_second.add(instant_second);
        instant_second.setUrl("http://hubchi.com/wp-content/uploads/2015/08/that-desert-tour-4.jpg");
        moment_second.setInstantList(instants_second);
        moments.add(moment_second);

        Instant instant_third = new Instant();
        instant_third.setName("Fun Ride with lions");
        instant_third.setPublishDate(DateUtil.getCurrentDate());
        instant_third.setUrl("http://hubchi.com/wp-content/uploads/2015/08/that-desert-tour-4.jpg");
        instants_third.add(instant_third);
        moment_third.setInstantList(instants_third);
        moments.add(moment_third);


        // GARDER CELA PARCE QU ELLE PERMET DE METTRE A JOUR L ADAPTER
        adapter.notifyDataSetChanged();
    }

    @Override
    public void albumByAlbumId_GetResponse(JSONObject responseObject) {
        //Le gson ne gere pas le format date de base il faut contourner le bail avec une classe JsonFateDeserializer ou Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create(); (not tested)
        Gson gson=new GsonBuilder().registerTypeAdapter(Date.class, new JsonDateDeserializer()).create();

        Log.d("response",responseObject.toString());
        selectedAlbum = gson.fromJson(responseObject.toString(), Album.class);
        Log.d("gson",gson.toString());
        Log.d("SELECTED ALBUM",selectedAlbum.toString());
        moments = selectedAlbum.getMoments();
    }

    @Override
    public void albumByAlbumId_NotifyWhenGetFinish(Integer result) {
        if(result == 1){
            Log.d(TAG,"Synchronisation ok");
            adapter.notifyDataSetChanged();
        }else{
            Toast.makeText(getContext(),"Failed to synchronize ! Please retry later",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userId = HomeActivity.userId;
        Log.d(TAG,"User id " + userId);

        Button uploadButton = (Button) momentView.findViewById(R.id.upload_button);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mCallback.momentFragmentOnClickButtonUpload(selectedAlbum.getId()); (DECOMMENTER EGALEMENT CELA APRES LE PREMIER LANCEMEENT)
                refresh_album_fragment();
            }
        });
        recyclerView = (RecyclerView) momentView.findViewById(R.id.moment_recyclerView);
        // 2nd Arguments refer to the number of columns in the grid
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),1);
        recyclerView.setLayoutManager(layoutManager);
        // Item decorations can affect both measurement and drawing of individual item views
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(dpToPx(10),1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if(savedInstanceState != null){
            selectedAlbum = (Album) savedInstanceState.getSerializable("selected_album");
        }else{
            selectedAlbum = (Album) getArguments().get("selected_album");
        }
        moments = selectedAlbum.getMoments() ;
        adapter = new MomentsAdapater(getContext(),moments,this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("selected_album",selectedAlbum);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnMomentSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {
        mCallback.onMomentSelected(position);

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
        void onMomentSelected(int position);
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
