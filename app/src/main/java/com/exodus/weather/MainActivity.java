package com.exodus.weather;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.exodus.weather.fragments.CityFragment;
import com.exodus.weather.fragments.CitySearchDialog;
import com.exodus.weather.interfaces.OnCitySelectedListener;
import com.exodus.weather.store.City;
import com.exodus.weather.store.DaoSession;
import com.exodus.weather.store.KeyStore;
import com.exodus.weather.store.ListCity;

import org.greenrobot.greendao.async.AsyncOperation;
import org.greenrobot.greendao.async.AsyncOperationListener;
import org.greenrobot.greendao.async.AsyncSession;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements AsyncOperationListener, OnCitySelectedListener {

    @BindView(R.id.dashboard_viewpager)
    ViewPager viewPager;
    @BindView(R.id.city_list_parse_progress_bar)
    ProgressBar progressBar;

    ArrayList<Fragment> myFragments = new ArrayList<>();

    @Inject
    AsyncSession asyncSession;
    @Inject
    DaoSession daoSession;
    @Inject
    KeyStore keystore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        ((MyApplication) getApplication()).getObjectsComponent().inject(this);

        asyncSession.setListenerMainThread(this);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/BebasNeueBook.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        if (!keystore.isCityListParsed()) {
            progressBar.setVisibility(View.VISIBLE);
            new ParseCityTask().execute();
        }

        initializePreviousCities();
        InitializeViews();

    }

    void initializePreviousCities() {
        myFragments.add(new CityFragment());
        List<City> cities = daoSession.getCityDao().loadAll();
        for (City city : cities) {
            CityFragment cityFragment = new CityFragment();
            cityFragment.cityId = city.getId();
            myFragments.add(cityFragment);
        }
    }

    private void InitializeViews() {
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(myFragments);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(10);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onAsyncOperationCompleted(AsyncOperation operation) {
        keystore.setCityListParsed(true);
        progressBar.setVisibility(View.GONE);
    }

    @OnClick(R.id.add_city_fab)
    void addCity() {
        if (keystore.isCityListParsed()) {
            CitySearchDialog citySearchDialog = new CitySearchDialog();
            citySearchDialog.setOnCitySelectedListener(this);
            citySearchDialog.show(getSupportFragmentManager(), "CHOOSE_CITY_DIALOG");
        }
    }

    @Override
    public void onCitySelected(long cityId) {
        CityFragment cityFragment = new CityFragment();
        cityFragment.cityId = cityId;
        myFragments.add(cityFragment);
        InitializeViews();
        viewPager.setCurrentItem(myFragments.size() - 1);
    }

    public class ParseCityTask extends AsyncTask<Void, Integer, Void> {
        ArrayList<ListCity> cityList = new ArrayList<>();

        @Override
        protected Void doInBackground(Void... params) {
            String json;
            try {
                InputStream is = getAssets().open("city.list.json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, "UTF-8");

                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    publishProgress(i / jsonArray.length() * 100);
                    JSONObject cityObject = jsonArray.getJSONObject(i);
                    ListCity listCity = new ListCity(cityObject.getLong("_id"));
                    listCity.setCountry(cityObject.getString("country"));
                    listCity.setName(cityObject.getString("name"));

                    cityList.add(listCity);
                }


            } catch (IOException | JSONException ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            asyncSession.insertOrReplaceInTx(ListCity.class, cityList);
        }
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
