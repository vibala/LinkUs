package com.start_up.dev.apilinkus.Tool;

import com.start_up.dev.apilinkus.TimeLine.ObservableHorizontalScrollView;

/**
 * Created by Huong on 08/01/2017.
 */

public interface OnScrollListener {
    public void onScrollChanged(ObservableHorizontalScrollView scrollView, int x, int y, int oldX, int oldY);
    public void onEndScroll(ObservableHorizontalScrollView scrollView);
    public void onTouchScroll(double x_delta, double focusX);
}