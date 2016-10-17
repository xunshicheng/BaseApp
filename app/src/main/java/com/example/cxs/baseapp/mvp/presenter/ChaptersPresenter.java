package com.example.cxs.baseapp.mvp.presenter;

import com.example.cxs.baseapp.http.RetrofitHelper;
import com.example.cxs.baseapp.manager.EventManager;
import com.example.cxs.baseapp.manager.http.ResponseBean;
import com.example.cxs.baseapp.manager.http.response.DazhuzaiChapterResp;
import com.example.cxs.baseapp.manager.http.response.DazhuzaiResponse;
import com.example.cxs.baseapp.mvp.base.BasePresenter;
import com.example.cxs.baseapp.ui.ChapterFragment;

import javax.inject.Inject;

import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by chengxunshi on 16/10/12.
 */
public class ChaptersPresenter extends BasePresenter<ChapterFragment> {

    @Inject
    RetrofitHelper retrofitHelper;

    @Inject
    public ChaptersPresenter() {

    }

    public void loadChapters(DazhuzaiResponse.Chapter chapter){
        if(null == chapter){
            return;
        }
        int start = chapter.url.lastIndexOf("/");
        String subUrl = chapter.url.substring(start + 1, chapter.url.length());
//        String url = App.BaseUrl + subUrl;
//        HttpManager.sendRequest(url,
//                new TypeToken<ResponseBean<DazhuzaiChapterResp>>() {}.getType());

        retrofitHelper.getChapter(subUrl)
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Action1<ResponseBean<DazhuzaiChapterResp>>() {
                            @Override
                            public void call(ResponseBean<DazhuzaiChapterResp> dazhuzaiChapterRespResponseBean) {
                                EventManager.post(dazhuzaiChapterRespResponseBean.data);
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        });
    }

}
