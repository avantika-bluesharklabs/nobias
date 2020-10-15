package com.nobias.businesslogic.viewmodel;

import android.text.TextUtils;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.MutableLiveData;

import com.nobias.MyApplication;
import com.nobias.businesslogic.interactors.ObservableString;
import com.nobias.businesslogic.network.RetroFitInterface;
import com.nobias.businesslogic.preferences.AppSharedPreferences;
import com.nobias.businesslogic.rx.SchedulerProvider;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Avantika Gadhiya on Apr, 16 2020 5:30.
 */

public class ViewModelBase {
    public ObservableBoolean observerProgress = new ObservableBoolean(false);

    public final ObservableBoolean observableShowLoading = new ObservableBoolean(false);
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    public final ObservableInt observerSnackBarInt = new ObservableInt();
    public final ObservableString observerSnackBarString = new ObservableString("");
    private MutableLiveData<Void> liveDataTokenExpire = new MutableLiveData<>();

    private MyApplication mApplication;
    private AppSharedPreferences mPreferences;
    private RetroFitInterface mApiHelper;
    private SchedulerProvider mSchedulerProvider;

    public ViewModelBase(MyApplication application) {
        mApplication = application;
        initializeVariables();
    }

    private void initializeVariables() {
        mPreferences = mApplication.getAppComponent().getPreferences();
        mApiHelper = mApplication.getAppComponent().getApiHelper();
        mSchedulerProvider = mApplication.getAppComponent().getSchedulerProvider();
    }

    public MyApplication getApplication() {
        return mApplication;
    }

    public RetroFitInterface getApiHelper() {
        return mApiHelper;
    }

    protected AppSharedPreferences getPreferences() {
        return mPreferences;
    }

    public CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }

    public SchedulerProvider getSchedulerProvider() {
        return mSchedulerProvider;
    }

    public MutableLiveData<Void> getLiveDataTokenExpire() {
        return liveDataTokenExpire;
    }

    public String getTrimmedData(String data) {
        return (!TextUtils.isEmpty(data) ? data.trim() : "");
    }
}
