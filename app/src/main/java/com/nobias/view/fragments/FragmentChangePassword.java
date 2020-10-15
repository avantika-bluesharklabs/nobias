package com.nobias.view.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.nobias.R;
import com.nobias.businesslogic.viewmodel.fragments.ViewModelChangePassword;
import com.nobias.databinding.FragmentChangePasswordBinding;
import com.nobias.utils.Utils;

/**
 * Created by Smit Shah on Aug 22 2018, 11:38 AM
 * <p>
 * Fragment for attendance log
 */
public class FragmentChangePassword extends FragmentBase{
    private ViewModelChangePassword mVMChangePassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentChangePasswordBinding binding = DataBindingUtil.inflate(inflater, R.layout
                .fragment_change_password, container, false);
        mVMChangePassword = new ViewModelChangePassword(mApplication);
        binding.setVmChangePassword(mVMChangePassword);
        observerEvents();
        return binding.getRoot();
    }

    /**
     * Observes events provided by live data single events from login and forgot password view
     * models
     */
    private void observerEvents() {
        mVMChangePassword.getLiveEventSave().observe(this, aVoid -> {
            Utils.hideKeyboard(mMainActivity);
            showSavedDialog(aVoid);
        });
    }

    private void showSavedDialog(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton(getString(R.string.message_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                mMainActivity.onBackPressed();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setTitle(getString(R.string.title_change_password_saved));
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
