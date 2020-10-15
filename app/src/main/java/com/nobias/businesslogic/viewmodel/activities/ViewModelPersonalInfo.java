package com.nobias.businesslogic.viewmodel.activities;

import android.text.TextUtils;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;

import com.nobias.MyApplication;
import com.nobias.R;
import com.nobias.businesslogic.interactors.ObservableString;
import com.nobias.businesslogic.interactors.SingleLiveEvent;
import com.nobias.businesslogic.pojo.PojoUser;
import com.nobias.businesslogic.viewmodel.ViewModelCommon;


/**
 * Created by Brij Dholakia on Jul, 10 2018 18:40.
 * <p>
 * View model for login
 */
public class ViewModelPersonalInfo extends ViewModelCommon<PojoUser> {
    public ObservableString observerFirstName = new ObservableString("");
    public ObservableString observerLastName = new ObservableString("");
    public ObservableString observerEmail = new ObservableString("");
    public ObservableString observerConfirmEmail = new ObservableString("");
    public ObservableString observerDOB = new ObservableString("");
    public ObservableString observerCompanyName = new ObservableString("");
    public ObservableString observerPhoneNumber = new ObservableString("");
    public ObservableString observerJobTitle = new ObservableString("");

    public ObservableInt observerErrorFirstName = new ObservableInt();
    public ObservableInt observerErrorLastName = new ObservableInt();
    public ObservableInt observerErrorEmail = new ObservableInt();
    public ObservableInt observerErrorConfirmEmail = new ObservableInt();
    public ObservableInt observerErrorDOB = new ObservableInt();
    public ObservableInt observerErrorCompanyName = new ObservableInt();
    public ObservableInt observerErrorPhoneNumber = new ObservableInt();
    public ObservableInt observerErrorJobTitle = new ObservableInt();

    public ObservableBoolean observerErrorEnabledFirstName = new ObservableBoolean(false);
    public ObservableBoolean observerErrorEnabledLastName = new ObservableBoolean(false);
    public ObservableBoolean observerErrorEnabledEmail = new ObservableBoolean(false);
    public ObservableBoolean observerErrorEnabledConfirmEmail = new ObservableBoolean(false);
    public ObservableBoolean observerErrorEnabledDOB = new ObservableBoolean(false);
    public ObservableBoolean observerErrorEnabledCompanyName = new ObservableBoolean(false);
    public ObservableBoolean observerErrorEnabledPhoneNumber = new ObservableBoolean(false);
    public ObservableBoolean observerErrorEnabledJobTitle = new ObservableBoolean(false);

    private SingleLiveEvent<Boolean> liveEventPersonalInfo = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> liveEventDatePicker = new SingleLiveEvent<>();

    public ViewModelPersonalInfo(MyApplication application) {
        super(application, true);
    }

    public SingleLiveEvent<Boolean> getLiveEventPersonalInfo() {
        return liveEventPersonalInfo;
    }

    public SingleLiveEvent<Void> getLiveEventDatePicker() {
        return liveEventDatePicker;
    }

    public void onDateClick() {
        liveEventDatePicker.call();
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

    private boolean validateLastName() {
        if (TextUtils.isEmpty(observerLastName.getTrimmed())) {
            observerErrorEnabledLastName.set(true);
            observerErrorLastName.set(R.string.error_blank_lastname);
            return false;
        } else {
            observerErrorEnabledLastName.set(false);
            return true;
        }
    }

    private boolean validateEmail() {
        if (TextUtils.isEmpty(observerEmail.getTrimmed())) {
            observerErrorEnabledEmail.set(true);
            observerErrorEmail.set(R.string.error_blank_email);
            return false;
        } else {
            observerErrorEnabledEmail.set(false);
            return true;
        }
    }

    private boolean validateConfirmEmail() {
        if (TextUtils.isEmpty(observerConfirmEmail.getTrimmed())) {
            observerErrorEnabledConfirmEmail.set(true);
            observerErrorConfirmEmail.set(R.string.error_blank_confirm_email);
            return false;
        } else {
            observerErrorEnabledConfirmEmail.set(false);
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
        boolean lastName = validateLastName();
        boolean email = validateEmail();
        boolean confirmEmail = validateConfirmEmail();
        boolean dob = validateDOB();
        boolean companyName = validateCompanyName();
        boolean phoneNumber = validatePhoneNumber();
        boolean jobTitle = validateJobTitle();
        liveEventPersonalInfo.setValue(firstName && lastName && email && companyName &&
                confirmEmail && dob && phoneNumber && jobTitle);
    }

    @Override
    public void networkCallData() {
    }

    @Override
    public void sendResponseBodyData(PojoUser data) {
    }
}