package com.start_up.dev.apilinkus;


import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.start_up.dev.apilinkus.Fragments.HomeFragment;
import com.start_up.dev.apilinkus.Fragments.HomeFragment.OnPostSelectedListener;
import com.start_up.dev.apilinkus.Fragments.MomentFragment;
import com.start_up.dev.apilinkus.Fragments.MomentFragment.OnMomentSelectedListener;
import com.start_up.dev.apilinkus.Fragments.ProfileFragment;
import com.start_up.dev.apilinkus.Fragments.ProfileFragment.OnAlbumSelectedListener;
import com.start_up.dev.apilinkus.Fragments.SlideshowDialogFragment;
import com.start_up.dev.apilinkus.Model.MomentTestModel;

import java.util.ArrayList;

/**
 * Created by Vignesh on 1/15/2017.
 */

public class HomeActivity extends AppCompatActivity
        implements OnNavigationItemSelectedListener,OnAlbumSelectedListener,OnMomentSelectedListener,OnPostSelectedListener{

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgProfile;
    private LinearLayout layoutNavHeaderBg;
    private TextView txtName, txtCurrentLocalation;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    private static final String TAG = HomeActivity.class.getSimpleName();

    // tags used to attach the fragments
    private static final String TAG_ACCUEIL = "Accueil";
    private static final String TAG_PROFIL = "Profile";
    private static final String TAG_PROCHES = "Proches";
    private static final String TAG_NOTIFICATIONS = "Notifications";
    private static final String TAG_PARAMETRES = "Paramètres";
    public static String CURRENT_TAG = TAG_ACCUEIL;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;
    private TextView toolbarTitle;
    private BottomBar bottomBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbarMain);
        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        setSupportActionBar(toolbar);
        /*******************************************************************************************/
        View view = getLayoutInflater().inflate(R.layout.custom_action_bar_v2, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);

        toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(CURRENT_TAG);
        getSupportActionBar().setCustomView(view,params);
        getSupportActionBar().setDisplayShowCustomEnabled(true); //show custom title
        getSupportActionBar().setDisplayShowTitleEnabled(false); //hide the default title
        /******************************************************************************************/
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /******************************************************************************************/
        /******************** BOTTOM BAR DEFINITION ******************/
        /******************************************************************************************/
        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId){
                    case R.id.action_favorites:
                        break;
                    case R.id.action_recents:
                        break;
                    case R.id.action_notifications:
                        break;

                }
            }
        });

    /****************************************************************************************************/
        //  ActionBarDrawerToggle permettra de gérer le comportement de votre Drawer lors de sa fermeture et de son ouverture.
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };
        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(toggle);
        //calling sync state is necessary or else your hamburger icon wont show up
        toggle.syncState();

        mHandler = new Handler();
        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.user_model_name);
        txtCurrentLocalation = (TextView) navHeader.findViewById(R.id.current_location);
        layoutNavHeaderBg = (LinearLayout) navHeader.findViewById(R.id.view_container);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile_nav_drawer);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        // showing dot next to notifications label
        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);

        // If the savedInstanceState is eq. to null == when the fragment is not reconstructed from a previous saved state, the home fragment will be loaded
        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_ACCUEIL;
            loadHomeFragment();
        }

        //
        if(CURRENT_TAG.contentEquals("Instants")){
            //  Cache le toolbar au moment d'afficher l'image en diapo
            toolbar.setVisibility(View.GONE);
            // Cache le bottom bar au moment d'afficher l'image en diapo
            bottomBar.setVisibility(View.INVISIBLE);

        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d("HomeActivity",CURRENT_TAG);
        toolbarTitle.setText(CURRENT_TAG);
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment(){
        // selecting appropriate nav menu item
        selectNavMenu();

        // setting toolbar title
        setActionBarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawer(GravityCompat.START);
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable(){
            @Override
            public void run() {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);

                // Check if the fragment to be shown is already present in the fragment backstack
                Fragment fragment = fragmentManager.findFragmentByTag(CURRENT_TAG);
                // Si le fragment n'existe pas, il faut le créer
                if(fragment == null){
                    fragment = getAppropriateFragment();
                    // Ajoutez le nouveau fragment (Dans ce cas précis, un fragment est déjà affiché à cet emplacement, il faut donc le remplacer et non pas l'ajouter)
                    fragmentTransaction.replace(R.id.frame,fragment,CURRENT_TAG);
                }else{
                    // Le fragment existe déjà, il vous suffit de l'afficher
                    fragmentTransaction.show(fragment);
                }

                // Ajoutez la transaction à la backstack pour la dépiler quand l'utilisateur appuiera sur back
                fragmentTransaction.addToBackStack(CURRENT_TAG);
                // Faites le commit
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if(mPendingRunnable != null){
            mHandler.post(mPendingRunnable);
        }

        // Closing drawer on item click
        drawer.closeDrawer(GravityCompat.START);

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    // Getting the appropriate fragment
    private Fragment getAppropriateFragment(){

        switch(navItemIndex){
            case 0:
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                ProfileFragment profilFragment = new ProfileFragment();
                return profilFragment;
            case 2:
                // ---
                break;
            case 3:
                // ---
                break;
            case 4:
                // ---
                break;
            default:
                break;
        }

        return new HomeFragment();
    }

    /*Selecting the index in the navigation's menu*/
    private void selectNavMenu(){
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    /*Setting the action bar's title*/
    private void setActionBarTitle(){
        Log.d(HomeActivity.class.getSimpleName(),"SetActionBarTitle");
        toolbarTitle.setText(activityTitles[navItemIndex]);
    }

    /*Loading header background 3*/
    private void loadHeaderBackgroundImage(String url){
        Glide.with(this).load(url)
                .asBitmap().into(new SimpleTarget<Bitmap>(layoutNavHeaderBg.getWidth(),layoutNavHeaderBg.getHeight()) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Drawable drawable = new BitmapDrawable(getResources(),resource);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    layoutNavHeaderBg.setBackground(drawable);
                }
            }
        });
    }

    /*Loading header profile image*/
    private void loadProfileImage(String url){
        Glide.with(this).load(url)
                .crossFade()
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() >= 2){
            int count = getSupportFragmentManager().getBackStackEntryCount();
            Log.d(TAG,"EEEEEE " + count);
            CURRENT_TAG = getSupportFragmentManager().getBackStackEntryAt(count-2).getName();
            Log.d(TAG,"ZZZZZZZZZZ " + CURRENT_TAG);
            if(!CURRENT_TAG.contentEquals("Instants")){
                // Etre sur que le toolbar et le bottomBar st bien visibles
                toolbar.setVisibility(View.VISIBLE);
                bottomBar.setVisibility(View.VISIBLE);
                toolbarTitle.setText(CURRENT_TAG);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(navItemIndex == 0){
            getMenuInflater().inflate(R.menu.menu_profile_fragment,menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    // This method will trigger on item Click of navigation menu
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //Check to see which item was being clicked and perform appropriate action
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch(id){
            case R.id.nav_home:
                navItemIndex = 0;
                CURRENT_TAG = TAG_ACCUEIL;
                break;
            case R.id.nav_profil:
                navItemIndex = 1;
                CURRENT_TAG = TAG_PROFIL;
                break;
            case R.id.nav_relatives:
                CURRENT_TAG = TAG_PROCHES;
                navItemIndex = 2;
                break;
            case R.id.nav_notifications:
                CURRENT_TAG = TAG_NOTIFICATIONS;
                navItemIndex = 3;
                break;
            case R.id.nav_settings:
                CURRENT_TAG = TAG_PARAMETRES ;
                navItemIndex = 4;
                break;
            case R.id.nav_about_us:
                // launch new intent instead of loading fragment
                startActivity(new Intent(HomeActivity.this,AboutUsActivity.class));
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_privacy_policy:
                // launch new intent instead of loading fragment
                startActivity(new Intent(HomeActivity.this,PrivacyPolicyActivity.class));
                drawer.closeDrawer(GravityCompat.START);
                break;
            default:
                navItemIndex = 0;
        }

        if(item.isChecked()){
            item.setChecked(false);
        }else{
            item.setChecked(true);
        }

        item.setChecked(true);
        loadHomeFragment();

        return true;
    }

    @Override
    public void onAlbumSelected(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);

        // Set the CURRENT_TAG
        CURRENT_TAG = "Moments";

        // The user selected the album from the lists of albums to glance at
        // Plus tard inclure la position
        // Check if the fragment to be shown is already present in the fragment backstack
        Fragment fragment = fragmentManager.findFragmentByTag("Moment");
        // Si le fragment n'existe pas, il faut le créer
        if(fragment == null){
            Log.d(TAG,"New moment fragment");
            fragment = new MomentFragment();
            Bundle args = new Bundle();
            args.putInt("Position",position);
            fragment.setArguments(args);
            // Ajoutez le nouveau fragment (Dans ce cas précis, un fragment est déjà affiché à cet emplacement, il faut donc le remplacer et non pas l'ajouter)
            fragmentTransaction.replace(R.id.frame,fragment,"Moment");
        }else{
            Log.d(TAG,"Retour onAlbumSelected");
            // Le fragment existe déjà, il vous suffit de l'afficher
            fragmentTransaction.show(fragment);
        }

        //Set the toolbar name
        toolbarTitle.setText(CURRENT_TAG);

        // Ajoutez la transaction à la backstack pour la dépiler quand l'utilisateur appuiera sur back
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        fragmentTransaction.addToBackStack(CURRENT_TAG);

        // Commit the transaction
        fragmentTransaction.commit();

    }

    @Override
    public void onMomentSelected(int position,ArrayList<MomentTestModel> moments) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
        Bundle bundle = new Bundle();
        Log.d(TAG,"Moment n°" + position);
        bundle.putSerializable("albums",moments.get(position).getListOfInstants());
        bundle.putInt("position",0);

        // Set the current tag
        CURRENT_TAG = "Instants";
        // Check if the fragment to be shown is already present in the fragment backstack
        Fragment fragment = fragmentManager.findFragmentByTag("Instants");
        // Si le fragment n'existe pas, il faut le créer
        if(fragment == null){
            fragment = new SlideshowDialogFragment();
            fragment.setArguments(bundle);
            //  Cache le toolbar au moment d'afficher l'image en diapo
            toolbar.setVisibility(View.GONE);
            // Cache le bottom bar au moment d'afficher l'image en diapo
            bottomBar.setVisibility(View.INVISIBLE);
            // Ajoutez le nouveau fragment (Dans ce cas précis, un fragment est déjà affiché à cet emplacement, il faut donc le remplacer et non pas l'ajouter)
            fragmentTransaction.replace(R.id.frame,fragment,"Instants");
        }else{
            Log.d(TAG,"Retour onMomentSelected");
            // Le fragment existe déjà, il vous suffit de l'afficher
            fragmentTransaction.show(fragment);
        }

        // Ajoutez la transaction à la backstack pour la dépiler quand l'utilisateur appuiera sur back
        fragmentTransaction.addToBackStack(CURRENT_TAG);
        // Faites le commit
        fragmentTransaction.commit();

    }

    @Override
    public void onPostSelected(int position,View view) {
        Toast.makeText(this,"OK COOL LA PAGE d'ACCUEIL EST IMPLEMENTEE ! View id " + view.getId(),Toast.LENGTH_SHORT).show();

    }
}
