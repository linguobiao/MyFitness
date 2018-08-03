package com.lgb.mvp.base;

import android.support.annotation.UiThread;

/**
 * Created by LGB on 2017/07/01.
 */

public interface BaseView extends MvpView {

    @UiThread
    void showLoading();

    @UiThread
    void dismissLoading();

    @UiThread
    void showEmpty();

    @UiThread
    void showNetError(Throwable e);

    @UiThread
    void finishRefresh();
}
