package com.example.cxs.baseapp.mvp.module;

import com.example.cxs.baseapp.http.RetrofitHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by chengxunshi on 16/10/14.
 */
@Module
public class AppModule {

    public AppModule(){

    }

    @Provides
    RetrofitHelper provideRetrofitHelper() {
        return RetrofitHelper.getInstance();
    }
}
