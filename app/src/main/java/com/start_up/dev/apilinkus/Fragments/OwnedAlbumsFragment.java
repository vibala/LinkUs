package com.start_up.dev.apilinkus.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.start_up.dev.apilinkus.API.APIGetAlbumByAlbumId_Observer;
import com.start_up.dev.apilinkus.API.APIGetAlbumsOwned_Observer;
import com.start_up.dev.apilinkus.API.APIGetListFriend_Observer;
import com.start_up.dev.apilinkus.API.APIGetListGroupFriend_Observer;
import com.start_up.dev.apilinkus.API.APIGetUsersWithRigthInAlbum_Observer;
import com.start_up.dev.apilinkus.API.APILinkUS;
import com.start_up.dev.apilinkus.API.APIPostShareAlbumWith_Observer;
import com.start_up.dev.apilinkus.Adapter.AlbumsAdapter;
import com.start_up.dev.apilinkus.HomeActivity;
import com.start_up.dev.apilinkus.Listener.RecyclerViewClickListener;
import com.start_up.dev.apilinkus.Model.Album;
import com.start_up.dev.apilinkus.Model.Authentification;
import com.start_up.dev.apilinkus.Model.IdRight;
import com.start_up.dev.apilinkus.R;
import com.start_up.dev.apilinkus.TimeLine.TimeLineActivity;
import com.start_up.dev.apilinkus.Tool.JsonDateDeserializer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
                            APIGetAlbumByAlbumId_Observer,APIGetUsersWithRigthInAlbum_Observer {

    private View myView;
    private Activity parent_activity;
    private APIPostShareAlbumWith_Observer apiPostShareAlbumWithObserver = this;
    private APIGetAlbumByAlbumId_Observer apiGetAlbumByAlbumIdObserver = this;
    protected static final String TAG = OwnedAlbumsFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private AlbumsAdapter adapter;
    private ArrayList<Album> owned_albums = new ArrayList<>();
    private OnOwnedAlbumSelectedListener mCallback;
    private APILinkUS api = new APILinkUS();
    ;
    private Album selected_album;
    private List<String> selected_album_userIdList, selected_album_groupuserIdList;
    private Map<String, String> list_friends = new HashMap<>();
    private Map<String, String> list_friendsgroup = new HashMap<>();
    private String scope = "";
    private FloatingActionButton fab;
    private EditText nameBox, countrynameBox, placenameBox;
    private String userId = Authentification.getUserId();


    // Container Activity must implement this interface
    public interface OnOwnedAlbumSelectedListener {
        void onOwnedAlbumSelected(String albumId);

        void onOwnedTimeLineAlbumSelected(String albumId);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_ownedalbums,container,false);
        Log.d(TAG,"User id " +userId);
        Log.d(TAG,"oncreateView");
        fab = (FloatingActionButton) myView.findViewById(R.id.fab);
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
        parent_activity = getActivity();
        selected_album_userIdList = new ArrayList<>();
        selected_album_groupuserIdList = new ArrayList<>();
        Log.d(TAG,"onActivityCreated " + parent_activity);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog dialog = createDialog();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int threshold = 0;
                        //save info where you want it
                        if( nameBox.getText().toString().trim().equals(""))
                        {
                            nameBox.setError( "Album name is required!" );
                            nameBox.setHint("Album name");
                        } else {
                            threshold++;
                        }

                        if( countrynameBox.getText().toString().trim().equals(""))
                        {
                            countrynameBox.setError( "Country name is required!" );
                            countrynameBox.setHint("Country name");
                        } else {
                            threshold++;
                        }

                        if(placenameBox.getText().toString().trim().equals(""))
                        {
                            placenameBox.setError( "Place name is required!" );
                            placenameBox.setHint("Place name");
                        } else {
                            threshold++;
                        }

                        if(threshold==3){
                            dialog.dismiss();
                            Log.d(TAG,"TT EST BON");
                            Album album = new Album();
                            album.setOwnerId(userId);
                            album.setName(nameBox.getText().toString());
                            album.setCountryName(countrynameBox.getText().toString());
                            album.setPlaceName(placenameBox.getText().toString());

                            IdRight adminRight = new IdRight("ADMIN");
                            adminRight.getUserIdList().add(userId);

                            IdRight commentRight = new IdRight("COMMENT");
                            commentRight.getUserIdList().add(userId);

                            IdRight writeRight = new IdRight("WRITE");
                            writeRight.getUserIdList().add(userId);

                            IdRight lectureRight = new IdRight("LECTURE");
                            lectureRight.getUserIdList().add(userId);

                            album.getIdRight().add(writeRight);
                            album.getIdRight().add(commentRight);
                            album.getIdRight().add(lectureRight);
                            album.getIdRight().add(adminRight);

                            String result = api.createNewAlbum(album);

                            if(result.contains("200")){
                                Toast.makeText(getContext(),"Vous venez de créer un nouvel album",Toast.LENGTH_SHORT).show();
                                synchronizeActionLinkedtoAlbum(getActivity());
                            }else{
                                Toast.makeText(getContext(),"L'album n'a pas pu être créé ! Merci de retenter plus tard",Toast.LENGTH_SHORT).show();
                            }


                        }else{
                            // Do nothing
                        }

                    }
                });
            }
        });


        if(savedInstanceState != null) {
            System.out.println("Je repasse");
            owned_albums = (ArrayList<Album>) savedInstanceState.getSerializable("owned_albums");

        }
        else {
            Log.d(TAG,"Owned album size " + owned_albums.size());
            if(owned_albums.isEmpty()) {
                api.getPreviewAlbumsOwned(this,parent_activity);
            }

        }
        adapter = new AlbumsAdapter(getContext(),owned_albums,this,this,mCallback);
        /////////////////////////////////////////////////////////////////////
        // A LayoutManager is responsible for measuring and positionning items within a RecyclerView as well as determining
        // the policy when to recycle items
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new OwnedAlbumsFragment.GridSpacingItemDecoration(2, dpToPx(15), true));
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

    public AlertDialog createDialog(){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.alert_dialog_create_album,null);
        nameBox = (EditText) layout.findViewById(R.id.album_name_edit_text);
        countrynameBox = (EditText) layout.findViewById(R.id.album_place_name_edit_text);
        placenameBox = (EditText) layout.findViewById(R.id.album_country_name_edit_text);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(layout);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.create();

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG,"onAttach");
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
    public void albumsOwned_NotifyWhenGetFinish(Integer result,final Activity parent_activity) {
        if (result == 1) {
            parent_activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG,"Parent activity " + parent_activity);
                    Toast.makeText(parent_activity,"Successfully fetching data",Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                }
            });


        } else {
            Toast.makeText(parent_activity, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG,"OWNED IS GET DETACHED");
    }

    @Override
    public void OnShareOwnedAlbumListener(final int position,String scope) {
        this.scope = scope;
        selected_album = owned_albums.get(position);
        if(scope.contentEquals("friends")){
            api.findUsersWithRightInAlbum(0,owned_albums.get(position).getId(),"LECTURE",this);
        }else if(scope.contentEquals("friendGroup")){
            api.findUsersWithRightInAlbum(1,owned_albums.get(position).getId(),"LECTURE",this);
        }

        //api.getAlbumByAlbumId(apiGetAlbumByAlbumIdObserver,owned_albums.get(position).getId(),getContext());

    }

    @Override
    public void usersWithRigthInAlbum_GetResponse(String responseObject) {
          System.out.println("UserId or  GroupUserId list en lecture " + responseObject);

          if(scope.contains("friends")){
                selected_album_userIdList.clear();
                selected_album_userIdList = new ArrayList<>(Arrays.asList(responseObject.split(", ")));
                Log.d(TAG,"UserIdList Size " + selected_album_userIdList.size());
            }else if(scope.contains("friendGroup")){
                selected_album_groupuserIdList.clear();
                selected_album_groupuserIdList = new ArrayList<>(Arrays.asList(responseObject.split(", ")));
                Log.d(TAG,"GroupIdList Size " + selected_album_groupuserIdList.size());
            }
    }

    @Override
    public void usersWithRigthInAlbum_NotifyWhenGetFinish(Integer result) {
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
                if(!selected_album_userIdList.contains(friend_id)){
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
                                           Log.d(TAG,"Selected album id " + selected_album.getId());
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
               /* if(!selected_album.getSpecificIdRight("LECTURE").getGroupIdList().contains(group_id)){
                    list_friendsgroup.put(friend_response.getString("name"),group_id);
                }*/
                if(!selected_album_groupuserIdList.contains(group_id)){
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

    public void synchronizeActionLinkedtoAlbum(Activity root_activity){
        Log.d(TAG,"SynchronizeActionLinkedtoAlbum");
        clearData();
        Log.d(TAG,"Parent activity frrom synchronizeAction " + root_activity);
        api.getPreviewAlbumsOwned(this,root_activity);
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
