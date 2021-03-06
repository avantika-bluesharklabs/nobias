package com.nobias.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nobias.MyApplication;
import com.nobias.R;
import com.nobias.businesslogic.interfaces.OnClickConsultants;
import com.nobias.businesslogic.pojo.PojoUser;
import com.nobias.databinding.FragmentConsultantsRowBinding;

import java.util.List;

/**
 * Created by Smit Shah on Aug 21 2018, 6:52 PM
 * <p>
 * Adapter for who is in list
 */
public class AdapterConsultants extends RecyclerView.Adapter {
    private Context mContext;
    private List<PojoUser> mArrayContent;
    private int mViewTypeItem = 1;
    private OnClickConsultants mOnClickConsultantListener;
    private boolean mIsConsultant = false;

    public AdapterConsultants(Context context, List<PojoUser> arrayContent,
                              OnClickConsultants onClickConsultantListener) {
        mContext = context;
        mArrayContent = arrayContent;
        mOnClickConsultantListener = onClickConsultantListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if (viewType == mViewTypeItem) {
           FragmentConsultantsRowBinding binding = DataBindingUtil.inflate
                    (layoutInflater, R.layout.fragment_consultants_row, parent, false);
            binding.setRequestOptions(((MyApplication) mContext.getApplicationContext())
                    .glideCenterCircle(R.drawable.placeholder_circle_small));
            binding.setOnContentClickListener(mOnClickConsultantListener);
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

        private FragmentConsultantsRowBinding mBinding;

        Viewholder(FragmentConsultantsRowBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void bind(PojoUser data, int position) {
            mBinding.setData(data);
            mBinding.setLayoutPosition(position);
            mBinding.executePendingBindings();
        }
    }
}