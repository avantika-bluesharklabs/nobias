package com.nobias.view.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.nobias.BR;
import com.nobias.R;
import com.nobias.businesslogic.interfaces.OnClickPastConsultant;
import com.nobias.businesslogic.pojo.PojoPastConsultants;
import com.nobias.businesslogic.viewmodel.fragments.ViewModelPastConsultant;
import com.nobias.databinding.FragmentPastConsultantBinding;

/**
 * Created by Smit Shah on Aug 22 2018, 11:38 AM
 * <p>
 * Fragment for attendance log
 */
public class FragmentPastConsultant extends FragmentBase implements OnClickPastConsultant {

    private ViewModelPastConsultant mVMPastConsultant;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentPastConsultantBinding binding = DataBindingUtil.inflate(inflater, R.layout
                .fragment_past_consultant, container, false);
        mVMPastConsultant = new ViewModelPastConsultant(mApplication);
        binding.setVmPastConsultantList(mVMPastConsultant);
        binding.setOnContentClickListener(this);
        mBroadcastManager.registerReceiver(mReceiverPastConsultants, new IntentFilter
                (getResources().getString(R.string.broadcastPastConsultantRate)));
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        mBroadcastManager.unregisterReceiver(mReceiverPastConsultants);
        super.onDestroyView();
    }

    private final BroadcastReceiver mReceiverPastConsultants = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            int rating = intent.getIntExtra(getString(R.string.bundlePastConsultantRate), 0);
            mVMPastConsultant.observerContent.get(mVMPastConsultant.observerPosition).setRating(String.valueOf(rating));
            mVMPastConsultant.observerContent.get(mVMPastConsultant.observerPosition).notifyPropertyChanged(BR.rating);
        }
    };

    @Override
    public void onClickPastConsultantRating(View view, int layoutPosition, PojoPastConsultants data) {
        mVMPastConsultant.observerPosition = layoutPosition;
        int rating = 0;
        Fragment fragment = new FragmentPastConsultantRating();
        Bundle bundle = new Bundle();
        bundle.putString(mContext.getResources().getString(R.string.bundlePastConsultantApptId), String.valueOf(data.getId()));
        if (!TextUtils.isEmpty(data.getRating()))
            rating = Integer.valueOf(data.getRating());
        bundle.putInt(mContext.getResources().getString(R.string.bundlePastConsultantRate), rating);
        fragment.setArguments(bundle);
        mMainActivity.addFragment(fragment, R.string.tagFragmentPastConsultantRating, R.string.tagFragmentPastConsultantRating);
    }
}
/*
* /*Bundle args = intent.getBundleExtra(getString(R.string.bundle));
                mVMPastConsultant.observerPastConsultantList.clear();
                mVMPastConsultant.observerContent.clear();
                List<PojoPastConsultants> object = (ArrayList<PojoPastConsultants>) args.getSerializable(
                        getString(R.string.bundlePastConsultant));
                mVMPastConsultant.observerPastConsultantList.addAll(object);
                mVMPastConsultant.observerContent.addAll(object);*/

