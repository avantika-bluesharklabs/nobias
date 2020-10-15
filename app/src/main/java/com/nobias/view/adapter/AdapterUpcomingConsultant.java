package com.nobias.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;
import com.nobias.MyApplication;
import com.nobias.R;
import com.nobias.businesslogic.interfaces.OnClickUpcomingConsultant;
import com.nobias.businesslogic.pojo.PojoPastConsultants;
import com.nobias.databinding.FragmentUpcomingConsultantRowBinding;

import java.util.List;

/**
 * Created by Smit Shah on Aug 21 2018, 6:52 PM
 * <p>
 * Adapter for who is in list
 */
public class AdapterUpcomingConsultant extends RecyclerView.Adapter {
    private Context mContext;
    private List<PojoPastConsultants> mArrayContent;
    private int mViewTypeItem = 1;
    private OnClickUpcomingConsultant mOnClickConsultantListener;

    public AdapterUpcomingConsultant(Context context, List<PojoPastConsultants> arrayContent,
                                     OnClickUpcomingConsultant onClickConsultantListener) {
        mContext = context;
        mArrayContent = arrayContent;
        mOnClickConsultantListener = onClickConsultantListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if (viewType == mViewTypeItem) {
           FragmentUpcomingConsultantRowBinding binding = DataBindingUtil.inflate
                    (layoutInflater, R.layout.fragment_upcoming_consultant_row, parent, false);
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

    /**
     * Notify item removed
     *
     * @param position Position at which item is to be removed
     */
    public void removeItem(int position) {
        mArrayContent.remove(position);
        notifyItemRemoved(position);
    }

    private class Viewholder extends RecyclerView.ViewHolder {
        private FragmentUpcomingConsultantRowBinding mBinding;

        Viewholder(FragmentUpcomingConsultantRowBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void bind(PojoPastConsultants data, int position) {
            mBinding.swipeNotification.setShowMode(SwipeLayout.ShowMode.PullOut);
            mBinding.swipeNotification.addDrag(
                    SwipeLayout.DragEdge.Right,
                    mBinding.swipeNotification.findViewById(R.id.llSwipe)
            );
            mBinding.setData(data);
            mBinding.setLayoutPosition(position);
            mBinding.setIsConsultant(((MyApplication) mContext.getApplicationContext()).mIsConsultant);
            mBinding.executePendingBindings();
        }
    }
}