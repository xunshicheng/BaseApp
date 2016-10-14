package com.example.cxs.baseapp.mvp.base;

/**
 * Created by chengxunshi on 16/10/12.
 */
public interface IBasePresenter<T extends IBaseView> {

    public void attachView(T view);

    public void detachView();
}
