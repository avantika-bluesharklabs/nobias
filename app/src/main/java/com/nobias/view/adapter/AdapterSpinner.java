package com.nobias.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.nobias.R;
import com.nobias.utils.Utils;

import java.util.List;

/**
 * Created by Brij Dholakia on Jul, 11 2018 17:37.
 * <p>
 * Adapter for spinners
 */
public class AdapterSpinner extends ArrayAdapter<String> {

    private String[] mDataString;
    private List<String> mDataArray;
    private Context mContext;

    public AdapterSpinner(Context context, int textViewResourceId, String[] arrayData) {
        super(context, textViewResourceId, arrayData);
        mContext = context;
        mDataString = arrayData;
    }

    public AdapterSpinner(Context context, int textViewResourceId, List<String> arrayData) {
        super(context, textViewResourceId, arrayData);
        mContext = context;
        mDataArray = arrayData;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, true);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, false);
    }

    private View getCustomView(int position, boolean isDropDown) {
        TextView textView = new AppCompatTextView(mContext);
        textView.setTextColor(ContextCompat.getColor(mContext, android.R.color.black));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, mContext.getResources().getDimensionPixelOffset(R.dimen
                .margin_sixteen), 0, 0);
        textView.setLayoutParams(layoutParams);

        if(isDropDown) {
            textView.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.white));
        }
        Utils.setSpinnerTextPadding(isDropDown, mContext, textView);
        String content = "";
        if (mDataString != null) {
            content = mDataString[position];
        } else if (mDataArray != null) {
            content = mDataArray.get(position);
        }
        textView.setText(!TextUtils.isEmpty(content) ? content : "");
        return textView;
    }
}
