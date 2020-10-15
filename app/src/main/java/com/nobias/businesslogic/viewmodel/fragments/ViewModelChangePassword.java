package com.nobias.businesslogic.viewmodel.fragments;

import android.text.TextUtils;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;

import com.nobias.MyApplication;
import com.nobias.R;
import com.nobias.businesslogic.interactors.ObservableString;
import com.nobias.businesslogic.interactors.SingleLiveEvent;
import com.nobias.businesslogic.pojo.PojoCommonResponse;
import com.nobias.businesslogic.viewmodel.ViewModelCommon;
import com.nobias.utils.Utils;


/**
 * Created by Brij Dholakia on Jul, 10 2018 18:40.
 * <p>
 * View model for login
 */
public class ViewModelChangePassword extends ViewModelCommon<PojoCommonResponse> {
    public ObservableString observerConfirmPassword = new ObservableString("");
    public ObservableInt observerErrorConfirmPassword = new ObservableInt();
    public ObservableBoolean observerErrorEnabledConfirmPassword = new ObservableBoolean(false);

    public ObservableString observerPassword = new ObservableString("");
    public ObservableInt observerErrorPassword = new ObservableInt();
    public ObservableBoolean observerErrorEnabledPassword = new ObservableBoolean(false);

    private SingleLiveEvent<String> liveEventSave = new SingleLiveEvent<>();

    public ViewModelChangePassword(MyApplication application) {
        super(application, true);
    }

    public SingleLiveEvent<String> getLiveEventSave() {
        return liveEventSave;
    }

    private boolean validatePassword() {
        if (TextUtils.isEmpty(observerPassword.getTrimmed())) {
            observerErrorEnabledPassword.set(true);
            observerErrorPassword.set(R.string.error_blank_password);
            return false;
        } else if (!Utils.isValidPassword(observerPassword.getTrimmed())) {
            observerErrorEnabledPassword.set(true);
            observerErrorPassword.set(R.string.error_invalid_password);
            return false;
        } else {
            observerErrorEnabledPassword.set(false);
            return true;
        }
    }

    private boolean validateConfirmPassword() {
        if (TextUtils.isEmpty(observerConfirmPassword.getTrimmed())) {
            observerErrorEnabledConfirmPassword.set(true);
            observerErrorConfirmPassword.set(R.string.error_blank_confirm_password);
            return false;
        } else if (!Utils.isValidPassword(observerConfirmPassword.getTrimmed())) {
            observerErrorEnabledConfirmPassword.set(true);
            observerErrorConfirmPassword.set(R.string.error_invalid_password);
            return false;
        } else if (!TextUtils.equals(observerPassword.getTrimmed(), observerConfirmPassword.getTrimmed())) {
            observerErrorEnabledConfirmPassword.set(true);
            observerErrorConfirmPassword.set(R.string.error_password_not_matched);
            return false;
        }else {
            observerErrorEnabledConfirmPassword.set(false);
            return true;
        }
    }

    public void onSaveClick() {
        boolean validateConfirmPassword = validateConfirmPassword();
        boolean validatePassword = validatePassword();
        networkCallData();
    }

    @Override
    public void networkCallData() {
    }

    @Override
    public void sendResponseBodyData(PojoCommonResponse pojoCommonResponse) {
        if (pojoCommonResponse.getSuccess()) {
            if(!TextUtils.isEmpty(pojoCommonResponse.getMessage()))
                liveEventSave.setValue(pojoCommonResponse.getMessage());
        } else {
            observerSnackBarInt.set(R.string.message_something_wrong);
        }
    }
}