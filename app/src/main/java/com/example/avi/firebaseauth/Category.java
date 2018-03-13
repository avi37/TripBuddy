package com.example.avi.firebaseauth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by AVI on 3/8/2018.
 */

public class Category extends Fragment implements View.OnClickListener {

    View view;
    Button btn_cat_beach, btn_cat_culture, btn_cat_treakking, btn_cat_heritage, btn_cat_pilgrimage, btn_cat_hillstation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.explore_package_categories, container, false);

        return view;
    }


    @Override
    public void onClick(View v) {
        return;
    }
}
