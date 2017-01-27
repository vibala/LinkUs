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
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.start_up.dev.apilinkus.Adapter.MomentsAdapater;
import com.start_up.dev.apilinkus.Listener.RecyclerViewClickListener;
import com.start_up.dev.apilinkus.Model.Album;
import com.start_up.dev.apilinkus.Model.Moment;
import com.start_up.dev.apilinkus.R;

import java.util.ArrayList;

/**
 * Created by Vignesh on 1/16/2017.
 */

public class AlbumFragment extends Fragment implements RecyclerViewClickListener{

    private RecyclerView recyclerView;
    private MomentsAdapater adapter;
    // A remplacer par les moments
    private ArrayList<Moment> moments;
    private String TAG = AlbumFragment.class.getSimpleName();
    private View momentView;
    OnMomentSelectedListener mCallback;
    private Album selectedAlbum;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        momentView = inflater.inflate(R.layout.content_moment_recyclerview_layout,container,false);
        return momentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button uploadButton = (Button) momentView.findViewById(R.id.upload_button);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.momentFragmentOnClickButtonUpload(selectedAlbum.getId());
            }
        });
        recyclerView = (RecyclerView) momentView.findViewById(R.id.moment_recyclerView);
        // 2nd Arguments refer to the number of columns in the grid
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),1);
        recyclerView.setLayoutManager(layoutManager);
        // Item decorations can affect both measurement and drawing of individual item views
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(dpToPx(10),1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if(savedInstanceState != null){
            selectedAlbum = (Album) savedInstanceState.getSerializable("selected_album");
        }else{
            selectedAlbum = (Album) getArguments().get("selected_album");
        }
        moments = selectedAlbum.getMoments() ;
        adapter = new MomentsAdapater(getContext(),moments,this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("selected_album",selectedAlbum);
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

    @Override
    public void recyclerViewListClicked(View v, int position) {
        mCallback.onMomentSelected(position);

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


    public interface OnMomentSelectedListener{
        public void onMomentSelected(int position);
        public void momentFragmentOnClickButtonUpload(String albumId);
    }


}
