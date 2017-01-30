package com.start_up.dev.apilinkus.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.start_up.dev.apilinkus.API.APIGetAlbumByAlbumId_Observer;
import com.start_up.dev.apilinkus.API.APIGetAlbumsOwned_Observer;
import com.start_up.dev.apilinkus.API.APIGetListFriend_Observer;
import com.start_up.dev.apilinkus.API.APIGetListGroupFriend_Observer;
import com.start_up.dev.apilinkus.API.APILinkUS;
import com.start_up.dev.apilinkus.API.APIPostShareAlbumWith_Observer;
import com.start_up.dev.apilinkus.Adapter.AlbumsAdapter;
import com.start_up.dev.apilinkus.HomeActivity;
import com.start_up.dev.apilinkus.Listener.RecyclerViewClickListener;
import com.start_up.dev.apilinkus.Model.Album;
import com.start_up.dev.apilinkus.R;
import com.start_up.dev.apilinkus.Tool.JsonDateDeserializer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Vignesh on 1/25/2017.
 */

public class OwnedAlbumsFragment extends Fragment implements RecyclerViewClickListener,
        APIGetAlbumsOwned_Observer,
            AlbumsAdapter.ClickListener,
                APIGetListFriend_Observer,
                    APIGetListGroupFriend_Observer,
                        APIPostShareAlbumWith_Observer,
                            APIGetAlbumByAlbumId_Observer {

    private View myView;
    private APIPostShareAlbumWith_Observer apiPostShareAlbumWithObserver = this;
    private APIGetAlbumByAlbumId_Observer apiGetAlbumByAlbumIdObserver = this;
    protected static final String TAG = OwnedAlbumsFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private AlbumsAdapter adapter;
    private ArrayList<Album> owned_albums = new ArrayList<>();
    private OnOwnedAlbumSelectedListener mCallback;
    private APILinkUS api;
    private Album selected_album;
    private Map<String,String> list_friends = new HashMap<>();
    private Map<String,String> list_friendsgroup = new HashMap<>();
    private String scope = "";


    // Container Activity must implement this interface
    public interface OnOwnedAlbumSelectedListener{
        void onOwnedAlbumSelected(String albumId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_ownedalbums,container,false);
        Log.d(TAG,"User id " + HomeActivity.userId);
        Log.d(TAG,"oncreateView");
        api = new APILinkUS();
        recyclerView = (RecyclerView) myView.findViewById(R.id.recycler_view_fragment_ownedalbums);
        return myView;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable("owned_albums",owned_albums);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG,"onActivityCreated");
        if(savedInstanceState != null) {
            System.out.println("Je repasse");
            owned_albums = (ArrayList<Album>) savedInstanceState.getSerializable("owned_albums");
        }
        else {
            Log.d(TAG,"Owned album size " + owned_albums.size());
            if(owned_albums.isEmpty()) {
                api.getPreviewAlbumsOwned(this);
            }
        }
        adapter = new AlbumsAdapter(getContext(),owned_albums,this,this);
        /////////////////////////////////////////////////////////////////////
        // A LayoutManager is responsible for measuring and positionning items within a RecyclerView as well as determining
        // the policy when to recycle items
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new OwnedAlbumsFragment.GridSpacingItemDecoration(2, dpToPx(10), true));
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
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnOwnedAlbumSelectedListener) context;
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
        Log.d(TAG,"Position of album " + position);
        // Get the id of the selected album
        String albumId = owned_albums.get(position).getId();
        // Send the event to the host activity
        mCallback.onOwnedAlbumSelected(albumId);
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void albumsOwned_GetResponse(JSONArray responseArray) {
        System.out.println("Content of owned albums" + responseArray);
        int length = responseArray.length();
        for(int i = 0; i < length; i++){
            JSONObject responseObject = responseArray.optJSONObject(i);
            Log.d("response",responseObject.toString());
            Album album = new Album();
            try {
                album.setId(responseObject.getString("albumId"));
                album.setName(responseObject.getString("albumName"));
                album.setImageUrl(responseObject.getString("imgUrl"));
                owned_albums.add(album);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        Log.d(TAG,"Owned album " + owned_albums);
    }

    public void albumsOwned_GetResponse(JSONObject responseObject){}

    @Override
    public void albumsOwned_NotifyWhenGetFinish(Integer result) {
        if (result == 1) {
            Toast.makeText(getActivity(),"Successfully fetching data",Toast.LENGTH_SHORT).show();
            adapter.notifyDataSetChanged();

        } else {
            Toast.makeText(getContext(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view, int position) {}

    @Override
    public void OnShareOwnedAlbumListener(int position,String scope) {
        this.scope = scope;
        api.getAlbumByAlbumId(apiGetAlbumByAlbumIdObserver,owned_albums.get(position).getId(),getContext());
    }

    @Override
    public void albumByAlbumId_GetResponse(JSONObject responseObject) {
        System.out.println("Album full details " + responseObject);
        //Le gson ne gere pas le format date de base il faut contourner le bail avec une classe JsonFateDeserializer ou Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create(); (not tested)
        Gson gson=new GsonBuilder().registerTypeAdapter(Date.class, new JsonDateDeserializer()).create();
        selected_album = gson.fromJson(responseObject.toString(), Album.class);
        System.out.println("Selected album useridlist" + selected_album.getSpecificIdRight("LECTURE").getUserIdList().size());
    }

    @Override
    public void albumByAlbumId_NotifyWhenGetFinish(Integer result) {
        if(result == 1){
            if(scope.contains("friendGroup")){
                list_friendsgroup.clear();
                Log.d(TAG,"Scope Friends Group");
                api.getListGroupFriend(this);
            }else{
                list_friends.clear();
                Log.d(TAG,"Scope Friends");
                api.getListFriend(this);
            }
        }else {
            Handler mHandler = new Handler();
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(), "Failed to fetch the details about the selected album", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    @Override
    public void getListFriend_GetResponse(JSONArray responseArray) {
        try {
            System.out.println(responseArray);
            for(int i=0;i<responseArray.length();i++){
                JSONObject friend_response= responseArray.optJSONObject(i);
                // 0 : WRITE; 1: COMMENT; 2: LECTURE; 3: ADMIN
                String friend_id = friend_response.getString("id");
                Log.d(TAG,"Selected album userIdList size " + selected_album.getIdRight().size());
                if(!selected_album.getSpecificIdRight("LECTURE").getUserIdList().contains(friend_id)){
                    list_friends.put(friend_response.getString("lastName") + " "+ friend_response.getString("firstName"),friend_id);
                }
            }
            Log.d(TAG,"List friends size " + list_friends.size());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void postShareAlbumWith_Notify(Boolean result) {
        if(result){
            Toast.makeText(getContext(),"This album has been shared successfully",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getContext(),"Internal Error ! This album cannot be shared successfully",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getListFriend_NotifyWhenGetFinish(Integer result) {
        if(result == 1){
            Log.d(TAG,"Sucessfully fetching data");
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Intialize  readable sequence of char values
                    final CharSequence[] dialogList=  list_friends.keySet().toArray(new CharSequence[list_friends.size()]);
                    final AlertDialog.Builder builderDialog = new AlertDialog.Builder(getContext());
                    builderDialog.setTitle("Share with");
                    int count = dialogList.length;
                    boolean[] is_checked = new boolean[count];

                    // Creating multiple selection by using setMutliChoiceItem method
                    builderDialog.setMultiChoiceItems(dialogList, is_checked,
                            new DialogInterface.OnMultiChoiceClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton, boolean isChecked) {
                                }
                    });

                    builderDialog.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which){
                                    ListView list = ((AlertDialog) dialog).getListView();

                                    for (int i = 0; i < list.getCount(); i++) {
                                        boolean checked = list.isItemChecked(i);

                                        if (checked) {
                                           api.shareAlbumWithFriend(list_friends.get(list.getItemAtPosition(i)),selected_album.getId(),"LECTURE",apiPostShareAlbumWithObserver);
                                        }
                                    }
                                }
                            }
                    );

                    builderDialog.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    AlertDialog alert = builderDialog.create();
                    alert.show();
                }
            });
        }else{
            Handler mHandler = new Handler();
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(),"Failed to fetch your friends list",Toast.LENGTH_LONG).show();
                }
            });

        }
    }

    @Override
    public void getListGroupFriend_GetResponse(JSONArray responseArray) {
        try {
            System.out.println(responseArray);
            for(int i=0;i<responseArray.length();i++){
                JSONObject friend_response = responseArray.optJSONObject(i);
                String group_id = friend_response.getString("id");
                if(!selected_album.getSpecificIdRight("LECTURE").getGroupIdList().contains(group_id)){
                    list_friendsgroup.put(friend_response.getString("name"),group_id);
                }
            }
            Log.d(TAG,"List group of friends size " + list_friendsgroup.size());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getListGroupFriend_NotifyWhenGetFinish(Integer result) {
        if(result == 1){
            Log.d(TAG,"Sucessfully fetching data");
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Intialize  readable sequence of char values
                    final CharSequence[] dialogList=  list_friendsgroup.keySet().toArray(new CharSequence[list_friendsgroup.size()]);
                    final AlertDialog.Builder builderDialog = new AlertDialog.Builder(getContext());
                    builderDialog.setTitle("Share with");
                    int count = dialogList.length;
                    boolean[] is_checked = new boolean[count];


                    // Creating multiple selection by using setMutliChoiceItem method
                    builderDialog.setMultiChoiceItems(dialogList, is_checked,
                            new DialogInterface.OnMultiChoiceClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton, boolean isChecked) {
                                }
                            });

                    builderDialog.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which){
                                    ListView list = ((AlertDialog) dialog).getListView();

                                    for (int i = 0; i < list.getCount(); i++) {
                                        boolean checked = list.isItemChecked(i);

                                        if (checked) {
                                            Log.d(TAG,"Id : " + list_friendsgroup.get(list.getItemAtPosition(i)));
                                            api.shareAlbumWithGroupFriend(list_friendsgroup.get(list.getItemAtPosition(i)),selected_album.getId(),"LECTURE",apiPostShareAlbumWithObserver);
                                        }
                                    }


                                }
                            }
                    );

                    builderDialog.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    AlertDialog alert = builderDialog.create();
                    alert.show();
                }
            });
        }else{
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(),"Failed to fetch your group of friends list",Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void synchronizeActionLinkedtoAlbum(){
        Log.d(TAG,"SsynchronizeActionLinkedtoAlbum");
        clearData();
        api.getPreviewAlbumsOwned(this);
    }

    public void clearData(){
        Iterator<Album> it = owned_albums.iterator();
        int i = 0;
        while(it.hasNext()){
            it.next();
            it.remove();
            adapter.notifyItemRemoved(i);
            adapter.notifyItemRangeChanged(i, owned_albums.size());
            i++;
        }

    }

}
