package com.example.cxs.baseapp.mvp.base;

/**
 * Created by chengxunshi on 16/10/12.
 */
public class BasePresenter<T extends IBaseView> implements IBasePresenter<T> {

    protected T mView;

    @Override
    public void attachView(T view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }
}
