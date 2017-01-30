package com.start_up.dev.apilinkus;


import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.start_up.dev.apilinkus.API.APIGetAlbumByAlbumId_Observer;
import com.start_up.dev.apilinkus.API.APIGetUserProfileDetails_Observer;
import com.start_up.dev.apilinkus.API.APILinkUS;
import com.start_up.dev.apilinkus.Fragments.AboutUsFragment;
import com.start_up.dev.apilinkus.Fragments.AlbumFragment;
import com.start_up.dev.apilinkus.Fragments.AlbumFragment.OnMomentSelectedListener;
import com.start_up.dev.apilinkus.Fragments.CircleFragment;
import com.start_up.dev.apilinkus.Fragments.CreateGroupFragment;
import com.start_up.dev.apilinkus.Fragments.HomeFragment;
import com.start_up.dev.apilinkus.Fragments.HomeFragment.OnPostSelectedListener;
import com.start_up.dev.apilinkus.Fragments.NotificationFragment;
import com.start_up.dev.apilinkus.Fragments.OwnedAlbumsFragment.OnOwnedAlbumSelectedListener;
import com.start_up.dev.apilinkus.Fragments.ParametreFragment;
import com.start_up.dev.apilinkus.Fragments.ParametreFragment.OnChangeUserInformationListener;
import com.start_up.dev.apilinkus.Fragments.PrivacyPolicyFragment;
import com.start_up.dev.apilinkus.Fragments.ProfileFragment;
import com.start_up.dev.apilinkus.Fragments.ReportProblemFragment;
import com.start_up.dev.apilinkus.Fragments.SharedAlbumsFragment.OnSharedAlbumSelectedListener;
import com.start_up.dev.apilinkus.Fragments.SlideshowDialogFragment;
import com.start_up.dev.apilinkus.Model.Album;
import com.start_up.dev.apilinkus.Model.Authentification;
import com.start_up.dev.apilinkus.Model.DBHandler;
import com.start_up.dev.apilinkus.Model.Instant;
import com.start_up.dev.apilinkus.Model.Moment;
import com.start_up.dev.apilinkus.Model.Subscription;
import com.start_up.dev.apilinkus.Service.DateUtil;
import com.start_up.dev.apilinkus.Tool.JsonDateDeserializer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.System.out;


/**
 * Created by Vignesh on 1/15/2017.
 */

