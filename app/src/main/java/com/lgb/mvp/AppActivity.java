package com.lgb.mvp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * 基类Activity 业务封装
 * Created by LGB on 2017/07/01.
 */
public abstract class AppActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}