package com.example.avi.firebaseauth;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;

/**
 * Created by AVI on 3/8/2018.
 */

public class CustomListAdapter extends ArrayAdapter<Cards> {

    public View view;
    private Context mContext;
    private int mResource;

    FavSharedPreference sharedPreference = new FavSharedPreference();
    Activity activity;

    /**
     * Holds variables in a View
     */

    private static class ViewHolder {
        TextView title;
        ImageView image;
        TextView day_night;
        TextView price;
        CardView cardView;
        Button btn_book;
        ImageView iv_addfav, iv_sharePkg;
    }

    public CustomListAdapter(Context context, int resource, ArrayList<Cards> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override

    public View getView(final int position, View convertView, ViewGroup parent) {

        //sets up the image loader library
        setupImageLoader();

        //get the persons information
        final String title = getItem(position).getTitle();
        String imgUrl = getItem(position).getImgurl();
        String day_night = getItem(position).getDay_night();
        String price = getItem(position).getPrice();
        //create the view result for showing the animation

        //ViewHolder object
        final ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.title = convertView.findViewById(R.id.pkg_card_name);
            holder.image = convertView.findViewById(R.id.cardimageview);
            holder.cardView = convertView.findViewById(R.id.pkg_cardView);
            holder.day_night = convertView.findViewById(R.id.pkg_card_duration);
            holder.price = convertView.findViewById(R.id.pkg_card_price);
            holder.btn_book = convertView.findViewById(R.id.card_btn_book);
            holder.iv_addfav = convertView.findViewById(R.id.iv_addToFav);
            holder.iv_sharePkg = convertView.findViewById(R.id.iv_sharePkg);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(title);

        holder.btn_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "BOOK", Toast.LENGTH_SHORT).show();
            }
        });

        holder.iv_addfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (true) {
                    holder.iv_addfav.setImageResource(R.drawable.fav_filled);
                    Toast.makeText(getContext(), "Added to favourites", Toast.LENGTH_SHORT).show();
                } else {
                    holder.iv_addfav.setImageResource(R.drawable.fav_ept);
                    Toast.makeText(getContext(), "Removed from favourites", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.iv_sharePkg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Share", Toast.LENGTH_SHORT).show();
            }
        });
        /*holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("CardView Call");
                Toast.makeText(mContext, "clicked=" + getItem(position).getTitle(), Toast.LENGTH_SHORT).show();
                //Intent i = new Intent(mContext, Test.class);
                //mContext.startActivity(i);
            }
        });*/

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Imageview clicked", Toast.LENGTH_SHORT).show();
            }
        });

        holder.day_night.setText(day_night);
        holder.price.setText(price);
        //create the imageloader object
        ImageLoader imageLoader = ImageLoader.getInstance();

        int defaultImage = mContext.getResources().getIdentifier("@drawable/loading", null, mContext.getPackageName());

        //create display options
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(defaultImage)
                .showImageOnFail(defaultImage)
                .showImageOnLoading(defaultImage).build();

        //download and display image from url
        imageLoader.displayImage(imgUrl, holder.image, options);

        return convertView;
    }

    /**
     * Required for setting up the Universal Image loader Library
     */
    private void setupImageLoader() {
        // UNIVERSAL IMAGE LOADER SETUP
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                mContext)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        // END - UNIVERSAL IMAGE LOADER SETUP
    }
}
