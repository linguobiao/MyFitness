package com.lgb.mvp.base;

import android.support.annotation.UiThread;

/**
 * Created by LGB on 2017/07/01.
 */

public interface MvpBaseView<M> extends BaseView {

    @UiThread
    void showContent(M result);

}
