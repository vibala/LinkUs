package com.start_up.dev.apilinkus.Fragments;

import android.app.Activity;
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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.start_up.dev.apilinkus.API.APIGetAlbumsFilterRight_Observer;
import com.start_up.dev.apilinkus.API.APILinkUS;
import com.start_up.dev.apilinkus.Adapter.AlbumsAdapter;
import com.start_up.dev.apilinkus.HomeActivity;
import com.start_up.dev.apilinkus.Listener.RecyclerViewClickListener;
import com.start_up.dev.apilinkus.Model.Album;
import com.start_up.dev.apilinkus.Model.Authentification;
import com.start_up.dev.apilinkus.Model.IdRight;
import com.start_up.dev.apilinkus.R;
import com.start_up.dev.apilinkus.Tool.JsonDateDeserializer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by Vignesh on 1/25/2017.
 */

public class SharedAlbumsFragment extends Fragment implements RecyclerViewClickListener, APIGetAlbumsFilterRight_Observer,View.OnClickListener {

    private View myView;
    protected static final String TAG = SharedAlbumsFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private AlbumsAdapter adapter;
    private ArrayList<Album> shared_albums = new ArrayList<>();
    //private ArrayList<Album> sent_shared_albums = new ArrayList<>();;
    private OnSharedAlbumSelectedListener mCallback;
    private APILinkUS api;
    private Spinner spinner;
    private ImageButton imageButton;
    private String userId;
    private int current_selector;



    // Container Activity must implement this interface
    public interface OnSharedAlbumSelectedListener{
        void onSharedAlbumSelected(String albumId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_sharedalbums,container,false);
        api = new APILinkUS();
        spinner = (Spinner) myView.findViewById(R.id.spinner);
        imageButton = (ImageButton) myView.findViewById(R.id.filter_imagebutton);
        imageButton.setOnClickListener(this);
        recyclerView = (RecyclerView) myView.findViewById(R.id.recycler_view_fragment_sharedalbums);
        return myView;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable("shared_albums",shared_albums);
        //savedInstanceState.putSerializable("sent_shared_albums",sent_shared_albums);
        savedInstanceState.putString("userId",userId);
        savedInstanceState.putInt("current_selector",current_selector);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null) {
            shared_albums = (ArrayList<Album>) savedInstanceState.getSerializable("shared_albums");
            userId = (String) savedInstanceState.getString("userId");
            spinner.setSelection(savedInstanceState.getInt("current_selector"));
            adapter = new AlbumsAdapter(getContext(),shared_albums,this,null);

        }else {
            if(shared_albums.isEmpty()){
                api.getAlbumsFilter(this,"LECTURE");
            }

            userId = Authentification.getUserId();
            adapter = new AlbumsAdapter(getContext(),shared_albums,this,null);

        }

        /////////////////////////////////////////////////////////////////////
        // A LayoutManager is responsible for measuring and positionning items within a RecyclerView as well as determining
        // the policy when to recycle items
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });
    }


    @Override
    public void onClick(View view) {
        switch(String.valueOf(spinner.getSelectedItem())){
            case "Par droit de lecture":
                current_selector = 1;
                //System.out.println("LECTURE");
                updateSentSharedAlbums("LECTURE");
                break;
            case "Par droit de commentaire":
                current_selector = 2;
                //System.out.println("COMMENT");
                updateSentSharedAlbums("COMMENT");
                break;
            case "Par droit d écriture":
                current_selector = 3;
                //System.out.println("WRITE");
                updateSentSharedAlbums("WRITE");
                break;
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnSharedAlbumSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnAlbumSelectedListener");
        }
    }

    /***
     * Class for designing each item that will be inserted into the recyclerView
     */
    class GridSpacingItemDecoration extends RecyclerView.ItemDecoration{

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount,int spacing,boolean includeEdge){
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        /*Retrieve any offsets for the given item.
        * Need to modify this function if you want that the item decoration affects the positionning
         * of items views */
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {
        Log.d(TAG,"Position of the selected album " + position);
        // Get the album id of the selected album
        String albumId = shared_albums.get(position).getId();
        // Send the event to the host activity
        mCallback.onSharedAlbumSelected(albumId);
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    @Override
    public void albumsFilterRight_GetResponse(JSONArray responseArray) {
        System.out.println("Content of owned albums" + responseArray);
        int length = responseArray.length();
        for (int i = 0; i < length; i++) {
            JSONObject responseObject = responseArray.optJSONObject(i);
            Log.d("response", responseObject.toString());
            Album album = new Album();
            try {
                album.setId(responseObject.getString("albumId"));
                album.setName(responseObject.getString("albumName"));
                album.setImageUrl(responseObject.getString("imgUrl"));
                shared_albums.add(album);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void albumsFilterRight_NotifyWhenGetFinish(Integer result) {
        if (result == 1) {
            Toast.makeText(getActivity(),"Successfully fetching data",Toast.LENGTH_SHORT).show();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });

        } else {
            Toast.makeText(getContext(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateSentSharedAlbums(String rigth){
        switch(rigth){
            case "LECTURE":
                clearData();
                api.getAlbumsFilter(this,rigth);
                break;
            case "COMMENT":
                clearData();
                api.getAlbumsFilter(this,rigth);
                break;
            case "WRITE":
                clearData();
                api.getAlbumsFilter(this,rigth);
                break;
        }
    }

    public void clearData(){
        Iterator<Album> it = shared_albums.iterator();
        int i = 0;
        while(it.hasNext()){
            it.next();
            it.remove();
            adapter.notifyItemRemoved(i);
            adapter.notifyItemRangeChanged(i, shared_albums.size());
            i++;
        }

    }
}
