package com.start_up.dev.apilinkus.Tool;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Huong on 17/01/2017.
 */

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration{

    private int spacing;
    private int spanCount;

    public GridSpacingItemDecoration(int spacing, int spanCount){
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