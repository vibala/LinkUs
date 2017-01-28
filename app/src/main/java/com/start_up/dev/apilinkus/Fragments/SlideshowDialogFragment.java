package com.start_up.dev.apilinkus.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.start_up.dev.apilinkus.HomeActivity;
import com.start_up.dev.apilinkus.Model.Instant;
import com.start_up.dev.apilinkus.R;

import java.util.ArrayList;

/**
 * Created by Vignesh on 1/11/2017.
 */

public class SlideshowDialogFragment extends DialogFragment {
    private String TAG = SlideshowDialogFragment.class.getSimpleName();
    private TextView lblCount, lblTitle,lblDate;
    //  ViewPager is used for screen slides
    private ViewPager viewPager;
    private int selectedPosition = 0;
    private InstantViewPagerAdapter instantViewPagerAdapter;
    /*TODO Remplacer les albums par les photos d'un album donné (Cf. avec Vincent)*/
    private ArrayList<Instant> instants;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // layoutInflater converts yout XML file into corresponding view groups and widgets
        View view = inflater.inflate(R.layout.fragment_image_slider,container,false);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        lblCount = (TextView)view.findViewById(R.id.lbl_count);
        lblDate = (TextView) view.findViewById(R.id.lbl_date);
        lblTitle = (TextView) view.findViewById(R.id.lbl_title);

        instants = (ArrayList<Instant>) getArguments().getSerializable("instants");
        selectedPosition = getArguments().getInt("position");

        Log.e(TAG,"position: " +  selectedPosition);
        Log.e(TAG,"albums size: " + instants.size());

        instantViewPagerAdapter = new InstantViewPagerAdapter();
        viewPager.setAdapter(instantViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        setCurrentItem(selectedPosition);
        return view;
    }

    //  page change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            displayMetaInfo(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void setCurrentItem(int position){
        viewPager.setCurrentItem(position,false);
        displayMetaInfo(position);
    }

    private void displayMetaInfo(int position){
        Instant instant = instants.get(position);
        lblTitle.setText(instant.getName());
        lblCount.setText((position+1) + " of " + instants.size());
        lblDate.setText(instant.getPublishDate().toString());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    public class InstantViewPagerAdapter extends PagerAdapter{

        private LayoutInflater layoutInflater;

        public InstantViewPagerAdapter(){}

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.image_fullscreen_preview,container,false);
            ImageView imageViewPreview = (ImageView) view.findViewById(R.id.image_preview);
            Instant instant = instants.get(position);

            Glide
                    .with(getActivity())
                    .load(instant.getUrl()+"&userId="+ HomeActivity.userId)
                    .fitCenter()
                    .crossFade() // to make the change of images more smoothly and easier on the eyer
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageViewPreview);

            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return instants.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((View) object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
