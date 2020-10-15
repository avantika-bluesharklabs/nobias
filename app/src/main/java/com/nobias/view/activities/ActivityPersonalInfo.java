package com.nobias.view.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.nobias.R;
import com.nobias.businesslogic.pojo.PojoCreateAccount;
import com.nobias.businesslogic.viewmodel.activities.ViewModelPersonalInfo;
import com.nobias.databinding.ActivityPersonalInfoBinding;
import com.nobias.utils.Utils;
import com.nobias.utils.UtilsDate;
import com.nobias.utils.constants.ConstantCodes;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ActivityPersonalInfo extends ActivityBase {
    private ViewModelPersonalInfo mViewModelPersonalInfo;
    private String mPassword="", mConfirmPassword ="",mUserType= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPersonalInfoBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_personal_info);
        mViewModelPersonalInfo = new ViewModelPersonalInfo(mApplication);
        mBinding.setVmPersonalInfo(mViewModelPersonalInfo);
        mPassword = getIntent().getStringExtra("password");
        mConfirmPassword = getIntent().getStringExtra("confirm_password");
        mUserType = getIntent().getStringExtra("user_type");
        observerEvents();
    }

    /**
     * Observes events provided by live data single events from login and forgot password view
     * models
     */
    private void observerEvents() {
        mViewModelPersonalInfo.getLiveEventDatePicker().observe(this, aVoid -> {
            Utils.hideKeyboard(ActivityPersonalInfo.this);
            setDateDialog();
        });

        mViewModelPersonalInfo.getLiveEventPersonalInfo().observe(this, areContentsValid -> {
            Utils.hideKeyboard(ActivityPersonalInfo.this);
            if (areContentsValid) {
                PojoCreateAccount pojoCreateAccount = new PojoCreateAccount(mPassword,mConfirmPassword,
                        mViewModelPersonalInfo.observerFirstName.getTrimmed(),
                        mViewModelPersonalInfo.observerLastName.getTrimmed(),
                        mViewModelPersonalInfo.observerEmail.getTrimmed(),
                        mViewModelPersonalInfo.observerConfirmEmail.getTrimmed(),
                        UtilsDate.changeDateformat(mViewModelPersonalInfo.observerDOB.getTrimmed(),ConstantCodes.DATE_FORMAT,
                        ConstantCodes.DATE_FORMAT_API),
                        mViewModelPersonalInfo.observerCompanyName.getTrimmed(),
                        mViewModelPersonalInfo.observerPhoneNumber.getTrimmed(),
                        mViewModelPersonalInfo.observerJobTitle.getTrimmed(), mUserType);

                Intent intent = new Intent(ActivityPersonalInfo.this, ActivityProfilePic.class);
                intent.putExtra("personal_info", pojoCreateAccount);
                startActivity(intent);
                //overridePendingTransition(0, 0);
                //finish();
            }
        });
    }

    private void setDateDialog() {
        DatePickerDialog dialog;
        int year;
        int month;
        int day;
        if (mViewModelPersonalInfo.observerDOB.getTrimmedLength() > 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(UtilsDate.dateToTimestamp(mViewModelPersonalInfo.observerDOB.getTrimmed(),
                    ConstantCodes.DATE_FORMAT));
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        } else {
            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }
        dialog = new DatePickerDialog(ActivityPersonalInfo.this, (view, year1, monthOfYear, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year1);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            mViewModelPersonalInfo.observerDOB.set(new SimpleDateFormat(ConstantCodes.DATE_FORMAT, Locale
                    .getDefault()).format(calendar.getTime()));
        }, year, month, day);

        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_YEAR, -1);
        yesterday = UtilsDate.clearTimes(yesterday);
        dialog.getDatePicker().setMaxDate(yesterday.getTimeInMillis());
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
