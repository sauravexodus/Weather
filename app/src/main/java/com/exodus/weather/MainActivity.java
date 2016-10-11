package com.exodus.weather;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements AsyncOperationListener {

    @BindView(R.id.dashboard_viewpager)
    ViewPager viewPager;

    ArrayList<Fragment> myFragments = new ArrayList<>();

    @Inject
    AsyncSession asyncSession;
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
            new ParseCityTask().execute();
        } else {
            InitializeViews();
        }

    }

    private void InitializeViews() {
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

    @Override
    public void onAsyncOperationCompleted(AsyncOperation operation) {
        InitializeViews();
        keystore.setCityListParsed(true);
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
            Log.d("City_LIST_PARSING", values[0] + "");
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
