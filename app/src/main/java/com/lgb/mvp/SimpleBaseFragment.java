package com.lgb.mvp;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lgb.myfitness.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by LW on 2017/3/31.
 *
 * no MVP base fragment
 */

public abstract class SimpleBaseFragment extends SuperAppFragment {

    private Unbinder mUnBinder;
    //cache Fragment view
    private View mRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(getLayoutId(), container, false);
            mUnBinder = ButterKnife.bind(this, mRootView);
            initView();
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(null != mUnBinder) mUnBinder.unbind();
    }

    public abstract void initView();

    @LayoutRes
    protected abstract int getLayoutId();

}
