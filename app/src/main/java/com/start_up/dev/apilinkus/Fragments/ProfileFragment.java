package com.start_up.dev.apilinkus.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.OnTabSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.start_up.dev.apilinkus.API.APIGetUserNbFriendsAndNbOwnedAlbums_Observer;
import com.start_up.dev.apilinkus.API.APIGetUserProfileDetails_Observer;
import com.start_up.dev.apilinkus.API.APILinkUS;
import com.start_up.dev.apilinkus.Adapter.ViewPagerAdapter;
import com.start_up.dev.apilinkus.Model.Album;
import com.start_up.dev.apilinkus.Model.IdRight;
import com.start_up.dev.apilinkus.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Vignesh on 1/15/2017.
 */

public class ProfileFragment extends Fragment implements OnTabSelectedListener,APIGetUserProfileDetails_Observer,APIGetUserNbFriendsAndNbOwnedAlbums_Observer {

    private View myView;
    protected static final String TAG = ProfileFragment.class.getSimpleName();
    private FrameLayout titleLayout;
    private CircularImageView circularImageView;
    private TextView nbProches_tv, nbAlbumOwned_tv,tvusername;
    private ViewPager viewPager;
    private String userId;
    private String username = "", nbProches = "", nbAlbumsOwned = "";
    private APILinkUS api;
    private FloatingActionButton fab;
    private EditText nameBox,countrynameBox,placenameBox;
    private OwnedAlbumsFragment ownedAlbumsFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.activity_userprofile,container,false);
        api = new APILinkUS();
        nbProches_tv = (TextView) myView.findViewById(R.id.tv_friends);
        nbAlbumOwned_tv = (TextView) myView.findViewById(R.id.tv_posts);
        tvusername = (TextView) myView.findViewById(R.id.tvusername);
        titleLayout = (FrameLayout) myView.findViewById(R.id.main_framelayout_title);
        circularImageView = (CircularImageView) myView.findViewById(R.id.circularImageView);
        viewPager = (ViewPager) myView.findViewById(R.id.tabanim_viewpager);
        setupViewPager(viewPager);
        TabLayout tabLayout = (TabLayout) myView.findViewById(R.id.tabanim_tabs);
        tabLayout.setupWithViewPager(viewPager);
        fab = (FloatingActionButton) myView.findViewById(R.id.fab);

        if(savedInstanceState != null){
            Log.d(TAG,"Je repasse");
            userId = savedInstanceState.getString("userId");
            username = savedInstanceState.getString("username");
            nbProches = savedInstanceState.getString("nbProches");
            nbAlbumsOwned = savedInstanceState.getString("nbAlbumsOwned");
            tvusername.setText(username);
            nbProches_tv.setText(nbProches);
            nbAlbumOwned_tv.setText(nbAlbumsOwned);
        }else{
            api.getUserProfileDetails(this,getContext()); // Pour récupérer le nom complet de l'user
            api.getNbofFriendsAndAlbumOwned(this);  // Pour récuper le nb de potos et le nb d'albums owned
        }

       return myView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("userId",userId);
        outState.putString("username",username);
        outState.putString("nbAlbumsOwned",nbAlbumsOwned);
        outState.putString("nbProches",nbProches);
        super.onSaveInstanceState(outState);
    }

    private void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        ownedAlbumsFragment = new OwnedAlbumsFragment();
        adapter.addFrag(ownedAlbumsFragment, "OWNED ALBUMS");
        SharedAlbumsFragment s = new SharedAlbumsFragment();
        adapter.addFrag(s, "SHARED ALBUMS");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
                                ownedAlbumsFragment.synchronizeActionLinkedtoAlbum();
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

        /******************************************************************************************/
        /*DONT TOUCH THIS PART !!!! IT'S FOR DISPLAYING CORRECTLY THE CIRCULARVIEW IN ANY PLATFORM*/
        /******************************************************************************************/
        int heigth = titleLayout.getHeight() * 2;
        int heigth2 = (300 - (heigth*2)) - 100;
        circularImageView.getLayoutParams().height = heigth2;
        circularImageView.getLayoutParams().width = heigth2;
        /*Setting the margin top*/
        ViewGroup.MarginLayoutParams marginParams = new ViewGroup.MarginLayoutParams(circularImageView.getLayoutParams());
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width= ((dm.widthPixels)/2) - dpToPx(heigth2/2);
        //int width2 = (width - circularImageView.getWidth())/2;
        marginParams.setMargins(width+60, dpToPx(heigth+40), 0, 0);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(marginParams);
        circularImageView.setLayoutParams(layoutParams);
        circularImageView.requestLayout();
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
    public void onTabSelected(TabLayout.Tab tab) {
        switch(tab.getPosition()){
            case 0:
                // Owned Albums
                break;
            case 1:
                // Shared Albums
                break;
        }

        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void userDetails_GetResponse(JSONObject responseJSON) {
        try {
            String fn = responseJSON.getString("firstName");
            username = responseJSON.getString("lastName").toUpperCase() + " " + fn.replace(fn.charAt(0),String.valueOf(fn.charAt(0)).toUpperCase().charAt(0));
            userId = responseJSON.getString("id");
            Log.d(TAG,"User id " + userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void userDetails_NotifyWhenGetFinish(Integer result) {
        if (result == 1) {
            Log.d(TAG,"Sucessfully fetching data");
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvusername.setText(username);
                }
            });
        } else {
            Log.d(TAG,"Failed to fetch data!");
        }
    }

    @Override
    public void userNbProchesAndOwnedAlbums_GetResponse(JSONObject responseJSON) {
        try {
            System.out.println(responseJSON);
            nbAlbumsOwned = responseJSON.getString("nbAlbumsOwned");
            nbProches = responseJSON.getString("nbProches");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void userNbProchesAndOwnedAlbums_NotifyWhenGetFinish(Integer result) {
        if (result == 1) {
            Log.d(TAG,"Sucessfully fetching data");
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    nbProches_tv.setText(nbProches);
                    nbAlbumOwned_tv.setText(nbAlbumsOwned);
                }
            });
        } else {
            Log.d(TAG,"Failed to fetch data!");
        }
    }
}
