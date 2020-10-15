package com.nobias.view.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.nobias.R;
import com.nobias.businesslogic.pojo.PojoSaveRequest;
import com.nobias.businesslogic.viewmodel.fragments.ViewModelScheduleTab;
import com.nobias.databinding.FragmentScheduleTabBinding;
import com.nobias.utils.Utils;
import com.nobias.utils.UtilsDate;
import com.nobias.utils.constants.ConstantCodes;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Smit Shah on Aug 22 2018, 11:38 AM
 * <p>
 * Fragment for attendance log
 */
public class FragmentScheduleTab extends FragmentBase{

    private ViewModelScheduleTab mVMScheduleTab;
    private View mParentView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentScheduleTabBinding binding = DataBindingUtil.inflate(inflater, R.layout
                .fragment_schedule_tab, container, false);
        mVMScheduleTab = new ViewModelScheduleTab(mApplication);
        binding.setVmScheduleTab(mVMScheduleTab);
        mParentView = binding.getRoot();
        observerEvents();
        return binding.getRoot();
    }

    /**
     * Observes events provided by live data single events from login and forgot password view
     * models
     */
    private void observerEvents() {
        mVMScheduleTab.getLiveEventNeedHelp().observe(this, areContentsValid -> {
            Utils.hideKeyboard(mMainActivity);
            PojoSaveRequest pojoSaveRequest = new PojoSaveRequest();
            pojoSaveRequest.setNeedType(mContext.getResources().getString(R.string.needTypeNow));
            Date date = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat(ConstantCodes.DATE_FORMAT_API);
            String formattedDate = df.format(date);
            pojoSaveRequest.setSessionDate(formattedDate);
            pojoSaveRequest.setSessionCategory("Immediate Consult Needed");

            Fragment fragment = new FragmentConsultants();
            Bundle bundle = new Bundle();
            bundle.putSerializable(mContext.getResources().getString(R.string.bundleSaveRequest),pojoSaveRequest);
            fragment.setArguments(bundle);
            mMainActivity.addFragment(fragment, R.string.tagFragmentConsultants, R.string.tagFragmentConsultants);
        });

        mVMScheduleTab.getLiveEventWantConsultant().observe(this, areContentsValid -> {
            Utils.hideKeyboard(mMainActivity);
            Fragment fragment = new FragmentSchedule();
            mMainActivity.addFragment(fragment, R.string.tagFragmentSchedule, R.string.tagFragmentSchedule);
        });

        mVMScheduleTab.getLiveEventSubmit().observe(this, areContentsValid -> {
            Utils.hideKeyboard(mMainActivity);
            if(areContentsValid) {
                Fragment fragment = new FragmentTimeSlot();
                PojoSaveRequest pojoSaveRequest = new PojoSaveRequest();
                pojoSaveRequest.setNeedType(getString(R.string.needTypeCode));
                pojoSaveRequest.setSessionDate(UtilsDate.changeDateformat(mVMScheduleTab.observerDate.getTrimmed(),
                        ConstantCodes.DATE_FORMAT, ConstantCodes.DATE_FORMAT_API));
                pojoSaveRequest.setConsultantId(mVMScheduleTab.observerConsultantCode.getTrimmed());
                pojoSaveRequest.setSessionCategory("Consultation Needed");
                Bundle bundle = new Bundle();
                bundle.putSerializable(getString(R.string.bundleSaveRequest), pojoSaveRequest);
                fragment.setArguments(bundle);
                mMainActivity.addFragment(fragment, R.string.tagFragmentTimeSlot, R.string.tagFragmentTimeSlot);
            }
        });

        mVMScheduleTab.getLiveEventDatePicker().observe(this, aVoid -> {
            Utils.hideKeyboard(mMainActivity);
            setDateDialog();
        });
    }

    private void setDateDialog() {
        DatePickerDialog dialog;
        int year;
        int month;
        int day;
        if (mVMScheduleTab.observerDate.getTrimmedLength() > 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(UtilsDate.dateToTimestamp(mVMScheduleTab.observerDate.getTrimmed(),
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
        dialog = new DatePickerDialog(mMainActivity, (view, year1, monthOfYear, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year1);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            mVMScheduleTab.observerDate.set(new SimpleDateFormat(ConstantCodes.DATE_FORMAT, Locale
                    .getDefault()).format(calendar.getTime()));
        }, year, month, day);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 2);
        dialog.getDatePicker().setMinDate(cal.getTimeInMillis());
        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
