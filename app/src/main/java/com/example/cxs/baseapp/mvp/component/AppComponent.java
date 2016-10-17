package com.example.cxs.baseapp.mvp.component;

import com.example.cxs.baseapp.http.RetrofitHelper;
import com.example.cxs.baseapp.mvp.module.AppModule;

import dagger.Component;

/**
 * Created by chengxunshi on 16/10/14.
 */

@Component(modules = AppModule.class)
public interface AppComponent {
    RetrofitHelper retrofitHelper();
}
