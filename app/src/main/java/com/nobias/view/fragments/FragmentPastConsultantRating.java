package com.nobias.view.fragments;

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
import com.nobias.businesslogic.viewmodel.fragments.ViewModelPastConsultantRating;
import com.nobias.databinding.FragmentPastConsultantsRatingBinding;
import com.nobias.utils.Utils;

/**
 * Created by Smit Shah on Aug 22 2018, 11:38 AM
 * <p>
 * Fragment for attendance log
 */
public class FragmentPastConsultantRating extends FragmentBase{
    private ViewModelPastConsultantRating mVMPastConsultantRating;
    private View mParentView;
    private String mAppointmentId = "";
    private int mRating = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentPastConsultantsRatingBinding binding = DataBindingUtil.inflate(inflater, R.layout
                .fragment_past_consultants_rating, container, false);
        mAppointmentId = getArguments().getString(mContext.getResources().getString(R.string
                .bundlePastConsultantApptId));
        mRating = getArguments().getInt(mContext.getResources().getString(R.string
                .bundlePastConsultantRate));
        mVMPastConsultantRating = new ViewModelPastConsultantRating(mApplication, mAppointmentId, mRating);
        binding.setVmPastConsultantRating(mVMPastConsultantRating);
        mParentView = binding.getRoot();
        observerEvents();
        return binding.getRoot();
    }

    /**
     * Observes events provided by live data single events from login and forgot password view
     * models
     */
    private void observerEvents() {
        mVMPastConsultantRating.getLiveEventSave().observe(this, aVoid -> {
            Utils.hideKeyboard(mMainActivity);
            if(mRating != mVMPastConsultantRating.observerRating.get())
                mVMPastConsultantRating.networkCallList();
        });

        mVMPastConsultantRating.getLiveEventSuccess().observe(this, aVoid -> {
            Utils.hideKeyboard(mMainActivity);
            Intent intent = new Intent(mApplication.getResources().getString(R
                    .string.broadcastPastConsultantRate));
            intent.putExtra(mApplication.getResources().getString(R.string.bundlePastConsultantRate),mVMPastConsultantRating.observerRating.get());
            mApplication.getAppComponent().getLocalBroadcastManager()
                    .sendBroadcast(intent);
            showSavedDialog();
        });
    }

    private void showSavedDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
        alertDialogBuilder.setMessage(getString(R.string.message_rating_saved));
        alertDialogBuilder.setPositiveButton(getString(R.string.message_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                mMainActivity.onBackPressed();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setTitle(getString(R.string.title_rating_saved));
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
