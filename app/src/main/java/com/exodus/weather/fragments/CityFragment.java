package com.exodus.weather.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.exodus.weather.MyApplication;
import com.exodus.weather.R;
import com.exodus.weather.Utils;
import com.exodus.weather.adapters.OtherWeatherAdapter;
import com.exodus.weather.store.City;
import com.exodus.weather.store.DaoSession;
import com.exodus.weather.store.FutureWeather;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.kymjs.rxvolley.http.VolleyError;

import org.greenrobot.greendao.async.AsyncOperation;
import org.greenrobot.greendao.async.AsyncOperationListener;
import org.greenrobot.greendao.async.AsyncSession;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class CityFragment extends Fragment implements AsyncOperationListener {

    private Unbinder unbinder;

    @BindView(R.id.other_weather_layout_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.weather_city_name)
    TextView cityName;

    @BindView(R.id.weather_current_temp)
    TextView temp;

    @BindView(R.id.weather_max_temp)
    TextView temp_max;

    @BindView(R.id.weather_min_temp)
    TextView temp_min;

    @BindView(R.id.weather_date)
    TextView updatedAt;

    @BindView(R.id.weather_day)
    TextView currentDay;

    @BindView(R.id.weather_humidity)
    TextView humidity;

    @BindView(R.id.weather_wind_speed)
    TextView windSpeed;

    @BindView(R.id.weather_image)
    ImageView weatherIcon;

    @BindView(R.id.weather_name_type)
    TextView weatherType;

    @BindView(R.id.weather_loading_progress_bar)
    ProgressBar progressBar;

    @Inject
    DaoSession daoSession;
    @Inject
    AsyncSession asyncSession;

    City city;
    private long cityId = 524901L;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_city, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
        ((MyApplication) getActivity().getApplication()).getObjectsComponent().inject(this);

        asyncSession.setListenerMainThread(this);
        city = daoSession.getCityDao().load(cityId);

        if (city == null)
            getWeather(cityId);
        else
            populateViews();
    }

    public void populateViews() {
        cityName.setText(city.getName());
        temp.setText(city.getTemperature() + "°");
        temp_max.setText(city.getTemp_max() + "°");
        temp_min.setText(city.getTemp_min() + "°");
        updatedAt.setText(new DateTime(city.getUpdated_at()).toString("dd/MM/yyyy"));
        currentDay.setText(new DateTime(city.getUpdated_at()).toString("EEEEE"));
        humidity.setText(city.getHumidity() + "%");
        windSpeed.setText("Wind " + city.getWind_speed() + "km/hr");
        weatherType.setText(city.getWeather_text());

        initializeRecyclerView();

        progressBar.setVisibility(View.GONE);
    }

    public void initializeRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        OtherWeatherAdapter adapter = new OtherWeatherAdapter(getActivity(), cityId);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void getWeather(long cityId) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("id", (int) cityId);
        httpParams.put("units", "metric");
        httpParams.put("appid", Utils.OPEN_WEATHER_MAP_API_KEY);

        HttpCallback httpCallback = new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                try {
                    parseWeather(t);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(VolleyError error) {
                error.printStackTrace();
            }
        };

        new RxVolley.Builder().url(Utils.OPEN_WEATHER_URL)
                .callback(httpCallback)
                .contentType(RxVolley.ContentType.JSON)
                .params(httpParams)
                .doTask();

    }

    private void parseWeather(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        City city = new City(jsonObject.getJSONObject("city").getLong("id"));
        city.setName(jsonObject.getJSONObject("city").getString("name"));

        JSONArray futureArray = jsonObject.getJSONArray("list");

        JSONObject currentWeatherObject = futureArray.getJSONObject(0);
        city.setHumidity(currentWeatherObject.getInt("humidity"));
        city.setTemperature(currentWeatherObject.getJSONObject("temp").getInt("day"));
        city.setTemp_max(currentWeatherObject.getJSONObject("temp").getInt("max"));
        city.setTemp_min(currentWeatherObject.getJSONObject("temp").getInt("min"));
        city.setWeather_text(currentWeatherObject.getJSONArray("weather").getJSONObject(0).getString("main"));
        city.setUpdated_at(new Date(currentWeatherObject.getLong("dt") * 1000));
        city.setWind_speed(currentWeatherObject.getInt("speed"));

        daoSession.getCityDao().insertOrReplace(city);


        ArrayList<FutureWeather> newFutureWeather = new ArrayList<>();

        for (int i = 1; i < futureArray.length(); i++) {
            JSONObject weatherObject = futureArray.getJSONObject(i);
            FutureWeather futureWeather = new FutureWeather();
            futureWeather.setDate(new Date(weatherObject.getLong("dt") * 1000));
            futureWeather.setCity(city);
            futureWeather.setCity_id(city.getId());
            futureWeather.setTemp(weatherObject.getJSONObject("temp").getInt("day"));
            newFutureWeather.add(futureWeather);
        }

        asyncSession.insertOrReplaceInTx(FutureWeather.class, newFutureWeather);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onAsyncOperationCompleted(AsyncOperation operation) {
        city = daoSession.getCityDao().load(cityId);
        populateViews();
    }
}
