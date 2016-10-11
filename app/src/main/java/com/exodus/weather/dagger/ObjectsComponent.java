package com.exodus.weather.dagger;


import com.exodus.weather.MainActivity;
import com.exodus.weather.adapters.CityListAdapter;
import com.exodus.weather.adapters.OtherWeatherAdapter;
import com.exodus.weather.fragments.CityFragment;
import com.exodus.weather.fragments.CitySearchDialog;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, ObjectModule.class})
public interface ObjectsComponent {
    void inject(MainActivity mainActivity);

    void inject(CityFragment cityFragment);

    void inject(OtherWeatherAdapter otherWeatherAdapter);

    void inject(CitySearchDialog citySearchDialog);

    void inject(CityListAdapter cityListAdapter);
}
