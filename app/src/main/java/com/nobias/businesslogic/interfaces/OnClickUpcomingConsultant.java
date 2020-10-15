package com.nobias.businesslogic.interfaces;

import android.view.View;

import com.nobias.businesslogic.pojo.PojoPastConsultants;

/**
 * Created by Smit Shah on Aug 21 2018, 6:43 PM
 */
public interface OnClickUpcomingConsultant {
    void onClickConsultantStatus(View view, int layoutPosition, PojoPastConsultants data);
    void onClickConsultantVideo(View view, int layoutPosition, PojoPastConsultants data);
    void onClickConsultantReminder(View view, int layoutPosition, PojoPastConsultants data);
    void onClickDeleteAppointment(View view, int layoutPosition, PojoPastConsultants data);
    void onClickReschedule(View view, int layoutPosition, PojoPastConsultants data);
}
