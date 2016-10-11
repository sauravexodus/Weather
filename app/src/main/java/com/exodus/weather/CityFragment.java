package com.exodus.weather;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.exodus.weather.store.City;
import com.exodus.weather.store.DaoSession;
import com.exodus.weather.store.FutureWeather;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.kymjs.rxvolley.http.VolleyError;

import org.greenrobot.greendao.async.AsyncSession;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class CityFragment extends Fragment {

    private Unbinder unbinder;

    @BindView(R.id.other_weather_layout_recycler_view)
    RecyclerView recyclerView;

    @Inject
    DaoSession daoSession;
    @Inject
    AsyncSession asyncSession;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_city, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        OtherWeatherAdapter adapter = new OtherWeatherAdapter();
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        getWeather(524901);
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
        city.setHumidity(currentWeatherObject.getJSONObject("main").getInt("humidity"));
        city.setTemperature(currentWeatherObject.getJSONObject("main").getInt("temp"));
        city.setTemp_max(currentWeatherObject.getJSONObject("main").getInt("temp_max"));
        city.setTemp_min(currentWeatherObject.getJSONObject("main").getInt("temp_min"));
        city.setWeather_text(currentWeatherObject.getJSONArray("weather").getJSONObject(0).getString("main"));
        city.setUpdated_at(new Date(currentWeatherObject.getLong("dt")));
        city.setWind_speed(currentWeatherObject.getJSONObject("wind").getInt("speed"));

        daoSession.getCityDao().insertOrReplace(city);


        ArrayList<FutureWeather> newFutureWeather = new ArrayList<>();

        for (int i = 1; i < futureArray.length(); i++) {
            JSONObject weatherObject = futureArray.getJSONObject(i);
            FutureWeather futureWeather = new FutureWeather();
            futureWeather.setDate(new Date(weatherObject.getLong("dt")));
            futureWeather.setCity(city);
            futureWeather.setCity_id(city.getId());
            futureWeather.setTemp(weatherObject.getJSONObject("main").getInt("temp"));
            newFutureWeather.add(futureWeather);
        }

        daoSession.getFutureWeatherDao().insertOrReplaceInTx(newFutureWeather);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
