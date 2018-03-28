package com.example.avi.firebaseauth;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MyFavouritesFragment extends Fragment {

    public static final String ARG_ITEM_ID = "favorite_list";

    ListView favoriteList;
    FavSharedPreference sharedPreference;
    List<Cards> favorites;

    Activity activity;
    CustomListAdapter productListAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_favourites, container, false);
        sharedPreference = new FavSharedPreference();
        favorites = sharedPreference.getFavorites(activity);

        if (favorites == null) {
            Snackbar.make(view, "No favourite packages", Snackbar.LENGTH_LONG).show();
        } else {
            if (favorites.size() == 0) {
                Snackbar.make(view, "No favourite packages", Snackbar.LENGTH_LONG).show();
            }

            favoriteList = view.findViewById(R.id.listview);
            if (favorites != null) {

                favoriteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                    }
                });

                favoriteList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(
                            AdapterView<?> parent, View view, int position, long id) {

                        ImageView button = view.findViewById(R.id.iv_addToFav);

                        String tag = button.getTag().toString();
                        if (tag.equalsIgnoreCase("grey")) {
                            sharedPreference.addFavorite(activity, favorites.get(position));
                            Toast.makeText(activity, "Added to Fav", Toast.LENGTH_SHORT).show();
                            button.setTag("red");
                            button.setImageResource(R.drawable.fav_filled);
                        } else {
                            sharedPreference.removeFavorite(activity, favorites.get(position));
                            button.setTag("grey");
                            button.setImageResource(R.drawable.fav_ept);
                            productListAdapter.remove(favorites.get(position));
                            Toast.makeText(activity, "Removed Favourites", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });
            }
        }
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("My Favourites");
    }


}
