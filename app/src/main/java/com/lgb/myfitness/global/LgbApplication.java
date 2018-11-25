package com.lgb.myfitness.global;

import android.app.Application;

import com.guoou.sdk.global.SdkManager;
import com.lgb.myfitness.helper.ContextHelper;
import com.pgyersdk.crash.PgyCrashManager;

public class LgbApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        SdkManager.getInstance().init(this);
        PgyCrashManager.register(); //推荐使用
        ContextHelper.getInstance().init(this);

    }
}
