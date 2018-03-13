package com.example.avi.firebaseauth;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ExploreFragment extends Fragment {

    ViewPager viewPager;
    TabLayout tabLayout;
    PopularFragment popularFragment;
    Category category;

    GlobalInterface mCallBackInt;

    public ExploreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(2);

        mCallBackInt = (GlobalInterface) getActivity();
        mCallBackInt.setSelectedFragment(ExploreFragment.this);
        mCallBackInt.setMyToolbarLabel("Explore");
        //Initializing the tablayout
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                viewPager.setCurrentItem(position, false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        setupViewPager(viewPager);
        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        popularFragment = new PopularFragment();
        category = new Category();

        adapter.addFragment(popularFragment, "Popular");
        adapter.addFragment(category, "Categories");

        viewPager.setAdapter(adapter);
    }

}
