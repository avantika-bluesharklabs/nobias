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

import com.nobias.BR;
import com.nobias.R;
import com.nobias.businesslogic.interfaces.OnClickSpecialities;
import com.nobias.businesslogic.pojo.PojoSpeciality;
import com.nobias.businesslogic.viewmodel.fragments.ViewModelSetSpecialities;
import com.nobias.databinding.FragmentSetSpecialityBinding;
import com.nobias.utils.Utils;

/**
 * Created by Smit Shah on Aug 22 2018, 11:38 AM
 * <p>
 * Fragment for attendance log
 */
public class FragmentSetSpecialities extends FragmentBase implements OnClickSpecialities {
    private ViewModelSetSpecialities mVMSetSpecialities;
    private View mParentView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentSetSpecialityBinding binding = DataBindingUtil.inflate(inflater, R.layout
                .fragment_set_speciality, container, false);
        mVMSetSpecialities = new ViewModelSetSpecialities(mApplication);
        binding.setVmSetSpeciality(mVMSetSpecialities);
        binding.setOnContentClickListener(this);
        mParentView = binding.getRoot();
        observerEvents();
        return binding.getRoot();
    }

    /**
     * Observes events provided by live data single events from login and forgot password view
     * models
     */
    /**
     * Observes events provided by live data single events from login and forgot password view
     * models
     */
    private void observerEvents() {
        mVMSetSpecialities.getSingleLiveEventSave().observe(this, message -> {
            Utils.hideKeyboard(mMainActivity);
            showSavedDialog(message);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
        alertDialog.setTitle(getString(R.string.title_specialities_saved));
        alertDialog.setCancelable(false);
        alertDialog.show();
    }


    @Override
    public void onClickSpeciality(View view, int layoutPosition, PojoSpeciality data) {
        if(mVMSetSpecialities.observerContent.get(layoutPosition).getSelected()) {
            mVMSetSpecialities.observerContent.get(layoutPosition).setSelected(false);
            mVMSetSpecialities.observerContent.get(layoutPosition).notifyPropertyChanged(BR.selected);
        }
        else {
            mVMSetSpecialities.observerContent.get(layoutPosition).setSelected(true);
            mVMSetSpecialities.observerContent.get(layoutPosition).notifyPropertyChanged(BR.selected);
        }
    }
}
