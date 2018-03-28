package com.example.avi.firebaseauth;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by AVI on 3/28/2018.
 */

public class FavSharedPreference {

    public static final String PREFS_NAME = "PRODUCT_APP";
    public static final String FAVORITES = "Product_Favorite";

    public FavSharedPreference() {
        super();
    }

    // This four methods are used for maintaining favorites.
    public void saveFavorites(Context context, List<Cards> favorites) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(FAVORITES, jsonFavorites);

        editor.commit();
    }

    public void addFavorite(Context context, Cards cards) {
        List<Cards> favorites = getFavorites(context);
        if (favorites == null)
            favorites = new ArrayList<Cards>();
        favorites.add(cards);
        saveFavorites(context, favorites);
    }

    public void removeFavorite(Context context, Cards cards) {
        ArrayList<Cards> favorites = getFavorites(context);
        if (favorites != null) {
            favorites.remove(cards);
            saveFavorites(context, favorites);
        }
    }

    public ArrayList<Cards> getFavorites(Context context) {
        SharedPreferences settings;
        List<Cards> favorites;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            Cards[] favoriteItems = gson.fromJson(jsonFavorites,
                    Cards[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<Cards>(favorites);
        } else
            return null;

        return (ArrayList<Cards>) favorites;
    }


}
