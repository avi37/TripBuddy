package com.example.avi.firebaseauth;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MainNavigation extends SelectProfilePic
        implements NavigationView.OnNavigationItemSelectedListener {

    ImageView imageView_profile;
    TextView textView_userName;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);
        mAuth = FirebaseAuth.getInstance();
        imageView_profile = (ImageView) findViewById(R.id.nav_profileImage);
        textView_userName = (TextView) findViewById(R.id.nav_username);
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        loadUserInfo();
        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.nav_explore);
            android.support.v4.app.Fragment fragment = new android.support.v4.app.Fragment();
            fragment = new ExploreFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main_navigation, fragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        android.support.v4.app.Fragment fragment = new android.support.v4.app.Fragment();

        int id = item.getItemId();

        if (id == R.id.nav_explore) {
            fragment = new ExploreFragment();
        } else if (id == R.id.nav_favourites) {
            return true;
        } else if (id == R.id.nav_contactUs) {
            startActivity(new Intent(getApplicationContext(), ContactUs.class));
            return true;
        } else if (id == R.id.nav_feedback) {
            startActivity(new Intent(getApplicationContext(), FeedbackForm.class));
            return true;
        } else if (id == R.id.nav_rateUs) {
            goToPlaystore();
            return true;
        } else if (id == R.id.nav_share) {
            fragment = new Share();
        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(getApplication(), LogIn.class));
            return true;
        } else if (id == R.id.nav_quit) {
            //showQuitDialog();
            System.exit(0);
        }
        transaction.replace(R.id.content_main_navigation, fragment).addToBackStack("Explore");
        transaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showQuitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Are you sure?")
                .setTitle("Quit from the app");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                System.exit(0);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                return;
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void loadUserInfo() {
        try {
            File filepath = Environment.getExternalStorageDirectory();
            String path = filepath.getAbsolutePath()
                    + "/TripBuddy/Profile Image";
            FirebaseUser user = mAuth.getCurrentUser();
            File f = new File(path, user.getDisplayName().toString() + ".jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            imageView_profile.setImageBitmap(b);
            if (user.getDisplayName() != null) {
                textView_userName.setText(user.getDisplayName());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    void goToPlaystore() {
        Uri uri = Uri.parse("market://details");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        uri = Uri.parse("http://play.google.com/store/apps/details?");
        intent.setData(uri);
        startActivity(intent);
    }
}


