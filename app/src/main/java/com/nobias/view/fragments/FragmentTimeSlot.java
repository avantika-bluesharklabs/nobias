package com.nobias.view.fragments;

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
import com.nobias.businesslogic.viewmodel.fragments.ViewModelTimeSlot;
import com.nobias.databinding.FragmentSetTimeBinding;
import com.nobias.utils.Utils;

/**
 * Created by Smit Shah on Aug 22 2018, 11:38 AM
 * <p>
 * Fragment for attendance log
 */
public class FragmentTimeSlot extends FragmentBase{
    private ViewModelTimeSlot mVMTimeSlot;
    private View mParentView;
    private PojoSaveRequest mPojoSaveRequest;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentSetTimeBinding binding = DataBindingUtil.inflate(inflater, R.layout
                .fragment_set_time, container, false);
        mVMTimeSlot = new ViewModelTimeSlot(mApplication);
        mPojoSaveRequest= (PojoSaveRequest) getArguments().getSerializable(getString(R.string.bundleSaveRequest));
        mVMTimeSlot.mPojoSaveRequest = mPojoSaveRequest;
        binding.setVmTimeSlot(mVMTimeSlot);
        mParentView = binding.getRoot();
        observerEvents();
        mVMTimeSlot.networkCallData();
        return binding.getRoot();
    }

    /**
     * Observes events provided by live data single events from login and forgot password view
     * models
     */
    private void observerEvents() {
        mVMTimeSlot.getLiveEventContinue().observe(this, aVoid -> {
            Utils.hideKeyboard(mMainActivity);
            Fragment fragment = new FragmentStripe();
            mPojoSaveRequest.setSessionTimeSlot(mVMTimeSlot.observerListTimeSlot.get(mVMTimeSlot.observerSessionTopicPosition.get()));
            Bundle bundle = new Bundle();
            bundle.putSerializable(getString(R.string.bundleSaveRequest), mPojoSaveRequest);
            bundle.putBoolean(getString(R.string.bundleIsCheckout), false);
            fragment.setArguments(bundle);
            mMainActivity.addFragment(fragment, R.string.tagFragmentConsultants, R.string.tagFragmentConsultants);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
