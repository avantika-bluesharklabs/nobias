package com.nobias.businesslogic.interfaces;

import android.view.View;

import com.nobias.businesslogic.pojo.PojoUser;

/**
 * Created by Smit Shah on Aug 21 2018, 6:43 PM
 */
public interface OnClickConsultants {
    void onClickConsultantInfo(View view, int layoutPosition, PojoUser data);
    void onClickConsultant(View view, int layoutPosition, PojoUser data);
}
