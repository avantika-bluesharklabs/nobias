package com.nobias.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nobias.R;
import com.nobias.businesslogic.interfaces.OnClickSpecialities;
import com.nobias.businesslogic.pojo.PojoSpeciality;
import com.nobias.databinding.FragmentSetSpecialityRowBinding;

import java.util.List;

/**
 * Created by Smit Shah on Aug 21 2018, 6:52 PM
 * <p>
 * Adapter for who is in list
 */
public class AdapterSetSpecialities extends RecyclerView.Adapter {
    private Context mContext;
    private List<PojoSpeciality> mArrayContent;
    private int mViewTypeItem = 1;
    private OnClickSpecialities mOnClickSpecialities;

    public AdapterSetSpecialities(Context context, List<PojoSpeciality> arrayContent,
                                  OnClickSpecialities onClickSpecialities) {
        mContext = context;
        mArrayContent = arrayContent;
        mOnClickSpecialities = onClickSpecialities;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if (viewType == mViewTypeItem) {
           FragmentSetSpecialityRowBinding binding = DataBindingUtil.inflate
                    (layoutInflater, R.layout.fragment_set_speciality_row, parent, false);
            binding.setOnContentClickListener(mOnClickSpecialities);
            return new Viewholder(binding);
        } else {
            return new ProgressViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_progress, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof Viewholder) {
            ((Viewholder) holder).bind(mArrayContent.get(position), position);
        }
    }

    @Override
    public int getItemCount() {
        return mArrayContent.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mArrayContent.get(position) != null ? mViewTypeItem : 0;
    }

    private class ProgressViewHolder extends RecyclerView.ViewHolder {
        ProgressViewHolder(View v) {
            super(v);
        }
    }

    private class Viewholder extends RecyclerView.ViewHolder {

        private FragmentSetSpecialityRowBinding mBinding;

        Viewholder(FragmentSetSpecialityRowBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void bind(PojoSpeciality data, int position) {
            mBinding.setData(data);
            mBinding.setLayoutPosition(position);
            mBinding.executePendingBindings();
        }
    }
}