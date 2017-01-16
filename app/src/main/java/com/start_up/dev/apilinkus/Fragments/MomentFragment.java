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
import android.widget.ImageView;

import com.start_up.dev.apilinkus.Model.AlbumTestModel;
import com.start_up.dev.apilinkus.Model.MomentTestModel;
import com.start_up.dev.apilinkus.MomentsAdapater;
import com.start_up.dev.apilinkus.R;
import com.start_up.dev.apilinkus.Listener.RecyclerViewClickListener;

import java.util.ArrayList;

/**
 * Created by Vignesh on 1/16/2017.
 */

public class MomentFragment extends Fragment implements RecyclerViewClickListener {

    private RecyclerView recyclerView;
    private MomentsAdapater adapter;
    // A remplacer par les moments
    private ArrayList<MomentTestModel> moments;
    private String TAG = MomentFragment.class.getSimpleName();
    private ImageView dots_display_big_view;
    private View momentView;
    OnMomentSelectedListener mCallback;

    public interface OnMomentSelectedListener{
        public void onMomentSelected(int position,ArrayList<MomentTestModel> moments);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        momentView = inflater.inflate(R.layout.moment_display_layout,container,false);
        moments = new ArrayList<>();
        dots_display_big_view = (ImageView) momentView.findViewById(R.id.expand_image);
        return momentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new MomentsAdapater(getContext(),moments,this);
        recyclerView = (RecyclerView) momentView.findViewById(R.id.moment_recyclerView);
        // 2nd Arguments refer to the number of columns in the grid
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),1);
        recyclerView.setLayoutManager(layoutManager);
        // Item decorations can affect both measurement and drawing of individual item views
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(dpToPx(10),1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        prepareMomentTestModels();
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

    private void prepareMomentTestModels() {
        int[] covers = new int[]{
                R.drawable.album1,
                R.drawable.album2,
                R.drawable.album3,
                R.drawable.album4,
                R.drawable.album5,
                R.drawable.album6,
                R.drawable.album7,
                R.drawable.album8,
                R.drawable.album9,
                R.drawable.album10,
                R.drawable.album11};

        MomentTestModel moment_a = new MomentTestModel("Restaurant SydneyAussies 17/08/2018");
        MomentTestModel moment_b = new MomentTestModel("Visit to the Melbourne Cricket Statium 05/09/2018");
        ArrayList<AlbumTestModel> albumsList = new ArrayList<>(); // List of Albums

        AlbumTestModel a = new AlbumTestModel("Maroon5", 13, covers[0]);
        albumsList.add(a);

        a = new AlbumTestModel("Sugar Ray", 8, covers[1]);
        albumsList.add(a);

        a = new AlbumTestModel("Bon Jovi", 11, covers[2]);
        albumsList.add(a);

        a = new AlbumTestModel("The Corrs", 12, covers[3]);
        albumsList.add(a);

        a = new AlbumTestModel("The Cranberries", 14, covers[4]);
        albumsList.add(a);

        a = new AlbumTestModel("Westlife", 1, covers[5]);
        albumsList.add(a);

        a = new AlbumTestModel("Black Eyed Peas", 11, covers[6]);
        albumsList.add(a);

        a = new AlbumTestModel("VivaLaVida", 14, covers[7]);
        albumsList.add(a);

        a = new AlbumTestModel("The Cardigans", 11, covers[8]);
        albumsList.add(a);

        a = new AlbumTestModel("Pussycat Dolls", 17, covers[9]);
        albumsList.add(a);

        /*Adding instants to the moment*/
        moment_a.setListOfInstants(albumsList);
        ArrayList<AlbumTestModel> albumsList2 = new ArrayList<>();
        albumsList2.add(albumsList.get(0));
        albumsList2.add(albumsList.get(1));
        moment_b.setListOfInstants(albumsList2);

        Log.d(TAG,"Australia size" + moment_a.getListOfInstants().size());
        Log.d(TAG,"Iindia size" + moment_b.getListOfInstants().size());
        /*Adding moments to the momentList*/
        moments.add(moment_a);
        moments.add(moment_b);

        //adapter.notifyDataSetChanged();
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {
        mCallback.onMomentSelected(position,moments);

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
}
