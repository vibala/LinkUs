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

import com.start_up.dev.apilinkus.Adapter.HomeAdapter;
import com.start_up.dev.apilinkus.Listener.RecyclerViewClickListener;
import com.start_up.dev.apilinkus.Model.Instant;
import com.start_up.dev.apilinkus.R;

import java.util.ArrayList;

/**
 * Created by Vignesh on 1/15/2017.
 */

public class HomeFragment extends Fragment implements RecyclerViewClickListener {

    private View homeView;
    private RecyclerView recyclerView;
    private ArrayList<Instant> instants;
    private HomeAdapter adapter;
    OnPostSelectedListener mCallback;

    public interface OnPostSelectedListener{
        public void onPostSelected(int position,View view);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeView = inflater.inflate(R.layout.content_home_fragment,container,false);
        recyclerView = (RecyclerView) homeView.findViewById(R.id.recycler_view);
        return homeView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(),1);
        instants = (ArrayList<Instant>) getArguments().get("recent_instants");
        adapter = new HomeAdapter(getContext(),instants,this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10)));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnPostSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {
        mCallback.onPostSelected(position,v);
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
