package com.lgb.mvp.base;

import java.lang.ref.WeakReference;

/**
 * Created by LGB on 2017/07/01.
 */

public class MvpBasePresenter<V extends MvpView> implements MvpPresenter<V> {

    // this can implements All Kinds of Presenter
    // for ex RxPresenter NoHttpPresenter

    private WeakReference<V> mMvpView;

    @Override
    public void attachView(V mvpView) {
        if(isViewAttached()) return;
        this.mMvpView = new WeakReference<>(mvpView);
    }

    @Override
    public void detachView() {
        if ( null != mMvpView && null != mMvpView.get()){
            mMvpView.clear();
            mMvpView = null;
        }
    }

    /**
     * return view is attached view
     */
    public boolean isViewAttached() {
        return null != mMvpView && mMvpView.get() != null;
    }

    /**
     * check View is attached to the Presenter
     */
    public void checkViewAttached(){
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }

    public V getMvpView() {
        try {
            checkViewAttached();
        } catch (MvpViewNotAttachedException e) {
            e.printStackTrace();
        }
        return null != mMvpView ? mMvpView.get() : null;
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call Presenter.attachView(MvpView) before" +
                    " requesting data to the Presenter");
        }
    }
}
