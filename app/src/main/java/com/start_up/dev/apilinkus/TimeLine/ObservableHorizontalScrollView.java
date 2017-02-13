package com.start_up.dev.apilinkus.TimeLine;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.start_up.dev.apilinkus.Tool.LockableHorizontalScrollView;
import com.start_up.dev.apilinkus.Tool.OnScrollListener;

/**
 * Created by Huong on 08/01/2017.
 */

public class ObservableHorizontalScrollView extends LockableHorizontalScrollView {


    private boolean mIsScrolling;
    private boolean mIsTouching;
    private Runnable mScrollingRunnable;
    private OnScrollListener mOnScrollListener;

    private final float COEFF_ZOOM=3.5f;
    public static final float MIN_DELTA=120.0f;
    public static Double MAX_DELTA;
    //public static Double MAX_DELTA=3.0*1099617.0;
    //public static final float MAX_DELTA=3.0f*1099617.0f;

    private Float scale = 5f;
    private float x_delta = -1;
    private ScaleGestureDetector mScaleDetector=new ScaleGestureDetector(getContext(),new ScaleListener());
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        @Override
        public boolean onScale(ScaleGestureDetector detector){

            Float scaleBkp=scale;
            //System.out.println(detector.getScaleFactor());
            //Float scale_bkp = new Float(scale);
        //    System.out.println("detector "+detector.getScaleFactor());
           // System.out.println("scale "+scale);
            float x=scale*detector.getScaleFactor();
            // On check que l'agrandissement soit pas trop petit
            if(scale*x>1) {
                scale = x;
                //scale=Math.max(0.5f,Math.min(scale,5f));

                x_delta = scale.floatValue()* COEFF_ZOOM + MIN_DELTA;
                //System.out.println(x_delta);
                if (x_delta > MIN_DELTA && x_delta< MAX_DELTA)
                    mOnScrollListener.onTouchScroll(x_delta,detector.getFocusX());
                else scale=scaleBkp;
            }
            return true;
        }
    }

    public ObservableHorizontalScrollView(Context context) {
        this(context, null, 0);
    }

    public ObservableHorizontalScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ObservableHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }



    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        int action = ev.getAction();
        if (mOnScrollListener != null) {
            mScaleDetector.onTouchEvent(ev);
        }

        if (action == MotionEvent.ACTION_DOWN) {
            if(( mOnScrollListener)!=null)
            ((Draw) mOnScrollListener).onTouchScrollView(ev);
        }

        if (action == MotionEvent.ACTION_MOVE) {
            mIsTouching = true;
            mIsScrolling = true;
        } else if (action == MotionEvent.ACTION_UP) {
            if (mIsTouching && !mIsScrolling) {
                if (mOnScrollListener != null) {
                    mOnScrollListener.onEndScroll(this);
                }
            }

            mIsTouching = false;
        }

        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldX, int oldY) {
        super.onScrollChanged(x, y, oldX, oldY);

        if (Math.abs(oldX - x) > 0) {
            if (mScrollingRunnable != null) {
                removeCallbacks(mScrollingRunnable);
            }

            mScrollingRunnable = new Runnable() {
                public void run() {
                    if (mIsScrolling && !mIsTouching) {
                        if (mOnScrollListener != null) {
                            mOnScrollListener.onEndScroll(ObservableHorizontalScrollView.this);
                        }
                    }

                    mIsScrolling = false;
                    mScrollingRunnable = null;
                }
            };

            postDelayed(mScrollingRunnable, 200);
        }

        if (mOnScrollListener != null) {
            mOnScrollListener.onScrollChanged(this, x, y, oldX, oldY);
        }
    }

    public OnScrollListener getOnScrollListener() {
        return mOnScrollListener;
    }

    public void setOnScrollListener(OnScrollListener mOnEndScrollListener) {
        this.mOnScrollListener = mOnEndScrollListener;

    }


}