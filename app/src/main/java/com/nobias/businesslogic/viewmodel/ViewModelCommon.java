package com.nobias.businesslogic.viewmodel;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;

import com.bumptech.glide.request.RequestOptions;
import com.nobias.MyApplication;
import com.nobias.R;
import com.nobias.businesslogic.interactors.ObservableString;
import com.nobias.businesslogic.pojo.PojoCommonResponse;
import com.nobias.businesslogic.pojo.PojoSetHours;
import com.nobias.businesslogic.preferences.AppSharedPreferences;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Brij Dholakia on Sep, 19 2018 18:11.
 * <p>
 * Common view model
 */
public abstract class ViewModelCommon<X> {

    protected MyApplication mApplication;
    protected AppSharedPreferences mPreferences;
    private boolean mIsToShowErrors, mIsFromSetHours = false;
    public RequestOptions requestOptionCentreProfilePic;

    public ObservableBoolean observerProgressBar = new ObservableBoolean(false);
    public ObservableInt observerSnackBarInt = new ObservableInt();
    public ObservableString observerSnackBarString = new ObservableString("");
    public ObservableInt observerNoRecords = new ObservableInt(R.string.text_norecordfound);
    public ObservableBoolean observerNoRecordsVisibility = new ObservableBoolean(false);
    public ObservableBoolean observerDataVisibility = new ObservableBoolean(false);

    public ViewModelCommon(MyApplication application, boolean isToShowErrors) {
        mApplication = application;
        mPreferences = application.getAppComponent().getPreferences();
        mIsToShowErrors = isToShowErrors;

        requestOptionCentreProfilePic = application.glideCenterCircle(R.drawable
                .placeholder_circle_big);
    }

    public ViewModelCommon(MyApplication application, boolean isToShowErrors, boolean isSetHours) {
        mApplication = application;
        mPreferences = application.getAppComponent().getPreferences();
        mIsToShowErrors = isToShowErrors;
        mIsFromSetHours = isSetHours;
        requestOptionCentreProfilePic = application.glideCenterCircle(R.drawable
                .placeholder_circle_big);
    }
    public void getPostData() {
        if (mApplication.isInternetConnected()) {
            observerProgressBar.set(mIsToShowErrors);
            networkCallData();
        } else {
            if (mIsToShowErrors) {
                observerSnackBarInt.set(R.string.message_noconnection);
            }
        }
    }

    public abstract void networkCallData();

    public boolean isIsToShowErrors() {
        return mIsToShowErrors;
    }

    public void setIsToShowErrors(boolean mIsToShowErrors) {
        this.mIsToShowErrors = mIsToShowErrors;
        this.observerProgressBar.set(mIsToShowErrors);
    }

    public abstract void sendResponseBodyData(X data);

    /**
     * Callback for network call
     */
    protected Callback<X> mCallbackData = new Callback<X>() {
        @Override
        public void onResponse(Call<X> call, Response<X> response) {
            if (response != null && response.isSuccessful() && response.body() != null) {
                X body = response.body();
                boolean isSuccess = false;
                if(mIsFromSetHours)
                    isSuccess =  ((PojoSetHours) body).getSuccess();
                else
                    isSuccess = ((PojoCommonResponse) body).getSuccess();
                if (isSuccess) {
                    sendResponseBodyData(body);
                } else {
                    if (mIsToShowErrors) {
                        observerNoRecordsVisibility.set(true);
                        observerSnackBarInt.set(R.string.message_something_wrong);
                    }
                }
            } else {
                if (mIsToShowErrors) {
                    observerNoRecordsVisibility.set(true);
                    observerSnackBarInt.set(R.string.message_something_wrong);
                }
            }
            observerProgressBar.set(false);
        }

        @Override
        public void onFailure(Call<X> call, Throwable t) {
            if (mIsToShowErrors) {
                observerNoRecordsVisibility.set(true);
                observerSnackBarInt.set(R.string.message_something_wrong);
            }
            observerProgressBar.set(false);
        }
    };

    public void setProgressBar(Boolean visible)
    {
        observerProgressBar.set(visible);
    }
}
