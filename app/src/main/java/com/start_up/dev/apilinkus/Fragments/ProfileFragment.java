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
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.start_up.dev.apilinkus.Adapter.AlbumsAdapter;
import com.start_up.dev.apilinkus.Model.Album;
import com.start_up.dev.apilinkus.R;
import com.start_up.dev.apilinkus.Listener.RecyclerViewClickListener;
import java.util.ArrayList;

/**
 * Created by Vignesh on 1/15/2017.
 */

public class ProfileFragment extends Fragment implements RecyclerViewClickListener {

    private View myView;
    protected static final String TAG = ProfileFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private AlbumsAdapter adapter;
    private ArrayList<Album> albums;
    private FrameLayout titleLayout;
    private CircularImageView circularImageView;
    private OnAlbumSelectedListener mCallback;

    // Container Activity must implement this interface
    public interface OnAlbumSelectedListener{
        public void onAlbumSelected(int position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.activity_userprofile,container,false);
        titleLayout = (FrameLayout) myView.findViewById(R.id.main_framelayout_title);
        circularImageView = (CircularImageView) myView.findViewById(R.id.circularImageView);
        recyclerView = (RecyclerView) myView.findViewById(R.id.recycler_view);
        return myView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /******************************************************************************************/
        /*DONT TOUCH THIS PART !!!! IT'S FOR DISPLAYING CORRECTLY THE CIRCULARVIEW IN ANY PLATFORM*/
        /******************************************************************************************/
        int heigth = titleLayout.getHeight();
        int heigth2 = (300 - (heigth*2)) - 100;
        circularImageView.getLayoutParams().height = heigth2;
        circularImageView.getLayoutParams().width = heigth2;
        /*Setting the margin top*/
        ViewGroup.MarginLayoutParams marginParams = new ViewGroup.MarginLayoutParams(circularImageView.getLayoutParams());
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width= ((dm.widthPixels)/2) - dpToPx(heigth2/2);
        //int width2 = (width - circularImageView.getWidth())/2;
        marginParams.setMargins(width+60, dpToPx(heigth+80), 0, 0);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(marginParams);
        circularImageView.setLayoutParams(layoutParams);
        circularImageView.requestLayout();

        /******************************************************************************************/
        /******************** RECYCLERVIEX DEFINITION ******************/
        /******************************************************************************************/
        Bundle bundle = this.getArguments();
        albums = (ArrayList<Album>) bundle.get("albums");
        adapter = new AlbumsAdapter(getContext(),albums,this);
        /////////////////////////////////////////////////////////////////////
        // A LayoutManager is responsible for measuring and positionning items within a RecyclerView as well as determining
        // the policy when to recycle items
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
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
        /*recyclerView.addOnScrollListener(new HideShowOnScrollListener() {
            @Override
            public void onHide() {
                //bottomNavigationView.setVisibility(INVISIBLE); // just want to hide the view
            }

            @Override
            public void onShow() {
                // bottomNavigationView.setVisibility(VISIBLE); // just want to hide the view
            }
        });*/


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnAlbumSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
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
        // Send the event to the host activity
        mCallback.onAlbumSelected(position);
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
