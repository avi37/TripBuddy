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
import android.support.v7.app.AppCompatActivity;
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
import java.util.Stack;

public class MainNavigation extends AppCompatActivity
        implements GlobalInterface, View.OnClickListener {

    boolean doubleBackToExitPressedOnce = false;

    ImageView imageView_profile;
    TextView textView_userName, textView_myProfile;
    FirebaseAuth mAuth;
    SessionManager session;
    NavigationView navigationView;
    DrawerLayout drawer;
    Fragment fragment = null, selectedFragment = null;

    private Stack<Fragment> fragmentStack;
    private FragmentManager fragmentManager;
    private FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);
        mAuth = FirebaseAuth.getInstance();
        session = new SessionManager(getApplicationContext());
        imageView_profile = (ImageView) findViewById(R.id.nav_profileImage);
        textView_userName = (TextView) findViewById(R.id.nav_tv_username);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        fragmentStack = new Stack<>();

        fragmentManager = getSupportFragmentManager();
        fragment = new ExploreFragment();
        ft = fragmentManager.beginTransaction();
        ft.add(R.id.content_main_navigation, fragment);
        fragmentStack.push(fragment);
        ft.commit();

        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main_navigation);
        textView_myProfile = (TextView) headerView.findViewById(R.id.nav_tv_myprofile);
        textView_myProfile.setOnClickListener(this);
         loadUserInfo();

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.content_main_navigation);
                if (f != null) {
                    updateTitleAndDrawer(f);
                }
            }
        });

    }

    private void updateTitleAndDrawer(Fragment f) {
        String fragClassName = f.getClass().getName();

        if (fragClassName.equals(ExploreFragment.class.getName())) {
            setTitle("Explore");
            navigationView.getMenu().getItem(0).setChecked(true);
        } else if (fragClassName.equals(MyFavouritesFragment.class.getName())) {
            setTitle("My Favourites");
            navigationView.getMenu().getItem(1).setChecked(true);
        } else if (fragClassName.equals(ContactUsFragment.class.getName())) {
            setTitle("Contact Us");
            navigationView.getMenu().getItem(2).setChecked(true);
        } else if (fragClassName.equals(FeedbackFormFragment.class.getName())) {
            setTitle("Feedback");
            navigationView.getMenu().getItem(3).setChecked(true);
        }
    }

    @Override
    public void setSelectedFragment(Fragment fragment) {
        this.selectedFragment = fragment;
    }

    public void setMyToolbarLabel(String strName) {
        setTitle(strName);
    }

    public void changeFragment(Fragment _fragment) {
        try {
            if (fragment != null) {
                ft = fragmentManager.beginTransaction();
                ft.add(R.id.content_main_navigation, _fragment).addToBackStack(null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentStack.lastElement().onPause();
                ft.hide(fragmentStack.lastElement());
                fragmentStack.push(_fragment);
                ft.commit();
                drawer.closeDrawer(GravityCompat.START);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (fragmentStack.size() > 1) {
            ft = fragmentManager.beginTransaction();
            fragmentStack.lastElement().onPause();
            ft.remove(fragmentStack.pop());
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            fragmentStack.lastElement().onResume();
            ft.show(fragmentStack.lastElement());
            ft.commit();
            updateTitleAndDrawer(fragmentStack.lastElement());
        } else {
            if (fragmentStack.size() == 1) {
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
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_explore) {
            changeFragment(new ExploreFragment());
            return true;
        } else if (id == R.id.nav_favourites) {
            changeFragment(new MyFavouritesFragment());
            return true;
        } else if (id == R.id.nav_contactUs) {
            changeFragment(new ContactUsFragment());
            return true;
        } else if (id == R.id.nav_feedback) {
            changeFragment(new FeedbackFormFragment());
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
            return true;
        }
        return true;
    }


    private void methodMenuExplore() {
        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (!(selectedFragment instanceof ExploreFragment)) {
            ft = fragmentManager.beginTransaction();
            Fragment exFrag = new ExploreFragment();
            ft.add(R.id.content_main_navigation, exFrag);
            fragmentStack.lastElement().onPause();
            ft.hide(fragmentStack.lastElement());
            fragmentStack.push(exFrag);
            ft.commit();
        }
        drawer.closeDrawer(GravityCompat.START);
        return;
    }

    private void methodMenuShare() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "https:nehal.tech/index.html");
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share App Link"));
    }

    void methodMenuRateUs() {
        Uri uri = Uri.parse("market://details");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        uri = Uri.parse("http://play.google.com/store/apps/details?");
        intent.setData(uri);
        startActivity(intent);
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
                navigationView.getMenu().getItem(0).setChecked(true);
                return;
            }
        });

        AlertDialog dialog = builder.create();
        drawer.closeDrawer(GravityCompat.START);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
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
        dialog.setCanceledOnTouchOutside(false);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nav_tv_myprofile:
                startActivity(new Intent(getApplicationContext(), MyProfile.class));
        }

    }
}


