package com.start_up.dev.apilinkus.Listener;

import android.support.v7.widget.RecyclerView;
import static android.support.v7.widget.RecyclerView.*;

/**
 * Created by Vignesh on 1/14/2017.
 */

public abstract class HideShowOnScrollListener extends RecyclerView.OnScrollListener{

    private static final int HIDE_THRESHOLD = 20;
    private int scrolledDistance = 0;
    private boolean controlsVisible = true; // state of the bottom navigation bar
    /**
     * Callback method to be invoked when the RecyclerView has been scrolled. This will be
     * called after the scroll has completed.
     * <p>
     * This callback will also be called if visible item range changes after a layout
     * calculation. In that case, dx and dy will be 0.
     *
     * @param recyclerView The RecyclerView which scrolled.
     * @param dx The amount of horizontal scroll.
     * @param dy The amount of vertical scroll.
     */
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if(scrolledDistance > HIDE_THRESHOLD && controlsVisible){
            onHide();
            controlsVisible = false;
            scrolledDistance = 0;
        }else if(scrolledDistance < (-HIDE_THRESHOLD) && !controlsVisible){
            onShow();
            controlsVisible = true;
            scrolledDistance = 0;
        }

        if((controlsVisible && dy > 0) || (!controlsVisible && dy < 0)){
            scrolledDistance += dy;
        }
    }

    public abstract void onHide();
    public abstract void onShow();
}
