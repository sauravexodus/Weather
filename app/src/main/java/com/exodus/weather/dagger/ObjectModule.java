package com.exodus.weather.dagger;


import android.app.Application;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import com.exodus.weather.store.DaoMaster;
import com.exodus.weather.store.DaoSession;

import org.greenrobot.greendao.async.AsyncSession;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ObjectModule {

    DaoSession daoSession;
    SQLiteDatabase db;
    SharedPreferences keyStore;

    public ObjectModule(Application application) {
        DaoMaster.DevOpenHelper dbHelper = new DaoMaster.DevOpenHelper(application
                , "dogether-db", null);
        db = dbHelper.getWritableDatabase();
        keyStore = application.getSharedPreferences("KEY_STORE", 0);
        daoSession = new DaoMaster(db).newSession();
    }

    @Provides
    @Singleton
    DaoSession provideDaoSession() {
        return daoSession;
    }

    @Provides
    @Singleton
    SQLiteDatabase provideDbInstance() {
        return db;
    }

    @Provides
    @Singleton
    SharedPreferences provideKeyStore() {
        return keyStore;
    }


    @Provides
    AsyncSession provideAsyncSession() {
        return daoSession.startAsyncSession();
    }
}
