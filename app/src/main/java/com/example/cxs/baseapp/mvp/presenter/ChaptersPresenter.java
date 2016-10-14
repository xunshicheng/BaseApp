package com.example.cxs.baseapp.mvp.presenter;

import com.example.cxs.baseapp.App;
import com.example.cxs.baseapp.manager.http.HttpManager;
import com.example.cxs.baseapp.manager.http.ResponseBean;
import com.example.cxs.baseapp.manager.http.response.DazhuzaiChapterResp;
import com.example.cxs.baseapp.manager.http.response.DazhuzaiResponse;
import com.example.cxs.baseapp.mvp.base.BasePresenter;
import com.example.cxs.baseapp.ui.ChapterFragment;
import com.google.gson.reflect.TypeToken;

import javax.inject.Inject;

/**
 * Created by chengxunshi on 16/10/12.
 */
public class ChaptersPresenter extends BasePresenter<ChapterFragment> {

    @Inject
    public ChaptersPresenter() {

    }

    public void loadChapters(DazhuzaiResponse.Chapter chapter){
        if(null == chapter){
            return;
        }
        int start = chapter.url.lastIndexOf("/");
        String subUrl = chapter.url.substring(start, chapter.url.length());
        String url = App.BaseUrl + subUrl;
        HttpManager.sendRequest(url,
                new TypeToken<ResponseBean<DazhuzaiChapterResp>>() {}.getType());
    }

}
