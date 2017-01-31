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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
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
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG,"onAttach is get called");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
