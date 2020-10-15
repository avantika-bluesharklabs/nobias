package com.nobias.businesslogic.viewmodel.fragments;

import android.text.TextUtils;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.nobias.MyApplication;
import com.nobias.R;
import com.nobias.businesslogic.interactors.ObservableString;
import com.nobias.businesslogic.interactors.SingleLiveEvent;
import com.nobias.businesslogic.pojo.PojoCommonResponse;
import com.nobias.businesslogic.viewmodel.ViewModelCommon;
import com.nobias.utils.UtilsDate;
import com.nobias.utils.constants.ConstantCodes;

import java.io.ByteArrayOutputStream;


/**
 * Created by Brij Dholakia on Jul, 10 2018 18:40.
 * <p>
 * View model for login
 */
public class ViewModelUpdateInfo extends ViewModelCommon<PojoCommonResponse> {
    private boolean mUserRegisteredSuccess = false;

    public ObservableString observerFirstName = new ObservableString("");
    public ObservableString observerDOB = new ObservableString("");
    public ObservableString observerCompanyName = new ObservableString("");
    public ObservableString observerPhoneNumber = new ObservableString("");
    public ObservableString observerJobTitle = new ObservableString("");

    public ObservableInt observerErrorFirstName = new ObservableInt();
    public ObservableInt observerErrorDOB = new ObservableInt();
    public ObservableInt observerErrorCompanyName = new ObservableInt();
    public ObservableInt observerErrorPhoneNumber = new ObservableInt();
    public ObservableInt observerErrorJobTitle = new ObservableInt();

    public ObservableBoolean observerErrorEnabledFirstName = new ObservableBoolean(false);
    public ObservableBoolean observerErrorEnabledDOB = new ObservableBoolean(false);
    public ObservableBoolean observerErrorEnabledCompanyName = new ObservableBoolean(false);
    public ObservableBoolean observerErrorEnabledPhoneNumber = new ObservableBoolean(false);
    public ObservableBoolean observerErrorEnabledJobTitle = new ObservableBoolean(false);

    public ObservableString observerPic = new ObservableString("");
    public ObservableField<ByteArrayOutputStream> observerStreamPic = new ObservableField<>();
    public ObservableString observerPicBase64 = new ObservableString("");
    public ObservableString observerPicUri = new ObservableString("");

    private SingleLiveEvent<Void> liveEventProfilePic = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> liveEventSubmit = new SingleLiveEvent<>();
    private SingleLiveEvent<PojoCommonResponse> liveEventRegisterSuccess = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> liveEventDatePicker = new SingleLiveEvent<>();

    public SingleLiveEvent<Void> getLiveEventDatePicker() {
        return liveEventDatePicker;
    }

    public void onDateClick() {
        liveEventDatePicker.call();
    }

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

    public ViewModelUpdateInfo(MyApplication application) {
        super(application, true);
    }

    private boolean validateFirstName() {
        if (TextUtils.isEmpty(observerFirstName.getTrimmed())) {
            observerErrorEnabledFirstName.set(true);
            observerErrorFirstName.set(R.string.error_blank_firstname);
            return false;
        } else {
            observerErrorEnabledFirstName.set(false);
            return true;
        }
    }

    private boolean validateDOB() {
        if (TextUtils.isEmpty(observerDOB.getTrimmed())) {
            observerErrorEnabledDOB.set(true);
            observerErrorDOB.set(R.string.error_blank_dob);
            return false;
        } else {
            observerErrorEnabledDOB.set(false);
            return true;
        }
    }

    private boolean validateCompanyName() {
        if (TextUtils.isEmpty(observerCompanyName.getTrimmed())) {
            observerErrorEnabledCompanyName.set(true);
            observerErrorCompanyName.set(R.string.error_blank_companyname);
            return false;
        } else {
            observerErrorEnabledCompanyName.set(false);
            return true;
        }
    }

    private boolean validatePhoneNumber() {
        if (TextUtils.isEmpty(observerPhoneNumber.getTrimmed())) {
            observerErrorEnabledPhoneNumber.set(true);
            observerErrorPhoneNumber.set(R.string.error_blank_phone);
            return false;
        } else {
            observerErrorEnabledPhoneNumber.set(false);
            return true;
        }
    }

    private boolean validateJobTitle() {
        if (TextUtils.isEmpty(observerJobTitle.getTrimmed())) {
            observerErrorEnabledJobTitle.set(true);
            observerErrorJobTitle.set(R.string.error_blank_job_title);
            return false;
        } else {
            observerErrorEnabledJobTitle.set(false);
            return true;
        }
    }

    public void onContinueClick() {
        boolean firstName = validateFirstName();
        boolean dob = validateDOB();
        boolean companyName = validateCompanyName();
        boolean phoneNumber = validatePhoneNumber();
        boolean jobTitle = validateJobTitle();
        if(firstName && dob && companyName && phoneNumber && jobTitle)
            networkCallData();
    }

    @Override
    public void networkCallData() {
        setProgressBar(true);
        String pic = "";
        /*if(TextUtils.isEmpty(observerPicBase64.getTrimmed()))
            pic= "data:image/jpeg;base64," + UtilsImage.convertUrlToBase64(observerPic.getTrimmed());
        else*/
            pic = "data:image/jpeg;base64," + observerPicBase64.getTrimmed();
        if (!mApplication.mIsConsultant) {
            mApplication.getRetroFitInterface().updateUser(
                    mPreferences.getString(R.string.prefAccessToken),
                    observerFirstName.getTrimmed(),
                    observerDOB.getTrimmed(),
                    observerCompanyName.getTrimmed(),
                    observerPhoneNumber.getTrimmed(),
                    observerJobTitle.getTrimmed(),
                    pic).enqueue(mCallbackData);
        } else {
            mApplication.getRetroFitInterface().updateConsultant(
                    mPreferences.getString(R.string.prefAccessToken),
                    observerFirstName.getTrimmed(),
                    UtilsDate.changeDateformat(observerDOB.getTrimmed(), ConstantCodes.DATE_FORMAT, ConstantCodes.DATE_FORMAT_API),
                    observerCompanyName.getTrimmed(),
                    observerPhoneNumber.getTrimmed(),
                    observerJobTitle.getTrimmed(),
                    pic).enqueue(mCallbackData);
        }
    }

    @Override
    public void sendResponseBodyData(PojoCommonResponse pojoCommonResponse) {
        if (pojoCommonResponse.getSuccess()) {
            liveEventRegisterSuccess.setValue(pojoCommonResponse);
        } else {
            observerSnackBarInt.set(R.string.message_something_wrong);
        }
    }
}