package com.exodus.weather.adapters;


import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.exodus.weather.MyApplication;
import com.exodus.weather.R;
import com.exodus.weather.store.DaoSession;
import com.exodus.weather.store.ListCity;
import com.exodus.weather.store.ListCityDao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.ViewHolder> {

    @Inject
    DaoSession daoSession;

    List<ListCity> cities = new ArrayList<>();

    public CityListAdapter(Activity appCompatActivity) {
        ((MyApplication) appCompatActivity.getApplication()).getObjectsComponent().inject(this);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.city_list_item, parent, false));
    }

    public void reQuery(String s) {
        int previousSize = cities.size();
        cities.clear();
        notifyItemRangeRemoved(0, previousSize - 1);
        cities = daoSession.getListCityDao().queryBuilder().where(ListCityDao
                .Properties.Name.like(s.toLowerCase())).limit(30).list();
        notifyItemRangeInserted(0, cities.size() - 1);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ListCity listCity = cities.get(holder.getAdapterPosition());
        holder.cityName.setText(listCity.getName());
    }

    @Override
    public int getItemCount() {
        return cities.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.city_list_item_city_name)
        TextView cityName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
