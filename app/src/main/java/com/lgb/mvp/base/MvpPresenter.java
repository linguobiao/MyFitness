package com.lgb.mvp.base;

import android.support.annotation.UiThread;

/**
 * Created by LGB on 2017/07/01.
 *
 * mvp design presenter
 */

public interface MvpPresenter<V extends MvpView> {

    /**
     * Set or attach the view to this presenter
     */
    @UiThread
    void attachView(V view);

    /**
     * Will be called if the view has been destroyed. Typically this method will be invoked from
     * <code>Activity.detachView()</code> or <code>Fragment.onDestroyView()</code>
     */
    @UiThread
    void detachView();

}
