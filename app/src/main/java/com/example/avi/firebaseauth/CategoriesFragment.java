package com.example.avi.firebaseauth;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Created by AVI on 3/8/2018.
 */

public class CategoriesFragment extends Fragment implements View.OnClickListener {

    View view;
    Button btn_cat_beach, btn_cat_culture, btn_cat_treakking, btn_cat_heritage, btn_cat_pilgrimage, btn_cat_hill_stations;

    FirebaseFirestore mfirestore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.explore_package_categories, container, false);

        btn_cat_beach = (Button) view.findViewById(R.id.cat_btn_beach);
        btn_cat_culture = (Button) view.findViewById(R.id.cat_btn_cultural);
        btn_cat_treakking = (Button) view.findViewById(R.id.cat_btn_trekking);
        btn_cat_heritage = (Button) view.findViewById(R.id.cat_btn_heritage);
        btn_cat_pilgrimage = (Button) view.findViewById(R.id.cat_btn_pilgrimage);
        btn_cat_hill_stations = (Button) view.findViewById(R.id.cat_btn_hill_stations);

        btn_cat_beach.setOnClickListener(this);
        btn_cat_culture.setOnClickListener(this);
        btn_cat_treakking.setOnClickListener(this);
        btn_cat_heritage.setOnClickListener(this);
        btn_cat_pilgrimage.setOnClickListener(this);
        btn_cat_hill_stations.setOnClickListener(this);

        mfirestore = FirebaseFirestore.getInstance();

        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cat_btn_beach:
                //Toast.makeText(getContext(), "Beach", Toast.LENGTH_SHORT).show();
                show("00");
                return;

            case R.id.cat_btn_cultural:
                //Toast.makeText(getContext(), "Cultural", Toast.LENGTH_SHORT).show();
                show("01");
                return;

            case R.id.cat_btn_trekking:
                //Toast.makeText(getContext(), "Trekking", Toast.LENGTH_SHORT).show();
                show("02");
                return;

            case R.id.cat_btn_heritage:
                //Toast.makeText(getContext(), "Heritage", Toast.LENGTH_SHORT).show();
                show("03");
                return;

            case R.id.cat_btn_pilgrimage:
                //Toast.makeText(getContext(), "Pilgrimage", Toast.LENGTH_SHORT).show();
                show("04");
                return;

            case R.id.cat_btn_hill_stations:
                //Toast.makeText(getContext(), "Hill Stations", Toast.LENGTH_SHORT).show();
                show("05");
                return;
        }
    }

    private void show(String item) {

        String[] name, price, duration;
        final int count = 0;
        mfirestore.collection("package_info").document(item).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    try {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        for (int i = 0; i < 3; i++) {
                            String field = documentSnapshot.getString("name");
                            Toast.makeText(getContext(),field, Toast.LENGTH_SHORT).show();
                        }
                    } catch (NullPointerException e) {
                        System.out.println("Null Pointer Exxxxx");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        });
    }
}
