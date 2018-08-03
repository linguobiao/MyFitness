package com.lgb.mvp;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by LW on 2017/4/13.
 *
 * no MVP base Activity
 */

public abstract class SimpleBaseActivity extends SupperAppActivity {

    private Unbinder mUnBinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mUnBinder = ButterKnife.bind(this);
        initBaseView();
        // orther init view
        initView();
    }

    // init layoutId
    @LayoutRes
    public abstract int getLayoutId();

    // initBaseView after initView
    public abstract void initView();

    private void initBaseView(){
        // do after more things
    }

    @Override
    protected void onDestroy() {
        if(null != mUnBinder) mUnBinder.unbind();
        super.onDestroy();
    }

}
