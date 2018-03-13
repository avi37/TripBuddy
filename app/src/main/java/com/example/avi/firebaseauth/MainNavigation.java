package com.example.avi.firebaseauth;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MainNavigation extends SelectProfilePic
        implements GlobalInterface {

    boolean doubleBackToExitPressedOnce = false;

    ImageView imageView_profile;
    TextView textView_userName, textView_myProfile;
    FirebaseAuth mAuth;
    SessionManager session;
    NavigationView navigationView;
    Fragment fragment = null, selectedFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);
        mAuth = FirebaseAuth.getInstance();
        session = new SessionManager(getApplicationContext());
        imageView_profile = (ImageView) findViewById(R.id.nav_profileImage);
        textView_userName = (TextView) findViewById(R.id.nav_tv_username);
        textView_myProfile = (TextView) findViewById(R.id.nav_tv_myprofile);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        // loadUserInfo();
        if (savedInstanceState == null) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            android.support.v4.app.Fragment fragment = new ExploreFragment();
            transaction.replace(R.id.content_main_navigation, fragment);
            transaction.commit();
        }

        /*textView_myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MyProfile.class));
            }
        });*/

    }

    @Override
    public void setSelectedFragment(Fragment fragment) {
        this.selectedFragment = fragment;
    }

    public void setMyToolbarLabel(String strName) {
        setTitle(strName);
    }

    public void changeFragment(Fragment fragment) {
        try {
            if (fragment != null) {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.content_main_navigation, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                //setSelectedFragment(fragment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            showQuitDialog();
        }

        if (selectedFragment != null) {
            if (selectedFragment instanceof ExploreFragment) {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    return;
                }
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_explore) {
            methodMenuExplore();
            //fragment = new ExploreFragment();
            return true;
        } else if (id == R.id.nav_favourites) {
            Toast.makeText(getApplicationContext(), "Yet to implement!", Toast.LENGTH_SHORT).show();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        } else if (id == R.id.nav_contactUs) {
            //fragment = new ContactUsFragment();
            methodMenuContactUs();
            return true;
        } else if (id == R.id.nav_feedback) {
            //fragment = new FeedbackFormFragment();
            methodMenuFeedback();
            return true;
        } else if (id == R.id.nav_rateUs) {
            methodMenuRateUs();
            return true;
        } else if (id == R.id.nav_share) {
            methodMenuShare();
            return true;
        } else if (id == R.id.nav_logout) {
            showLogoutDialog();
            return true;
        } else if (id == R.id.nav_quit) {
            showQuitDialog();
        }

        //transaction.replace(R.id.content_main_navigation, fragment).addToBackStack("Explore");
        //transaction.commit();
        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //drawer.closeDrawer(GravityCompat.START);

        //changeFragment(fragment);
        return true;
    }

    private void methodMenuFeedback() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        android.support.v4.app.Fragment fragment = new FeedbackFormFragment();
        transaction.replace(R.id.content_main_navigation, fragment);
        transaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return;
    }

    private void methodMenuContactUs() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        android.support.v4.app.Fragment fragment = new ContactUsFragment();
        transaction.replace(R.id.content_main_navigation, fragment);
        transaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return;
    }

    private void methodMenuExplore() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (!(navigationView.getMenu().findItem(R.id.nav_explore).isChecked())) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            android.support.v4.app.Fragment fragment = new ExploreFragment();
            transaction.replace(R.id.content_main_navigation, fragment);
            transaction.commit();
        } else {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        return;
    }

    private void methodMenuShare() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "https:nehal.tech/home.html");
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share App Link"));
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        session.logoutUser();
        finish();
    }

    private void showQuitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Want to exit from the app?")
                .setTitle("Exit from TripBuddy");

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

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Are you sure you want to logout?")
                .setTitle("Confirm Logout");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                logout();
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
            File f = new File(path, "1.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            imageView_profile.setImageBitmap(b);
            if (user.getDisplayName() != null) {
                textView_userName.setText(user.getDisplayName());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    void methodMenuRateUs() {
        Uri uri = Uri.parse("market://details");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        uri = Uri.parse("http://play.google.com/store/apps/details?");
        intent.setData(uri);
        startActivity(intent);
    }

    public void gotoMyProfile(View view) {
        startActivity(new Intent(getApplicationContext(), MyProfile.class));
    }
}


