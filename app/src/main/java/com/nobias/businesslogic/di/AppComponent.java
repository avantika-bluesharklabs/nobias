package com.nobias.businesslogic.di;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.nobias.MyApplication;
import com.nobias.businesslogic.network.RetroFitInterface;
import com.nobias.businesslogic.preferences.AppSharedPreferences;
import com.nobias.businesslogic.rx.SchedulerProvider;
import com.nobias.view.activities.ActivityBase;
import com.nobias.view.fragments.FragmentBase;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Avantika Gadhiya on Apr, 16 2020 5:30.
 */

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(MyApplication myApplication);

    void inject(ActivityBase activityBase);

    void inject(FragmentBase fragmentBase);

    AppSharedPreferences getPreferences();

    RetroFitInterface getApiHelper();

    LocalBroadcastManager getLocalBroadcastManager();

    SchedulerProvider getSchedulerProvider();
}
