package com.nobias.businesslogic.di;

import android.content.Context;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.nobias.MyApplication;
import com.nobias.businesslogic.network.RetroFitCallFactory;
import com.nobias.businesslogic.network.RetroFitInterface;
import com.nobias.businesslogic.preferences.AppSharedPreferences;
import com.nobias.businesslogic.rx.AppSchedulerProvider;
import com.nobias.businesslogic.rx.SchedulerProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Avantika Gadhiya on Apr, 16 2020 5:30.
 */
@Module
public class AppModule {
    private final MyApplication mApplication;

    public AppModule(MyApplication application) {
        mApplication = application;
    }

    @Provides
    Context providesApplicationContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    MyApplication providesMyApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    AppSharedPreferences providesSharedPreferences() {
        return new AppSharedPreferences(mApplication);
    }

    @Provides
    @Singleton
    RetroFitInterface providesRetrofitInterface() {
        return RetroFitCallFactory.create();
    }

    @Provides
    SchedulerProvider provideSchedulerProvider() {
        return new AppSchedulerProvider();
    }

    @Provides
    @Singleton
    LocalBroadcastManager providesLocalBroadcastInstance() {
        return LocalBroadcastManager.getInstance(mApplication);
    }
}
