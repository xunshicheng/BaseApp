package com.example.cxs.baseapp;

import android.app.Application;
import android.content.Context;

import com.example.cxs.baseapp.manager.http.response.DazhuzaiResponse;
import com.example.cxs.baseapp.mvp.component.AppComponent;
import com.example.cxs.baseapp.mvp.component.DaggerAppComponent;
import com.example.cxs.baseapp.mvp.module.AppModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chengxunshi on 16/9/14.
 */
public class App extends Application {

    private static List<DazhuzaiResponse.MixToc.ChapterIntro> chapters = new ArrayList<>();

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext(){
        return mContext;
    }

    public static List<DazhuzaiResponse.MixToc.ChapterIntro> getChapters() {
        return chapters;
    }

    public static void saveChapters(List<DazhuzaiResponse.MixToc.ChapterIntro> chapterList) {
        if(null != chapterList && chapterList.size() > 0){
            chapters.clear();
            chapters.addAll(chapterList);
        }
    }

    public static AppComponent getAppComponent() {
        return DaggerAppComponent.builder().appModule(new AppModule()).build();
    }
}
