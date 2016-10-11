package com.exodus.weather.dagger;


import com.exodus.weather.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, ObjectModule.class})
public interface ObjectsComponent {
    void inject(MainActivity mainActivity);
}
