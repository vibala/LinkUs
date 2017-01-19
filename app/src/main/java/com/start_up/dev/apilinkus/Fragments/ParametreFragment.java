package com.start_up.dev.apilinkus.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.start_up.dev.apilinkus.R;

/**
 * Created by Vignesh on 1/17/2017.
 */

public class ParametreFragment extends Fragment {

    private View parametreView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parametreView = inflater.inflate(R.layout.settings_fragment_layout,container,false);
        return parametreView;
    }
}
