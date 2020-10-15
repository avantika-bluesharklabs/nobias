package com.nobias.view.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.nobias.R;
import com.nobias.businesslogic.pojo.PojoCommonResponse;
import com.nobias.businesslogic.pojo.PojoCreateAccount;
import com.nobias.businesslogic.viewmodel.activities.ViewModelProfilePic;
import com.nobias.databinding.ActivityProfilePicBinding;
import com.nobias.utils.Utils;
import com.nobias.utils.UtilsImage;

import java.io.ByteArrayOutputStream;

public class ActivityProfilePic extends ActivityBase {
    private ViewModelProfilePic mViewModelProfilePic;
    private PojoCreateAccount mPojoCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityProfilePicBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_profile_pic);
        mPojoCreateAccount = (PojoCreateAccount)getIntent().getSerializableExtra("personal_info");
        mViewModelProfilePic = new ViewModelProfilePic(mApplication,mPojoCreateAccount);
        mBinding.setVmProfilePic(mViewModelProfilePic);

        observerEvents();
        mBroadcastManager.registerReceiver(mReceiverImageResult, new IntentFilter
                (getResources().getString(R.string.broadcastImageResult)));
    }

    /**
     * Observes events provided by live data single events from login and forgot password view
     * models
     */
    private void observerEvents() {
        mViewModelProfilePic.getLiveEventProfilePic().observe(this, aVoid -> {
            Utils.hideKeyboard(ActivityProfilePic.this);
            imageChooser();
        });

        mViewModelProfilePic.getLiveEventSubmit().observe(this, aVoid -> {
            Utils.hideKeyboard(ActivityProfilePic.this);
            if(!TextUtils.isEmpty( mViewModelProfilePic.observerPicBase64.get()))
                mViewModelProfilePic.networkCallData();
            else
                Toast.makeText(mContext, "Please select profile image.",Toast.LENGTH_SHORT).show();
        });

        mViewModelProfilePic.getLiveEventRegisterSuccess().observe(
                this, this::saveUserData);
    }

    /**
     * Receiver for getting image result
     */
    private final BroadcastReceiver mReceiverImageResult = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            byte[] byteArray = intent.getByteArrayExtra(mContext.getResources().getString(R.string
                    .bundleImageResultStream));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(byteArray.length);
            outputStream.write(byteArray, 0, byteArray.length);
            mViewModelProfilePic.observerStreamPic.set(outputStream);
            mViewModelProfilePic.observerPicBase64.set(UtilsImage.convertImageToBase64(byteArray));
            mViewModelProfilePic.observerPicUri.set(intent.getStringExtra
                    (mContext.getResources().getString(R.string.bundleImageResultUri)));
        }
    };

    private void saveUserData(PojoCommonResponse data) {
        mApplication.saveUserData(data,false);
        if(!TextUtils.isEmpty(data.getMessage()))
            showSuccessDialog(data.getMessage());
        else
            showSuccessDialog("User has been successfully created.");
    }

    private void showSuccessDialog(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityProfilePic.this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton(getString(R.string.message_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                startActivity(new Intent(ActivityProfilePic.this, ActivityMain.class));
                overridePendingTransition(0, 0);
                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setTitle(getString(R.string.title_user_created));
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
