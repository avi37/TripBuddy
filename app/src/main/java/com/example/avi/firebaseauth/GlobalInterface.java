package com.example.avi.firebaseauth;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;

/**
 * Created by AVI on 3/13/2018.
 */

public interface GlobalInterface extends NavigationView.OnNavigationItemSelectedListener {

    void setSelectedFragment(Fragment fragment);

    void setMyToolbarLabel(String strName);
    //void setTabSelection(int tabSelection);

}

