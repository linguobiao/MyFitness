package com.guoou.sdk.bean;

import java.util.Calendar;

public class BpmResultBean {

    private Calendar datetime;
    private int systolic;
    private int diatolic;
    private int heartRate;
    private int error;
    private boolean isSuccess;

    public BpmResultBean(boolean isSuccess) {
        this.isSuccess = isSuccess;
        datetime = Calendar.getInstance();
    }

    public int getSystolic() {
        return systolic;
    }
    public void setSystolic(int systolic) {
        this.systolic = systolic;
    }
    public int getDiatolic() {
        return diatolic;
    }
    public void setDiatolic(int diatolic) {
        this.diatolic = diatolic;
    }
    public int getHeartRate() {
        return heartRate;
    }
    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public Calendar getDatetime() {
        return datetime;
    }
    public void setDatetime(Calendar datetime) {
        this.datetime = datetime;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
