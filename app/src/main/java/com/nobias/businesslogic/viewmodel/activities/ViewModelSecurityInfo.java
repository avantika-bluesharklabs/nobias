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
public class ViewModelSecurityInfo extends ViewModelCommon<PojoUser> {
    public ObservableString observerConfirmPassword = new ObservableString("");
    public ObservableInt observerErrorConfirmPassword = new ObservableInt();
    public ObservableBoolean observerErrorEnabledConfirmPassword = new ObservableBoolean(false);

    public ObservableString observerPassword = new ObservableString("");
    public ObservableInt observerErrorPassword = new ObservableInt();
    public ObservableBoolean observerErrorEnabledPassword = new ObservableBoolean(false);

    private SingleLiveEvent<Boolean> liveEventSecurityInfo = new SingleLiveEvent<>();

    public ViewModelSecurityInfo(MyApplication application) {
        super(application, true);
    }

    public SingleLiveEvent<Boolean> getLiveEventSecurityInfo() {
        return liveEventSecurityInfo;
    }

    private boolean validatePassword() {
        if (TextUtils.isEmpty(observerPassword.getTrimmed())) {
            observerErrorEnabledPassword.set(true);
            observerErrorPassword.set(R.string.error_blank_password);
            return false;
        } /*else if (observerPassword.getTrimmedLength() <= 6) {
            observerErrorEnabledPassword.set(true);
            observerErrorPassword.set(R.string.error_invalid_password);
            return false;
        }*/ else {
            observerErrorEnabledPassword.set(false);
            return true;
        }
    }

    private boolean validateConfirmPassword() {
        if (TextUtils.isEmpty(observerConfirmPassword.getTrimmed())) {
            observerErrorEnabledConfirmPassword.set(true);
            observerErrorConfirmPassword.set(R.string.error_blank_confirm_password);
            return false;
        } /*else if (observerConfirmPassword.getTrimmedLength() <= 6) {
            observerErrorEnabledConfirmPassword.set(true);
            observerErrorConfirmPassword.set(R.string.error_invalid_password);
            return false;
        }*/ else if (!TextUtils.equals(observerPassword.getTrimmed(), observerConfirmPassword.getTrimmed())) {
            observerErrorEnabledConfirmPassword.set(true);
            observerErrorConfirmPassword.set(R.string.error_password_not_matched);
            return false;
        }else {
            observerErrorEnabledConfirmPassword.set(false);
            return true;
        }
    }

    public void onLetsContinue() {
        boolean validateConfirmPassword = validateConfirmPassword();
        boolean validatePassword = validatePassword();
        liveEventSecurityInfo.setValue(validateConfirmPassword && validatePassword);
    }

    @Override
    public void networkCallData() {
    }

    @Override
    public void sendResponseBodyData(PojoUser data) {
    }
}