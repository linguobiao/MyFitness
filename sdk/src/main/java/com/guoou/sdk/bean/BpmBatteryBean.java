package com.guoou.sdk.bean;

public class BpmBatteryBean {
    private int batteryPercent;

    public BpmBatteryBean(int batteryPercent) {
        this.batteryPercent = batteryPercent;
    }

    public int getBatteryPercent() {
        return batteryPercent;
    }

    public void setBatteryPercent(int batteryPercent) {
        this.batteryPercent = batteryPercent;
    }
}
