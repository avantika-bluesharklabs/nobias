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
import com.nobias.businesslogic.viewmodel.fragments.ViewModelSchedule;
import com.nobias.databinding.FragmentScheduleBinding;
import com.nobias.utils.Utils;
import com.nobias.utils.UtilsDate;
import com.nobias.utils.constants.ConstantCodes;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Smit Shah on Aug 22 2018, 11:38 AM
 * <p>
 * Fragment for attendance log
 */
public class FragmentSchedule extends FragmentBase{

    private ViewModelSchedule mVMSchedule;
    private View mParentView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentScheduleBinding binding = DataBindingUtil.inflate(inflater, R.layout
                .fragment_schedule, container, false);
        mVMSchedule = new ViewModelSchedule(mApplication);
        binding.setVmSchedule(mVMSchedule);
        mParentView = binding.getRoot();
        observerEvents();
        return binding.getRoot();
    }

    /**
     * Observes events provided by live data single events from login and forgot password view
     * models
     */
    private void observerEvents() {
        mVMSchedule.getLiveEventDatePicker().observe(this, aVoid -> {
            Utils.hideKeyboard(mMainActivity);
            setDateDialog();
        });

        mVMSchedule.getLiveEventContinue().observe(this, aVoid -> {
            Utils.hideKeyboard(mMainActivity);
            Fragment fragment = new FragmentConsultants();
            PojoSaveRequest pojoSaveRequest = new PojoSaveRequest();
            pojoSaveRequest.setNeedType(getString(R.string.needTypeFuture));
            pojoSaveRequest.setSessionDate( UtilsDate.changeDateformat(mVMSchedule.observerSessionDate.getTrimmed(),
                    ConstantCodes.DATE_FORMAT, ConstantCodes.DATE_FORMAT_API));
            pojoSaveRequest.setSessionCategory(mVMSchedule.observerListSessionTopic.get(
                    mVMSchedule.observerSessionTopicPosition.get()).getCategory());
            pojoSaveRequest.setSessionName(mVMSchedule.observerListSessionTopic.get(
                    mVMSchedule.observerSessionTopicPosition.get()).getName());
            Bundle bundle = new Bundle();
            bundle.putSerializable(getString(R.string.bundleSaveRequest), pojoSaveRequest);
            fragment.setArguments(bundle);
            mMainActivity.addFragment(fragment, R.string.tagFragmentConsultants, R.string.tagFragmentConsultants);
        });
    }

    private void setDateDialog() {
        DatePickerDialog dialog;
        int year;
        int month;
        int day;
        if (mVMSchedule.observerSessionDate.getTrimmedLength() > 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(UtilsDate.dateToTimestamp(mVMSchedule.observerSessionDate.getTrimmed(),
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
            mVMSchedule.observerSessionDate.set(new SimpleDateFormat(ConstantCodes.DATE_FORMAT, Locale
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
