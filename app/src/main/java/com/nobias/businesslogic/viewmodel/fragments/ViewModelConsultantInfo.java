package com.nobias.businesslogic.viewmodel.fragments;

import android.text.TextUtils;

import androidx.databinding.ObservableBoolean;

import com.nobias.MyApplication;
import com.nobias.R;
import com.nobias.businesslogic.interactors.ObservableString;
import com.nobias.businesslogic.pojo.PojoCommonResponse;
import com.nobias.businesslogic.viewmodel.ViewModelCommon;

import static com.nobias.utils.constants.RetrofitConstants.METHOD_GET_CONSULTANTS;

/**
 * Created by Smit Shah on Aug 22 2018, 10:24 AM
 * <p>
 * View model for attendance log
 */
public class ViewModelConsultantInfo extends ViewModelCommon<PojoCommonResponse> {
    public ObservableString observerConsultantId = new ObservableString();
    public ObservableString observerName = new ObservableString();
    public ObservableString observerPic = new ObservableString();
    public ObservableString observerRating = new ObservableString();
    public ObservableString observerTitle = new ObservableString();
    public ObservableBoolean observableDataLoaded = new ObservableBoolean(false);

    public ViewModelConsultantInfo(MyApplication application) {
        super(application, false);
    }

    @Override
    public void networkCallData() {
        observableDataLoaded.set(false);
        setProgressBar(true);
        String url = METHOD_GET_CONSULTANTS + "/" + observerConsultantId.getTrimmed();
        mApplication.getRetroFitInterface().getConsultants(mPreferences.getString(R.string.prefAccessToken), url)
                .enqueue(mCallbackData);
    }

    @Override
    public void sendResponseBodyData(PojoCommonResponse pojoCommonResponse) {
        if (pojoCommonResponse.getSuccess()) {
            if (pojoCommonResponse.getPojoConsultantInfo() != null) {
                observerName.set(pojoCommonResponse.getPojoConsultantInfo().getName());
                if (pojoCommonResponse.getPojoConsultantInfo().getId() != null)
                    observerRating.set(String.valueOf(pojoCommonResponse.getPojoConsultantInfo().getId()));
                observerPic.set(pojoCommonResponse.getPojoConsultantInfo().getProfileThumbPath());
                if(!TextUtils.isEmpty(pojoCommonResponse.getPojoConsultantInfo().getTitle()))
                {
                    String title = pojoCommonResponse.getPojoConsultantInfo().getTitle();
                    if(!TextUtils.isEmpty(pojoCommonResponse.getPojoConsultantInfo().getCompany()))
                        title += pojoCommonResponse.getPojoConsultantInfo().getCompany();
                    observerTitle.set(title);
                }
            }
            observableDataLoaded.set(true);
        } else {
            observerSnackBarInt.set(R.string.message_something_wrong);
        }
    }
}