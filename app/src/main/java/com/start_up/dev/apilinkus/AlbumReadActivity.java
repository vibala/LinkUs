package com.start_up.dev.apilinkus;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.start_up.dev.apilinkus.API.APIGetAlbumsFilterRight_Observer;
import com.start_up.dev.apilinkus.API.APILinkUS;
import com.start_up.dev.apilinkus.Tool.DataAdapterAlbum;
import com.start_up.dev.apilinkus.Tool.GridItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static java.lang.String.valueOf;

/**
 * Created by Huong on 18/12/2016.
 */

public class AlbumReadActivity extends AppCompatActivity implements APIGetAlbumsFilterRight_Observer {
    private static final String TAG = AlbumActivity.class.getSimpleName();

    private ProgressBar mProgressBar;
    private ArrayList<GridItem> gridItems;
    private DataAdapterAlbum adapter;
    private RecyclerView recyclerView;
    private int albumRelativeTargeted = 0; //La fonction retourne une liste d'album (on s'en fou des id) et 0 correspondrait donc au premier album retourné
    //private String FEED_URL = "http://javatechig.com/?json=get_recent_posts&count=45";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_1_read);
        mProgressBar = (ProgressBar) findViewById(R.id.album_progressBar);
        mProgressBar.setVisibility(View.VISIBLE);
        initViews();

        //Start download
        new APILinkUS().getAlbumsFilter(this, "lecture");
    }

    private void initViews() {

        recyclerView = (RecyclerView) findViewById(R.id.album_card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(layoutManager);

        gridItems = new ArrayList<GridItem>();
        adapter = new DataAdapterAlbum(getApplicationContext(), gridItems);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void albumsFilterRight_GetResponse(JSONArray responseArray) {
        try {
            System.out.println(responseArray);
            JSONObject album = responseArray.optJSONObject(albumRelativeTargeted);
            JSONArray moments = album.optJSONArray("moments");
            GridItem item;
            for (int i = 0; i < moments.length(); i++) {
                JSONObject moment = moments.optJSONObject(i);
                String title = moment.getString("name");
                item = new GridItem();
                item.setTitle(title);
                item.setImage(moment.getString("url"));

                gridItems.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void albumsFilterRight_GetResponse(JSONObject responseObject) {

    }

    @Override
    public void albumsFilterRight_NotifyWhenGetFinish(Integer result) {

        if (result == 1) {
            adapter.setGridData(gridItems);

        } else {
            Toast.makeText(AlbumReadActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
        }

        //Hide progressbar
        mProgressBar.setVisibility(View.GONE);
    }
}

