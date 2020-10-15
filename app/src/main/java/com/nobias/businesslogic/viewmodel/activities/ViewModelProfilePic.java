package com.nobias.businesslogic.viewmodel.activities;

import android.text.TextUtils;

import androidx.databinding.ObservableField;

import com.nobias.BuildConfig;
import com.nobias.MyApplication;
import com.nobias.R;
import com.nobias.businesslogic.interactors.ObservableString;
import com.nobias.businesslogic.interactors.SingleLiveEvent;
import com.nobias.businesslogic.pojo.PojoCommonResponse;
import com.nobias.businesslogic.pojo.PojoCreateAccount;
import com.nobias.businesslogic.viewmodel.ViewModelCommon;

import java.io.ByteArrayOutputStream;

import okhttp3.MediaType;
import okhttp3.RequestBody;


/**
 * Created by Brij Dholakia on Jul, 10 2018 18:40.
 * <p>
 * View model for login
 */
public class ViewModelProfilePic extends ViewModelCommon<PojoCommonResponse> {
    public PojoCreateAccount mPojoCreateAccount;
    private boolean mUserRegisteredSuccess = false;
    public ObservableString observerPic = new ObservableString("");
    public ObservableField<ByteArrayOutputStream> observerStreamPic = new ObservableField<>();
    public ObservableString observerPicBase64 = new ObservableString("");
    public ObservableString observerPicUri = new ObservableString("");

    private SingleLiveEvent<Void> liveEventProfilePic = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> liveEventSubmit = new SingleLiveEvent<>();
    private SingleLiveEvent<PojoCommonResponse> liveEventRegisterSuccess = new SingleLiveEvent<>();

    public SingleLiveEvent<Void> getLiveEventProfilePic() {
        return liveEventProfilePic;
    }

    public SingleLiveEvent<Void> getLiveEventSubmit() {
        return liveEventSubmit;
    }

    public void onImageClick() {
        liveEventProfilePic.call();
    }

    public void onSubmitClick() {
        liveEventSubmit.call();
    }

    public SingleLiveEvent<PojoCommonResponse> getLiveEventRegisterSuccess() {
        return liveEventRegisterSuccess;
    }

    public ViewModelProfilePic(MyApplication application, PojoCreateAccount pojoCreateAccount) {
        super(application, true);
        mPojoCreateAccount = pojoCreateAccount;
    }

    @Override
    public void networkCallData() {
        setIsToShowErrors(true);
        String base64Pic = "data:image/jpeg;base64," + observerPicBase64.getTrimmed();
        if (TextUtils.equals(mPojoCreateAccount.getUserType(), mApplication.getString(R.string.user)) && !mUserRegisteredSuccess) {
            mApplication.getRetroFitInterface().registerUser(
                    mApplication.getString(R.string.bearer) + " " + BuildConfig.APP_TOKEN,
                    RequestBody.create(MediaType.parse("multipart/form-data"), BuildConfig.APP_TOKEN),
                    RequestBody.create(MediaType.parse("multipart/form-data"), mPojoCreateAccount.getFirstName() + " " + mPojoCreateAccount.getLastName()),
                    RequestBody.create(MediaType.parse("multipart/form-data"), mPojoCreateAccount.getEmail()),
                    RequestBody.create(MediaType.parse("multipart/form-data"), mPojoCreateAccount.getPassword()),
                    RequestBody.create(MediaType.parse("multipart/form-data"), mPojoCreateAccount.getDob()),
                    RequestBody.create(MediaType.parse("multipart/form-data"), mPojoCreateAccount.getPhoneNumber()),
                    RequestBody.create(MediaType.parse("multipart/form-data"), mPojoCreateAccount.getCompanyName()),
                    RequestBody.create(MediaType.parse("multipart/form-data"), mPojoCreateAccount.getJobTitle()),
                    RequestBody.create(MediaType.parse("multipart/form-data"), base64Pic)).enqueue(mCallbackData);
        } else if (TextUtils.equals(mPojoCreateAccount.getUserType(), mApplication.getString(R.string.consultant)) && !mUserRegisteredSuccess) {
            mApplication.getRetroFitInterface().registerConsultant(
                    mApplication.getString(R.string.bearer) + " " + BuildConfig.APP_TOKEN,
                    RequestBody.create(MediaType.parse("multipart/form-data"), BuildConfig.APP_TOKEN),
                    RequestBody.create(MediaType.parse("multipart/form-data"), mPojoCreateAccount.getFirstName() + " " + mPojoCreateAccount.getLastName()),
                    RequestBody.create(MediaType.parse("multipart/form-data"), mPojoCreateAccount.getEmail()),
                    RequestBody.create(MediaType.parse("multipart/form-data"), mPojoCreateAccount.getPassword()),
                    RequestBody.create(MediaType.parse("multipart/form-data"), mPojoCreateAccount.getDob()),
                    RequestBody.create(MediaType.parse("multipart/form-data"), mPojoCreateAccount.getPhoneNumber()),
                    RequestBody.create(MediaType.parse("multipart/form-data"), mPojoCreateAccount.getCompanyName()),
                    RequestBody.create(MediaType.parse("multipart/form-data"), mPojoCreateAccount.getJobTitle()),
                    RequestBody.create(MediaType.parse("multipart/form-data"), base64Pic)).enqueue(mCallbackData);
        }else if(mUserRegisteredSuccess)
        {
            mApplication.getRetroFitInterface().login(BuildConfig.APP_TOKEN,
                    mPojoCreateAccount.getEmail(),
                    mPojoCreateAccount.getPassword(),
                    mPreferences.getString(R.string.prefGcmToken)).enqueue(mCallbackData);
        }
    }

    @Override
    public void sendResponseBodyData(PojoCommonResponse pojoCommonResponse) {
        if (pojoCommonResponse.getSuccess()) {
            if (!mUserRegisteredSuccess) {
                mUserRegisteredSuccess = true;
                networkCallData();
            } else
                liveEventRegisterSuccess.setValue(pojoCommonResponse);
        } else {
            observerSnackBarInt.set(R.string.message_something_wrong);
        }
    }
}