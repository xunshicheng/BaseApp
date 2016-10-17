package com.example.cxs.baseapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cxs.baseapp.App;
import com.example.cxs.baseapp.R;
import com.example.cxs.baseapp.http.RetrofitHelper;
import com.example.cxs.baseapp.manager.http.ErrorBean;
import com.example.cxs.baseapp.manager.http.ResponseBean;
import com.example.cxs.baseapp.manager.http.response.DazhuzaiResponse;
import com.example.cxs.baseapp.mvp.component.DaggerFragmentComponent;
import com.example.cxs.baseapp.mvp.module.FragmentModule;
import com.example.cxs.baseapp.ui.base.BaseFragment;
import com.example.cxs.baseapp.util.UIUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Collections;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by chengxunshi on 2016/8/11.
 */
public class NovelFragment extends BaseFragment {

    public static final String TAG = "NovelFragment";

    @Inject
    RetrofitHelper retrofitHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerFragmentComponent.builder()
                .appComponent(App.getAppComponent())
                .fragmentModule(new FragmentModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_novels, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick(R.id.btn_novel_dazhuzai)
    public void getChapterList(){
        UIUtil.showProgressDialog(getFragmentManager(), null, true);
//        HttpManager.sendRequest(App.BaseUrl,
//                new TypeToken<ResponseBean<DazhuzaiResponse>>() {}.getType());

        retrofitHelper.getChapterList()
                .subscribeOn(Schedulers.io())
                .doOnNext(new Action1<ResponseBean<DazhuzaiResponse>>() {
                    @Override
                    public void call(ResponseBean<DazhuzaiResponse> response) {
                        Log.i(TAG, "====doOnNext======");
                        Collections.reverse(response.data.chapterList);
                        App.saveChapters(response.data.chapterList);
                    }
                })
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        Log.i(TAG, "====subscribe======");
                        dismissDialogIfExist(null);
                        Intent intent = new Intent(getActivity(), ChapterFragment.class);
                        startFragment(intent);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });

    }

    @Subscribe(threadMode=ThreadMode.MAIN)
    public void onEvent(ErrorBean errorBean){
        dismissDialogIfExist(null);
        Toast.makeText(getActivity(), errorBean.msg, Toast.LENGTH_LONG).show();
    }

    @Subscribe
    public void onEvent(DazhuzaiResponse response){
        Log.i(TAG, "--------DazhuzaiResponse--------");
        dismissDialogIfExist(null);
        Collections.reverse(response.chapterList);
        App.saveChapters(response.chapterList);
        Intent intent = new Intent(getActivity(), ChapterFragment.class);
        startFragment(intent);
    }

}
