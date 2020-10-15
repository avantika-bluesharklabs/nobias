package com.nobias.businesslogic.interfaces;

import android.view.View;

import com.nobias.businesslogic.pojo.PojoStripeCardData;

/**
 * Created by Smit Shah on Aug 21 2018, 6:43 PM
 */
public interface OnClickStripeCard {
    void onClickCard(View view, int layoutPosition, PojoStripeCardData data);
}
