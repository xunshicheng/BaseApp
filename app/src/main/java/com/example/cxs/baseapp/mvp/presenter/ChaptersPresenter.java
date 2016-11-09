package com.example.cxs.baseapp.mvp.presenter;

import com.example.cxs.baseapp.App;
import com.example.cxs.baseapp.http.RetrofitHelper;
import com.example.cxs.baseapp.manager.EventManager;
import com.example.cxs.baseapp.manager.http.ErrorBean;
import com.example.cxs.baseapp.manager.http.response.DazhuzaiChapterResp;
import com.example.cxs.baseapp.manager.http.response.DazhuzaiResponse;
import com.example.cxs.baseapp.mvp.base.BasePresenter;
import com.example.cxs.baseapp.ui.base.BaseFragment;

import java.util.Collections;

import javax.inject.Inject;

import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by chengxunshi on 16/10/12.
 */
public class ChaptersPresenter extends BasePresenter<BaseFragment> {

    @Inject
    RetrofitHelper retrofitHelper;

    @Inject
    public ChaptersPresenter() {

    }

    public void loadChapters(DazhuzaiResponse.MixToc.ChapterIntro chapterIntro) {
        if (null == chapterIntro) {
            return;
        }
        retrofitHelper.getChapter(chapterIntro.link)
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<DazhuzaiChapterResp>() {
                    @Override
                    public void call(DazhuzaiChapterResp dazhuzaiChapterRespResponseBean) {
                        EventManager.post(dazhuzaiChapterRespResponseBean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        EventManager.post(new ErrorBean("getChapter", -1, "请求失败!"));
                    }
                });
    }

    public void getChapterList() {
        retrofitHelper.getChapterList()
                .subscribeOn(Schedulers.io())
                .filter(new Func1<DazhuzaiResponse, Boolean>() {
                    @Override
                    public Boolean call(DazhuzaiResponse response) {
                        return response.mixToc.chapters.size() > 0;
                    }
                })
                .doOnNext(new Action1<DazhuzaiResponse>() {
                    @Override
                    public void call(DazhuzaiResponse response) {
                        Collections.reverse(response.mixToc.chapters);
                        App.saveChapters(response.mixToc.chapters);
                    }
                })
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<DazhuzaiResponse>() {
                    @Override
                    public void call(DazhuzaiResponse res) {
                        EventManager.post(res);
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
