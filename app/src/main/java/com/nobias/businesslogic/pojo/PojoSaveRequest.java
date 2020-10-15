package com.nobias.businesslogic.pojo;

import java.io.Serializable;

public class PojoSaveRequest implements Serializable {
    private String sessionDate;
    private String sessionCategory;
    private String sessionName;
    private String sessionTimeSlot;
    private String needType;
    private String consultantId;

    public String getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(String sessionDate) {
        this.sessionDate = sessionDate;
    }

    public String getSessionCategory() {
        return sessionCategory;
    }

    public void setSessionCategory(String sessionCategory) {
        this.sessionCategory = sessionCategory;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getSessionTimeSlot() {
        return sessionTimeSlot;
    }

    public void setSessionTimeSlot(String sessionTimeSlot) {
        this.sessionTimeSlot = sessionTimeSlot;
    }

    public String getNeedType() {
        return needType;
    }

    public void setNeedType(String needType) {
        this.needType = needType;
    }

    public String getConsultantId() {
        return consultantId;
    }

    public void setConsultantId(String consultantId) {
        this.consultantId = consultantId;
    }
}
