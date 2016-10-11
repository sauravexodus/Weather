package com.exodus.weather;

import android.app.Application;
import android.content.Context;

import com.exodus.weather.dagger.DaggerObjectsComponent;
import com.exodus.weather.dagger.ObjectModule;
import com.exodus.weather.dagger.ObjectsComponent;

import net.danlew.android.joda.JodaTimeAndroid;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MyApplication extends Application {

    private ObjectsComponent objectsComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        JodaTimeAndroid.init(this);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/BebasNeueBook.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        objectsComponent = DaggerObjectsComponent.builder().objectModule(new ObjectModule(this)).build();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public ObjectsComponent getObjectsComponent() {
        return objectsComponent;
    }
}
