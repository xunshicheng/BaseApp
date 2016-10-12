package com.example.cxs.baseapp;

import android.app.Application;
import android.content.Context;

import com.example.cxs.baseapp.manager.http.response.DazhuzaiResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chengxunshi on 16/9/14.
 */
public class App extends Application {

    public final static String BaseUrl = "http://45.78.39.97:8080/dazhuzai";

    private static List<DazhuzaiResponse.Chapter> chapters = new ArrayList<>();

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext(){
        return mContext;
    }

    public static List getChapters(){
        return chapters;
    }

    public static void saveChapters(List<DazhuzaiResponse.Chapter> chapterList) {
        if(null != chapterList && chapterList.size() > 0){
            chapters.clear();
            chapters.addAll(chapterList);
        }
    }
}
