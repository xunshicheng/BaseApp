package com.example.cxs.baseapp.mvp.presenter;

import com.example.cxs.baseapp.App;
import com.example.cxs.baseapp.http.RetrofitHelper;
import com.example.cxs.baseapp.manager.EventManager;
import com.example.cxs.baseapp.manager.http.ErrorBean;
import com.example.cxs.baseapp.manager.http.ResponseBean;
import com.example.cxs.baseapp.manager.http.response.DazhuzaiChapterResp;
import com.example.cxs.baseapp.manager.http.response.DazhuzaiResponse;
import com.example.cxs.baseapp.mvp.base.BasePresenter;
import com.example.cxs.baseapp.ui.ChapterFragment;

import java.util.Collections;

import javax.inject.Inject;

import rx.functions.Action1;
import rx.functions.Func1;
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
        final String subUrl = chapter.url.substring(start + 1, chapter.url.length());

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
                        EventManager.post(new ErrorBean("getChapter", -1, "请求失败!"));
                    }
                });
    }

    public void refreshChapList() {
        retrofitHelper.getChapterList()
                .subscribeOn(Schedulers.io())
                .filter(new Func1<ResponseBean<DazhuzaiResponse>, Boolean>() {
                    @Override
                    public Boolean call(ResponseBean<DazhuzaiResponse> response) {
                        return response.data.chapterList.size() > 0;
                    }
                })
                .doOnNext(new Action1<ResponseBean<DazhuzaiResponse>>() {
                    @Override
                    public void call(ResponseBean<DazhuzaiResponse> response) {
                        Collections.reverse(response.data.chapterList);
                        App.saveChapters(response.data.chapterList);
                    }
                })
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ResponseBean<DazhuzaiResponse>>() {
                    @Override
                    public void call(ResponseBean<DazhuzaiResponse> res) {
                        EventManager.post(res.data);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        EventManager.post(new ErrorBean("getChapterList", -1, "请求失败!"));
                    }
                });
    }

}
