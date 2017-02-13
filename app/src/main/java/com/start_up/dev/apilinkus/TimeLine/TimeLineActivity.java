package com.start_up.dev.apilinkus.TimeLine;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.start_up.dev.apilinkus.R;

import java.util.ArrayList;


public class TimeLineActivity extends AppCompatActivity {

    private Draw draw;
    private RecyclerView recyclerView;
    private ObservableHorizontalScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        //-------------BUTTON-----------------------
        FloatingActionButton buttonChangeMode=(FloatingActionButton)findViewById(R.id.button_change_mode);
        buttonChangeMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                draw.switchChangeMode();
            }
        });

        //-------------DRAW-----------------------
        scrollView=(ObservableHorizontalScrollView) findViewById(R.id.horizontalScroll);
        scrollView.setHorizontalScrollBarEnabled(false);

        ViewTreeObserver vto = scrollView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                scrollView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                Log.e("onGlobalLayout child",scrollView.getChildAt(0).toString());
                Log.e("onGlobalLayout ",Integer.toString(scrollView.getChildAt(0).getMeasuredWidth()));
                Log.e("onGlobalLayout ",Integer.toString(getWindowManager().getDefaultDisplay().getWidth()));
                Log.e("onGlobalLayout Scroll",Integer.toString(scrollView.getChildAt(0).getMeasuredWidth()-getWindowManager().getDefaultDisplay().getWidth()));
                ObservableHorizontalScrollView.MAX_DELTA=(double)(scrollView.getChildAt(0).getMeasuredWidth()-getWindowManager().getDefaultDisplay().getWidth())/Draw.YEAR.NUMBER;
            }
        });
        //scrollView = new ObservableHorizontalScrollView(this);

        recyclerView = (RecyclerView) findViewById(R.id.mini_caroussel_icon);
       // draw = new Draw(this,scrollView,recyclerView,getIntent().getStringArrayListExtra("listAlbumIdToLoad"));
        ArrayList<String> listAlbumIdToLoad=new ArrayList<>();
        listAlbumIdToLoad.add(getIntent().getStringExtra("albumId"));

        draw = new Draw(this,scrollView,recyclerView,listAlbumIdToLoad);
        draw.setBackgroundColor(Color.rgb(233,235,238));
        scrollView.setOnScrollListener(draw);
        scrollView.addView(draw);

    }


}