public class HomeActivity extends AppCompatActivity implements OnNavigationItemSelectedListener,
        OnSharedAlbumSelectedListener,OnOwnedAlbumSelectedListener,
        OnMomentSelectedListener,
        OnPostSelectedListener,
        OnChangeUserInformationListener,
        CircleFragment.onCircleInteraction,
        CreateGroupFragment.onCreateGroupInteraction {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgProfile;
    private LinearLayout layoutNavHeaderBg;
    private TextView txtName, txtCurrentLocalation;
    private Toolbar toolbar;
    private CoordinatorLayout coordinatorLayout;

    /*BUNDLE FOR CERTAIN FRAGMENTS*/

    private ArrayList<Moment> moments;

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
    private static final String TAG_ABOUT_US = "A propos de Linkus";
    private static final String TAG_PRIVACY_POLICY = "Politique de confidentialité";
    private ArrayList<Album> albums;
    public static String userId;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;
    private TextView toolbarTitle;
    private BottomBar bottomBar;

    private APILinkUS api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "HomeActivity landscape On create");
        setContentView(R.layout.activity_main);

        api = new APILinkUS();
        albums = new ArrayList<>();
        toolbar = (Toolbar) findViewById(R.id.toolbarMain);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        setSupportActionBar(toolbar);

        View view = getLayoutInflater().inflate(R.layout.custom_action_bar_v2, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);

        toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(CURRENT_TAG);

        getSupportActionBar().setCustomView(view, params);
        getSupportActionBar().setDisplayShowCustomEnabled(true); //show custom title
        getSupportActionBar().setDisplayShowTitleEnabled(false); //hide the default title

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
                switch (tabId) {
                    case R.id.action_favorites:
                        break;
                    case R.id.action_recents:
                        break;
                    case R.id.action_notifications:
                        break;

                }
            }
        });

        bottomBar.setVisibility(View.GONE);

        //  ActionBarDrawerToggle permettra de gérer le comportement de votre Drawer lors de sa fermeture et de son ouverture.
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
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

        //obtain  Intent Object send  from SenderActivity
        Intent intent = this.getIntent();

        /* Obtain String from Intent  */
        if(intent !=null && intent.getExtras()!=null) {
            String strdata = intent.getExtras().getString("Uniqid");
            if (strdata != null && strdata.equals("From SendMomentActivity")) {
                //Do Something here...
                callAlbumFragment(intent.getExtras().getString("albumId"));
            }
        }

        // If the savedInstanceState is eq. to null == when the fragment is not reconstructed from a previous saved state, the home fragment will be loaded
        if (savedInstanceState == null) {
            Log.d(TAG, "HomeActivity landscape Saved Instance");
            navItemIndex = 1;
            //CURRENT_TAG = TAG_ACCUEIL;
            CURRENT_TAG = TAG_PROFIL;
            loadHomeFragment();
        }

        //
        if (CURRENT_TAG.contentEquals("Instants")) {
            //  Cache le toolbar au moment d'afficher l'image en diapo
            toolbar.setVisibility(View.GONE);
            // Cache le bottom bar au moment d'afficher l'image en diapo
            bottomBar.setVisibility(View.GONE);
        }

        // Envoi de notifications
        api.sendTokenNotification();

        //setRepeatingAsyncTask();
    }

    public void callAlbumFragment(String albumId){
            Log.d(TAG, "callAlbumFragment AlbumId " + albumId);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);

            // Set the CURRENT_TAG
            CURRENT_TAG = "Albums";

            // The user selected the album from the lists of albums to glance at
            // Plus tard inclure la position
            // Check if the fragment to be shown is already present in the fragment backstack
                Fragment fragment = new AlbumFragment();
                Bundle args = new Bundle();
                args.putString("albumId",albumId);

                fragment.setArguments(args);

                // Ajoutez le nouveau fragment (Dans ce cas précis, un fragment est déjà affiché à cet emplacement, il faut donc le remplacer et non pas l'ajouter)
                fragmentTransaction.replace(R.id.frame, fragment);

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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, CURRENT_TAG);
        toolbarTitle.setText(CURRENT_TAG);
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        Log.d(TAG, "HomeActivity landscape loadHomeFragment");
        Log.d(TAG, "HomeActivity landscape loadHomeFragment " + CURRENT_TAG);
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
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);

                // Get the appropriate fragment
                Fragment fragment = getAppropriateFragment();
                // Replace the previous fragment by the new fragment
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                // Ajoutez la transaction à la backstack pour la dépiler quand l'utilisateur appuiera sur back
                fragmentTransaction.addToBackStack(CURRENT_TAG);
                // Faites le commit
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // Closing drawer on item click
        drawer.closeDrawer(GravityCompat.START);

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    // Getting the appropriate fragment
    private Fragment getAppropriateFragment() {
        Bundle bundle;
        switch (navItemIndex) {
            case 0:
                HomeFragment homeFragment = new HomeFragment();
                bundle = new Bundle();
                bundle.putSerializable("recent_instants", moments.get(0).getInstantList());
                homeFragment.setArguments(bundle);
                return homeFragment;
            case 1:
                ProfileFragment profilFragment = new ProfileFragment();
                return profilFragment;
            case 2:
                CircleFragment circleFragment = new CircleFragment();
                return circleFragment;
            case 3:
                NotificationFragment notificationFragment = new NotificationFragment();
                return notificationFragment;
            case 4:
                // Parametres
                bottomBar.setVisibility(View.GONE);
                ParametreFragment parametreFragment = new ParametreFragment();
                return parametreFragment;
            case 5:
                AboutUsFragment aboutUsFragment = new AboutUsFragment();
                return aboutUsFragment;
            default:
                break;
        }

        return new HomeFragment();
    }



    /*Selecting the index in the navigation's menu*/
    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }


    /*Setting the action bar's title*/
    private void setActionBarTitle() {
        Log.d(HomeActivity.class.getSimpleName(), "SetActionBarTitle");
        toolbarTitle.setText(activityTitles[navItemIndex]);
    }

    /*Loading header background 3*/
    private void loadHeaderBackgroundImage(String url) {
        Glide.with(this).load(url)
                .asBitmap().into(new SimpleTarget<Bitmap>(layoutNavHeaderBg.getWidth(), layoutNavHeaderBg.getHeight()) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Drawable drawable = new BitmapDrawable(getResources(), resource);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    layoutNavHeaderBg.setBackground(drawable);
                }
            }
        });
    }

    /*Loading header profile image*/
    private void loadProfileImage(String url) {
        Glide.with(this).load(url)
                .crossFade()
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);
    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        // Pour mes tests
        for (int i = 0; i < count; i++) {
            System.out.println("Tags in BackStack " + getSupportFragmentManager().getBackStackEntryAt(i));
        }

        if(count == 1){
            api.logout();
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // call this to finish the current activity
        }

        if (getSupportFragmentManager().getBackStackEntryCount() >= 2) {
            count = getSupportFragmentManager().getBackStackEntryCount();
            CURRENT_TAG = getSupportFragmentManager().getBackStackEntryAt(count - 2).getName();
            Log.d(TAG, "CURRENTLY DISPLAYED TAG = " + CURRENT_TAG);
            if (!CURRENT_TAG.contentEquals("Instants")) {
                // Etre sur que le toolbar et le bottomBar st bien visibles
                toolbar.setVisibility(View.VISIBLE);
                //bottomBar.setVisibility(View.VISIBLE);
                toolbarTitle.setText(CURRENT_TAG);
            } else {
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
        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.menu_profile_fragment, menu);
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
        switch (id) {
            case R.id.nav_home:
                navItemIndex = 0;
                CURRENT_TAG = TAG_ACCUEIL;
                break;
            case R.id.nav_profil:
                navItemIndex = 1;
                CURRENT_TAG = TAG_PROFIL;
                break;
            case R.id.nav_relatives:
                navItemIndex = 2;
                CURRENT_TAG = TAG_PROCHES;
                break;
            case R.id.nav_notifications:
                navItemIndex = 3;
                CURRENT_TAG = TAG_NOTIFICATIONS;
                break;
            case R.id.nav_settings:
                navItemIndex = 4;
                CURRENT_TAG = TAG_PARAMETRES;
                break;
            case R.id.nav_about_us:
                navItemIndex = 5;
                CURRENT_TAG = TAG_ABOUT_US;
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_logout:
            case 6:
                drawer.closeDrawer(GravityCompat.START);
                api.logout();
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // call this to finish the current activity
                break;
            default:
                navItemIndex = 0;
        }

        Log.d(TAG, "Current TAG is " + CURRENT_TAG + " NavItemIndex " + navItemIndex);

        if (item.isChecked()) {
            item.setChecked(false);
        } else {
            item.setChecked(true);
        }

        item.setChecked(true);

        if (id == R.id.nav_about_us) {
            item.setChecked(false);
        }

        loadHomeFragment();

        return true;
    }



    @Override
    public void onOwnedAlbumSelected(String albumId) { callAlbumFragment(albumId); }

    @Override
    public void onSharedAlbumSelected(String albumId) {
        callAlbumFragment(albumId);
    }

   @Override
    public void onMomentSelected(Moment moment) {
        ArrayList<Instant> instants = moment.getInstantList();

        if(instants == null || instants.isEmpty()){
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Le moment ne contient aucun instants enregistrés", Snackbar.LENGTH_LONG);
            snackbar.show();
        }else{
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);

            Bundle bundle = new Bundle();
            Log.d(TAG, "Moment name = " + moment.getName());
            bundle.putSerializable("instants", instants);

            // Set the current tag
            CURRENT_TAG = "Instants";

            // Check if the fragment to be shown is already present in the fragment backstack
            Fragment fragment = new SlideshowDialogFragment();
            fragment.setArguments(bundle);

            //  Cache le toolbar au moment d'afficher l'image en diapo
            toolbar.setVisibility(View.GONE);

            // Cache le bottom bar au moment d'afficher l'image en diapo
            bottomBar.setVisibility(View.GONE);

            // Ajoutez le nouveau fragment (Dans ce cas précis, un fragment est déjà affiché à cet emplacement, il faut donc le remplacer et non pas l'ajouter)
            fragmentTransaction.replace(R.id.frame, fragment);

            // Ajoutez la transaction à la backstack pour la dépiler quand l'utilisateur appuiera sur back
            fragmentTransaction.addToBackStack(CURRENT_TAG);

            // Faites le commit
            fragmentTransaction.commit();
        }
    }

    @Override
    public void momentFragmentOnClickButtonUpload(String albumId) {
        Intent intent = new Intent(HomeActivity.this, GalleryActivity.class);
        intent.putExtra("albumId",albumId);
        //Start details activity
        startActivity(intent);
    }

    @Override
    public void onPostSelected(int position, View view) {
        Toast.makeText(this, "OK COOL LA PAGE d'ACCUEIL EST IMPLEMENTEE ! View id " + view.getId(), Toast.LENGTH_SHORT).show();
    }

    // Taken from stackOverFlow
    private void setRepeatingAsyncTask() {

        final Handler handler = new Handler();
        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            SimulationAsyncTask simulationTask = new SimulationAsyncTask();
                            simulationTask.execute();
                        } catch (Exception e) {
                            // error, do something
                        }
                    }
                });
            }
        };


        TimerTask task2 = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            // Plus tard pour recupérer les albums et store en cache
                        } catch (Exception e) {
                            // error, do something
                        }
                    }
                });
            }
        };

        timer.schedule(task, 0, 60 * 1000);  // interval of one minute
        timer.schedule(task2, 0, 5 * 1000); // interval of five seconds
    }



    @Override
    public void onChangeUserInformation(String key, String[] value) {
        if (key.contentEquals("Fullname")) {
            api.changeUserFullName(value[0], value[1]);
        } else {
            api.changeUsernameWhichisEquivalentToTheUserEmail(value[0]);
        }
    }

    @Override
    public void onUpdateSubscription(String type, int length, String unit) {

        Subscription subscription = new Subscription(type, userId, DateUtil.getCurrentDate());

        if (type.contentEquals("FRIEND")) {
            if (length == 1 && unit.contentEquals("YEAR")) {
                api.updateSubscription(subscription, "10");
            } else if (length == 6 && unit.contentEquals("MONTHS")) {
                api.updateSubscription(subscription, "11");
            }
        } else if (type.contentEquals("DESCRIPTION")) {
            if (length == 1 && unit.contentEquals("YEAR")) {
                api.updateSubscription(subscription, "1");
            } else if (length == 6 && unit.contentEquals("MONTHS")) {
                api.updateSubscription(subscription, "2");
            }
        }
    }

    @Override
    public void onDeleteSubscription(String type, int length, String unit) {}

    @Override
    public void createGroupFragmentOnButtonCreateGroup () {
        //onBackPressed();
    }

    @Override
    public void circleFragmentOnButtonCreateGroup() {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);

            // Set the CURRENT_TAG
            CURRENT_TAG = "CreateGroup";

            // The user selected the album from the lists of albums to glance at
            // Plus tard inclure la position
            // Check if the fragment to be shown is already present in the fragment backstack
            Fragment fragment = fragmentManager.findFragmentByTag("CreateGroup");
            // Si le fragment n'existe pas, il faut le créer
            if (fragment == null) {
                Log.d(TAG, "New createGroup fragment");
                fragment = new CreateGroupFragment();
                Bundle args = new Bundle();
                fragment.setArguments(args);
                // Ajoutez le nouveau fragment (Dans ce cas précis, un fragment est déjà affiché à cet emplacement, il faut donc le remplacer et non pas l'ajouter)
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
            } else {
                Log.d(TAG, "Retour onAlbumSelected");
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
    public void onPressReportaProblemPage () {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);

         // Set the current tag
        CURRENT_TAG = "Signaler un problème";

        //Set the toolbar name
        toolbarTitle.setText(CURRENT_TAG);

        // Check if the fragment to be shown is already present in the fragment backstack
        Fragment fragment = new ReportProblemFragment();

        // Ajoutez le nouveau fragment (Dans ce cas précis, un fragment est déjà affiché à cet emplacement, il faut donc le remplacer et non pas l'ajouter)
        fragmentTransaction.replace(R.id.frame, fragment);

        // Ajoutez la transaction à la backstack pour la dépiler quand l'utilisateur appuiera sur back
        fragmentTransaction.addToBackStack(CURRENT_TAG);

        // Faites le commit
        fragmentTransaction.commit();
    }


    @Override
    public void onPressPrivacyPolicyPage () {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);

            // Set the current tag
            CURRENT_TAG = "Politique de confidentialité";

            //Set the toolbar name
            toolbarTitle.setText(CURRENT_TAG);

            // Check if the fragment to be shown is already present in the fragment backstack
            Fragment fragment = new PrivacyPolicyFragment();

            // Ajoutez le nouveau fragment (Dans ce cas précis, un fragment est déjà affiché à cet emplacement, il faut donc le remplacer et non pas l'ajouter)
            fragmentTransaction.replace(R.id.frame, fragment);

            // Ajoutez la transaction à la backstack pour la dépiler quand l'utilisateur appuiera sur back
            fragmentTransaction.addToBackStack(CURRENT_TAG);

            // Faites le commit
            fragmentTransaction.commit();
    }

    // Simulation de l'upload de plusiuers instants toutes les minutes
    class SimulationAsyncTask extends AsyncTask<Void, Void, Void> {

            Instant instant;

            @Override
            protected Void doInBackground(Void... voids) {
                instant = new Instant();
                instant.setName("William and Kate end India trip with historic Taj Mahal ");
                instant.setUrl("http://www.todayonline.com/sites/default/files/styles/photo_gallery_image_lightbox/public/photos/43_images/screen_shot_2016-04-16_at_20.32.45.png?itok=Qnz0OmCj");
                instant.setPublishDate(DateUtil.getCurrentDate());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                HomeFragment fragment;
                if ((fragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(CURRENT_TAG)) != null) {
                    fragment.synchroniseRecentlyPostedInstants(instant);
                }

            }
        }


}

















































