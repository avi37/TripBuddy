package com.example.avi.firebaseauth;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class PopularFragment extends Fragment {

    CustomListAdapter adapter;
    private ListView mlistview;
    CardView crdlist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.listview_layout, container, false);
        mlistview = (ListView) view.findViewById(R.id.listview);
        crdlist = (CardView) view.findViewById(R.id.pkg_cardView);

        ArrayList<Cards> list = new ArrayList<Cards>();

        list.add(new Cards("drawable://" + R.drawable.udaipur, "History of Rajasthan", "5D/3N", "10000/-"));
        list.add(new Cards("drawable://" + R.drawable.south_india, "Best of South India", "3D/2N", "12000/-"));
        list.add(new Cards("drawable://" + R.drawable.north, "Journey to North", "5D/4N", "15000/-"));
        list.add(new Cards("drawable://" + R.drawable.goa, "Enjoy in Goa", "3D/2N", "9000/-"));
        list.add(new Cards("drawable://" + R.drawable.amritsar, "Golden Triangle Tour", "2D/1N", "7000/-"));
        list.add(new Cards("drawable://" + R.drawable.kashmir, "Heaven on Earth", "6D/5N", "15000/-"));
        list.add(new Cards("drawable://" + R.drawable.gujarat, "Culture of Gujarat", "4D/3N", "8000/-"));
        list.add(new Cards("drawable://" + R.drawable.chardham, "Holiest Place Chardham", "5D/3N", "13000/-"));
        list.add(new Cards("drawable://" + R.drawable.taj, "Wonder of India", "2D/1N", "9000/-"));
        list.add(new Cards("drawable://" + R.drawable.lonavala, "Vacation in Lonavala", "3D/2N", "8000/-"));


        adapter = new CustomListAdapter(getActivity(), R.layout.explore_popular_packages, list);
        // CustomListAdapter adapter=new CustomListAdapter(this,R.layout.explore_package_categories,list);
        mlistview.setAdapter(adapter);

        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(), "CardView Call", Toast.LENGTH_LONG);

            }


        });
        return view;
    }

}
