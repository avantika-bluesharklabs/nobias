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
import androidx.fragment.app.Fragment;

import com.nobias.R;
import com.nobias.businesslogic.interfaces.OnClickConsultants;
import com.nobias.businesslogic.pojo.PojoSaveRequest;
import com.nobias.businesslogic.pojo.PojoUser;
import com.nobias.businesslogic.viewmodel.fragments.ViewModelConsultants;
import com.nobias.databinding.FragmentConsultantsBinding;
import com.nobias.utils.Utils;

/**
 * Created by Smit Shah on Aug 22 2018, 11:38 AM
 * <p>
 * Fragment for attendance log
 */
public class FragmentConsultants extends FragmentBase implements OnClickConsultants {
    private ViewModelConsultants mVMConsultants;
    private PojoSaveRequest mPojoSaveRequest = new PojoSaveRequest();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentConsultantsBinding binding = DataBindingUtil.inflate(inflater, R.layout
                .fragment_consultants, container, false);
        mVMConsultants = new ViewModelConsultants(mApplication);
        mPojoSaveRequest = (PojoSaveRequest) getArguments().getSerializable(getString(R.string.bundleSaveRequest));
        mVMConsultants.pojoSaveRequest = mPojoSaveRequest;
        binding.setVmConsultants(mVMConsultants);
        binding.setOnContentClickListener(this);
        mVMConsultants.fetchDataList();
        observerEvents();
        return binding.getRoot();
    }

    /**
     * Observes events provided by live data single events from login and forgot password view
     * models
     */
    private void observerEvents() {
        mVMConsultants.getLiveEvent().observe(this, areContentsValid -> {
            Utils.hideKeyboard(mMainActivity);
            showNoConsultantsDialog();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClickConsultantInfo(View view, int layoutPosition, PojoUser data) {
        Fragment fragment = new FragmentConsultantInfo();
        Bundle bundle = new Bundle();
        bundle.putString(mContext.getResources().getString(R.string.bundleConsultantId), String.valueOf(data.getId()));
        fragment.setArguments(bundle);
        mMainActivity.addFragment(fragment, R.string.tagFragmentConsultantInfo, R.string.tagFragmentConsultantInfo);
    }

    @Override
    public void onClickConsultant(View view, int layoutPosition, PojoUser data) {
        Fragment fragment = new FragmentTimeSlot();
        Bundle bundle = new Bundle();
        mPojoSaveRequest.setConsultantId(String.valueOf(data.getId()));
        bundle.putSerializable(mContext.getResources().getString(R.string.bundleSaveRequest), mPojoSaveRequest);
        fragment.setArguments(bundle);
        mMainActivity.addFragment(fragment, R.string.tagFragmentTimeSlot, R.string.tagFragmentTimeSlot);
    }

    private void showNoConsultantsDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
        alertDialogBuilder.setMessage(getString(R.string.message_consultant_unavailable));
        alertDialogBuilder.setPositiveButton(getString(R.string.message_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                mMainActivity.onBackPressed();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setTitle(getString(R.string.title_consultant_unavailable));
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
}
