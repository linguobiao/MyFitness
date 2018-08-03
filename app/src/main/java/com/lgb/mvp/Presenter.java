package com.lgb.mvp;

import com.lgb.mvp.base.MvpBasePresenter;
import com.lgb.mvp.base.MvpView;

import junit.framework.Assert;

/**
 * Created by LW on 2017/3/31.
 *
 * MVP P level
 */

public class Presenter<V extends MvpView> extends MvpBasePresenter<V> {
    // this class can to do more things

    private AppActivity appActivity;

    protected Presenter(AppActivity appActivity, V mvpView){
        this.appActivity = appActivity;
        Assert.assertNotNull(mvpView);
        this.attachView(mvpView);
    }

    public AppActivity getAppActivity(){
        Assert.assertNotNull(appActivity);
        return appActivity;
    }
}