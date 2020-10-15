package com.nobias.view.fragments;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.nobias.R;
import com.nobias.businesslogic.viewmodel.fragments.ViewModelReschedule;
import com.nobias.databinding.FragmentRescheduleBinding;
import com.nobias.utils.Utils;
import com.nobias.utils.UtilsDate;
import com.nobias.utils.constants.ConstantCodes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Smit Shah on Aug 22 2018, 11:38 AM
 * <p>
 * Fragment for attendance log
 */
public class FragmentReschedule extends FragmentBase {
    private ViewModelReschedule mVMReschedule;
    private View mParentView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentRescheduleBinding binding = DataBindingUtil.inflate(inflater, R.layout
                .fragment_reschedule, container, false);
        mVMReschedule = new ViewModelReschedule(mApplication);
        binding.setVmReschedule(mVMReschedule);
        mParentView = binding.getRoot();
        initialiseVariables();
        observerEvents();
        return binding.getRoot();
    }

    private void initialiseVariables() {
        mVMReschedule.appointmentID = getArguments().getString(mContext.getResources().getString(R.string
                .bundleRescheduleAppointmentID));

        List<String> time = new ArrayList<>();
        time.add("8:30 am");
        time.add("9:00 am");
        time.add("9:30 am");
        time.add("10:00 am");
        time.add("10:30 am");
        time.add("11:00 am");
        time.add("11:30 am");
        time.add("12:00 pm");
        time.add("12:30 pm");
        time.add("1:00 pm");
        time.add("1:30 pm");
        time.add("2:00 pm");
        time.add("2:30 pm");
        time.add("3:00 pm");
        time.add("3:30 pm");
        time.add("4:00 pm");
        time.add("4:30 pm");
        time.add("5:00 pm");
        time.add("5:50 pm");
        time.add("6:00 pm");
        time.add("6:30 pm");
        mVMReschedule.observerListTimeSlot.addAll(time);
    }

    /**
     * Observes events provided by live data single events from login and forgot password view
     * models
     */
    private void observerEvents() {
        mVMReschedule.getLiveEventMessage().observe(this, aVoid -> {
            Utils.hideKeyboard(mMainActivity);
            showMessageDialog(aVoid);
        });

        mVMReschedule.getLiveEventDatePicker().observe(this, aVoid -> {
            Utils.hideKeyboard(mMainActivity);
            setDateDialog();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void setDateDialog() {
        DatePickerDialog dialog;
        int year;
        int month;
        int day;
        if (mVMReschedule.observerDate.getTrimmedLength() > 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(UtilsDate.dateToTimestamp(mVMReschedule.observerDate.getTrimmed(),
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
            mVMReschedule.observerDate.set(new SimpleDateFormat(ConstantCodes.DATE_FORMAT, Locale
                    .getDefault()).format(calendar.getTime()));
        }, year, month, day);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 2);
        dialog.getDatePicker().setMinDate(cal.getTimeInMillis());
        dialog.show();
    }

    private void showMessageDialog(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton(getString(R.string.message_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                Intent i = new Intent(mApplication.getResources().getString(R
                        .string.broadcastNobiasRefreshList));
                i.putExtra(mApplication.getResources().getString(R.string.bundleIsFromCalendarPermission),false);
                mApplication.getAppComponent().getLocalBroadcastManager().sendBroadcast(i);
                mMainActivity.getSupportFragmentManager().popBackStack();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setTitle("");
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
}
