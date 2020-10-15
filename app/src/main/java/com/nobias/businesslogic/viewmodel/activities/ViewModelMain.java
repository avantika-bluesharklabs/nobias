package com.nobias.businesslogic.viewmodel.activities;

import android.text.TextUtils;

import com.nobias.MyApplication;
import com.nobias.R;
import com.nobias.businesslogic.pojo.PojoCommonResponse;
import com.nobias.businesslogic.viewmodel.ViewModelCommon;

/**
 * Created by Naria Sachin on Feb, 26 2020 12:10.
 */

public class ViewModelMain extends ViewModelCommon<PojoCommonResponse> {
    public ViewModelMain(MyApplication application) {
        super(application,false);
    }

    @Override
    public void networkCallData() {
        if(!TextUtils.isEmpty(mApplication.getAppSharedPreferences().getString(R.string.prefGcmToken))) {
            mApplication.getRetroFitInterface().pushNotificationToken(
                    mPreferences.getString(R.string.prefAccessToken),
                    mApplication.getAppSharedPreferences().getString(R.string.prefGcmToken)).enqueue(mCallbackData);
        }
    }

    @Override
    public void sendResponseBodyData(PojoCommonResponse pojoCommonResponse) {

    }
}
