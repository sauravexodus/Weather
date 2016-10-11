package com.exodus.weather;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.dashboard_viewpager)
    ViewPager viewPager;

    ArrayList<Fragment> myFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/BebasNeueBook.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        InitializeViews();
    }

    private void InitializeViews() {
        myFragments.add(new CityFragment());
        myFragments.add(new CityFragment());
        myFragments.add(new CityFragment());
        myFragments.add(new CityFragment());
        myFragments.add(new CityFragment());
        myFragments.add(new CityFragment());
        myFragments.add(new CityFragment());
        myFragments.add(new CityFragment());
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(myFragments);

        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        ArrayList<Fragment> myFragments = new ArrayList<>();

        public MyPagerAdapter(ArrayList<Fragment> fragments) {
            super(getSupportFragmentManager());
            this.myFragments.addAll(fragments);
        }

        @Override
        public Fragment getItem(int position) {
            return myFragments.get(position);
        }

        @Override
        public int getCount() {
            return myFragments.size();
        }
    }
}
