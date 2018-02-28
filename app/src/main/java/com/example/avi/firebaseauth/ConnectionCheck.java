package com.example.avi.firebaseauth;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by AVI on 1/29/2018.
 */

public class ConnectionCheck {
    ConnectivityManager connectivityManager;
    NetworkInfo info;

    public boolean checkConnection(Context context) {
        boolean flag = true;
        try {
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            info = connectivityManager.getActiveNetworkInfo();

            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                System.out.println(info.getTypeName());
                flag = false;
            }
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                System.out.println(info.getTypeName());
                flag = false;
            }
        } catch (Exception exception) {
            System.out.println("Exception at network connection....."
                    + exception);
        }
        return flag;
    }
}


