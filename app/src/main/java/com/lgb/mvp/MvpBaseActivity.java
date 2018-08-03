package com.lgb.mvp;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by LW on 2017/3/31.
 *
 * MVP 模式
 * ButterKnife 绑定
 *
 */

public abstract class MvpBaseActivity<T extends Presenter> extends SupperAppActivity {

    private Unbinder mUnBinder;

    protected T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mPresenter = initPresenter();
        mUnBinder = ButterKnife.bind(this);
        initBaseView();
        // orther init view
        initView();
    }

    // init presenter
    public abstract T initPresenter();

    // init layoutId
    @LayoutRes
    public abstract int getLayoutId();

    private void initBaseView(){
        // do after more things
    }

    // initBaseView after initView
    public abstract void initView();

    @Override
    protected void onDestroy() {
        if(null != mPresenter) mPresenter.detachView();
        if(null != mUnBinder) mUnBinder.unbind();
        super.onDestroy();
    }
}