/**
 * Adding few AlbumTestModels for testing
 */
  /*  private ArrayList<Album> prepareAlbumTestModels() {
        ArrayList<Album> albums = new ArrayList<>();
        Album a = new Album();

        //Etape 1 : Creation d'un album
        a.setActive(true);
        a.setName("Trip to India");
        a.setId("A001");
        a.setOwnerId("001");
        a.setCountryName("India");
        a.setPlaceName("Mumbai");
        a.setThumbnail(R.drawable.india);
        Date date = DateUtil.getCurrentDate();
        Log.d(TAG, "Current Date: " + date.toString());
        a.setBeginDate(date);
        a.setEndDate(DateUtil.addDays(date, 90)); // 3 mois a peu près

        //Etape 2 : Création de deux moments
        Moment moment_first = new Moment();
        moment_first.setId("A001M001");
        moment_first.setName("Visit of the palace Taj Mahal");

        Moment moment_second = new Moment();
        moment_second.setId("A001M002");
        moment_second.setName("Dromedary ride in the desert of Rajasthan");

        //Etape 3 : Creation de deux instants
        ArrayList<Instant> instants_first = new ArrayList<>();
        ArrayList<Instant> instants_second = new ArrayList<>();
        Instant instant_first = new Instant();

        instant_first.setName("Visiting Taj Mahal with Cxxx");
        instant_first.setUrl("http://whc.unesco.org/uploads/thumbs/site_0252_0008-750-0-20151104113424.jpg");
        instants_first.add(instant_first);
        instant_first.setPublishDate(DateUtil.getCurrentDate());
        moment_first.setInstantList(instants_first);

        Instant instant_second = new Instant();
        instant_second.setName("Fun Ride with dromedaries");
        instant_second.setPublishDate(DateUtil.getCurrentDate());
        instants_second.add(instant_second);
        instant_second.setUrl("http://hubchi.com/wp-content/uploads/2015/08/that-desert-tour-4.jpg");
        moment_second.setInstantList(instants_second);

        //Ajout
        ArrayList<Moment> moments = new ArrayList<>();
        moments.add(moment_first);
        moments.add(moment_second);
        a.setMoments(moments);
        albums.add(a);

        Album b = new Album();
        b.setActive(true);
        b.setName("Trip to Australia");
        b.setId("2");
        b.setOwnerId("001");
        b.setCountryName("Australia");
        b.setPlaceName("Sydney");
        b.setThumbnail(R.drawable.australia);
        date = DateUtil.getCurrentDate();
        Log.d(TAG, "Current Date: " + date.toString());
        b.setBeginDate(date);
        b.setEndDate(DateUtil.addDays(date, 90)); // 3 mois a peu près
        albums.add(b);

        Album c = new Album();
        c.setActive(true);
        c.setName("Trip to Malaysia");
        c.setId("3");
        c.setOwnerId("001");
        c.setCountryName("Malaysia");
        c.setPlaceName("Kualampur");
        date = DateUtil.getCurrentDate();
        Log.d(TAG, "Current Date: " + date.toString());
        c.setThumbnail(R.drawable.malaysia);
        c.setBeginDate(date);
        c.setEndDate(DateUtil.addDays(date, 90)); // 3 mois a peu près
        albums.add(c);

        Album d = new Album();
        d.setActive(true);
        d.setName("Trip to New Zealand");
        d.setId("4");
        d.setOwnerId("001");
        d.setCountryName("New Zealand");
        d.setPlaceName("Auckland");
        d.setThumbnail(R.drawable.newzeland);
        date = DateUtil.getCurrentDate();
        Log.d(TAG, "Current Date: " + date.toString());
        d.setBeginDate(date);
        d.setEndDate(DateUtil.addDays(date, 90)); // 3 mois a peu près
        albums.add(d);

        return albums;
    }*/


















































