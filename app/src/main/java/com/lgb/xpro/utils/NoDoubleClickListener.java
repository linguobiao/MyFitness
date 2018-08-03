package com.lgb.xpro.utils;

import android.view.View;

import java.util.Calendar;

/**
 * 点击事件监听， 防止网络不好、手机卡顿、点击过快等产生重复事件
 * Created by LGB on 17/3/1.
 */

public abstract class NoDoubleClickListener implements View.OnClickListener {
    public static final int MIN_CLICK_DELAY_TIME = 500;

    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        long lastClickTime = (null != v.getTag()) ? (long) v.getTag() : 0;
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            v.setTag(currentTime);
            onNoDoubleClick(v);
        }
    }

    protected abstract void onNoDoubleClick(View v);

}

