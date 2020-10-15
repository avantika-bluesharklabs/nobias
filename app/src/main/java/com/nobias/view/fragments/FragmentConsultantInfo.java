package com.nobias.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.nobias.R;
import com.nobias.businesslogic.viewmodel.fragments.ViewModelConsultantInfo;
import com.nobias.databinding.FragmentConsultantInfoBinding;

/**
 * Created by Smit Shah on Aug 22 2018, 11:38 AM
 * <p>
 * Fragment for attendance log
 */
public class FragmentConsultantInfo extends FragmentBase {
    private ViewModelConsultantInfo mVMConsultantsInfo;
    private String mConsultantId="";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentConsultantInfoBinding binding = DataBindingUtil.inflate(inflater, R.layout
                .fragment_consultant_info, container, false);
        mVMConsultantsInfo = new ViewModelConsultantInfo(mApplication);
        mConsultantId = getArguments().getString(mContext.getResources().getString(R.string
                .bundleConsultantId));
        mVMConsultantsInfo.observerConsultantId.set(mConsultantId);
        binding.setVmConsultantInfo(mVMConsultantsInfo);
        mVMConsultantsInfo.networkCallData();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
