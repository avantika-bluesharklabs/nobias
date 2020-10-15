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
import com.nobias.businesslogic.pojo.PojoSaveRequest;
import com.nobias.businesslogic.viewmodel.fragments.ViewModelConsultantSaveRequest;
import com.nobias.databinding.FragmentConsultantRequestBinding;
import com.nobias.utils.Utils;

/**
 * Created by Smit Shah on Aug 22 2018, 11:38 AM
 * <p>
 * Fragment for attendance log
 */
public class FragmentConsultantSaveRequest extends FragmentBase {
    private ViewModelConsultantSaveRequest mVMConsultantsSaveRequest;
    private PojoSaveRequest mPojoSaveRequest;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentConsultantRequestBinding binding = DataBindingUtil.inflate(inflater, R.layout
                .fragment_consultant_request, container, false);
        mVMConsultantsSaveRequest = new ViewModelConsultantSaveRequest(mApplication);
        mPojoSaveRequest = (PojoSaveRequest) getArguments().getSerializable(mContext.getResources().getString(R.string
                .bundleSaveRequest));
        mVMConsultantsSaveRequest.pojoSaveRequests = mPojoSaveRequest;
        binding.setVmConsultantSaveRequest(mVMConsultantsSaveRequest);
        observerEvents();
        return binding.getRoot();
    }

    /**
     * Observes events provided by live data single events from login and forgot password view
     * models
     */
    private void observerEvents() {
        mVMConsultantsSaveRequest.getLiveEventSaveRequest().observe(this, areContentsValid -> {
            Utils.hideKeyboard(mMainActivity);
            mVMConsultantsSaveRequest.networkCallData();
        });

        mVMConsultantsSaveRequest.getLiveEventSaveRequestSuccess().observe(this, areContentsValid -> {
            Utils.hideKeyboard(mMainActivity);
            showSavedDialog();
        });
    }

    private void showSavedDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
        alertDialogBuilder.setMessage(getString(R.string.message_appointment_send));
        alertDialogBuilder.setPositiveButton(getString(R.string.message_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                for(int i =1; i< mMainActivity.getSupportFragmentManager().getBackStackEntryCount(); i++)
                    mMainActivity.getSupportFragmentManager().popBackStack();

                Intent intent = new Intent(mApplication.getResources().getString(R
                        .string.broadcastNobiasChangeIndex));
                mApplication.getAppComponent().getLocalBroadcastManager().sendBroadcast(intent);
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setTitle(getString(R.string.title_appointment_send));
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
