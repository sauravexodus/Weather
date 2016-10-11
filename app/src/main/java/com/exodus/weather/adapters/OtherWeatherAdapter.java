package com.exodus.weather.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.exodus.weather.MyApplication;
import com.exodus.weather.R;
import com.exodus.weather.Utils;
import com.exodus.weather.store.DaoSession;
import com.exodus.weather.store.FutureWeather;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class OtherWeatherAdapter extends RecyclerView.Adapter<OtherWeatherAdapter.ViewHolder> {

    @Inject
    DaoSession daoSession;

    long cityId;
    List<FutureWeather> futureWeathers = new ArrayList<>();

    public OtherWeatherAdapter(Context context, long cityId) {
        ((MyApplication) ((AppCompatActivity) context).getApplication()).getObjectsComponent().inject(this);
        this.cityId = cityId;
        futureWeathers = daoSession.getFutureWeatherDao()._queryCity_FutureWeather(cityId);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.day_weather_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FutureWeather futureWeather = futureWeathers.get(holder.getAdapterPosition());
        holder.day.setText(new DateTime(futureWeather.getDate()).toString("EEE"));
        holder.temperature.setText(futureWeather.getTemp() + "°");
        holder.weatherImage.setImageResource(Utils.getCityImageDrawable(futureWeather.getWeather_icon()));
    }

    @Override
    public int getItemCount() {
        return futureWeathers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @BindView(R.id.other_items_weather_day)
        TextView day;

        @Nullable
        @BindView(R.id.other_items_weather_image)
        ImageView weatherImage;

        @Nullable
        @BindView(R.id.other_items_weather_temp)
        TextView temperature;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
