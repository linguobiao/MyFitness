package com.lgb.myfitness.global;

import android.app.Application;

import com.guoou.sdk.global.SdkManager;

public class LgbApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        SdkManager.getInstance().init(this);
    }
}
