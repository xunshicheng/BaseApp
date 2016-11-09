package com.example.cxs.baseapp.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.cxs.baseapp.App;
import com.example.cxs.baseapp.R;
import com.example.cxs.baseapp.common.Constant;
import com.example.cxs.baseapp.manager.http.response.DazhuzaiChapterResp;
import com.example.cxs.baseapp.manager.http.response.DazhuzaiResponse;
import com.example.cxs.baseapp.mvp.component.DaggerFragmentComponent;
import com.example.cxs.baseapp.mvp.module.FragmentModule;
import com.example.cxs.baseapp.mvp.presenter.ChaptersPresenter;
import com.example.cxs.baseapp.ui.base.BaseFragment;
import com.example.cxs.baseapp.ui.widget.readerview.BaseReadView;
import com.example.cxs.baseapp.ui.widget.readerview.OnReadStateChangeListener;
import com.example.cxs.baseapp.ui.widget.readerview.PageWidget;
import com.example.cxs.baseapp.util.UIUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

/**
 * Created by chengxunshi on 2016/8/11.
 */
public class ChapterReadingFragment extends BaseFragment {

    private int chapterNum;
    private PageWidget pageWidget;
    private Receiver receiver = new Receiver();
    private IntentFilter intentFilter = new IntentFilter();

    @Inject
    ChaptersPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerFragmentComponent.builder()
                .appComponent(App.getAppComponent())
                .fragmentModule(new FragmentModule(this))
                .build()
                .inject(this);
        if (getArguments() != null) {
            chapterNum = getArguments().getInt(ChapterFragment.CHAPTER_NUM, 0);
        }
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chapter_reading, container, false);
        init(view);
        loadChapter();
        return view;
    }

    private void init(View view){
        FrameLayout flReadWidget = (FrameLayout) view.findViewById(R.id.flReadWidget);
        pageWidget = new PageWidget(getActivity(), "", new OnReadStateChangeListener() {
            @Override
            public void onChapterChanged(int chapter) {
            }

            @Override
            public void onPageChanged(int chapter, int page) {
            }

            @Override
            public void onLoadChapterFailure(int chapter) {
            }

            @Override
            public void onCenterClick() {
            }

            @Override
            public void onFlip() {
            }
        });
        pageWidget.setTextColor(ContextCompat.getColor(getActivity(), R.color.chapter_content_night),
                ContextCompat.getColor(getActivity(), R.color.chapter_title_night));
        pageWidget.setLoadChapterListener(new BaseReadView.LoadChapterListener() {
            @Override
            public void loadPreChapter() {
                chapterNum--;
                loadChapter();
            }

            @Override
            public void loadNextChapter() {
                chapterNum++;
                loadChapter();
            }
        });
        flReadWidget.addView(pageWidget);
    }

    private void loadChapter() {
        UIUtil.showProgressDialog(getFragmentManager(), null, true);
        DazhuzaiResponse.MixToc.ChapterIntro chapter = App.getChapters().get(chapterNum);
        mPresenter.loadChapters(chapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DazhuzaiChapterResp response) {
        Log.i("cxs", "--------DazhuzaiChapterResp--------");
        dismissDialogIfExist(null);
        pageWidget.refreshContent(response.chapter.body, chapterNum);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }

    public class Receiver extends BroadcastReceiver {

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("TAG", "=====onReceive=====");
            if (pageWidget != null) {
                if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
                    int level = intent.getIntExtra("level", 0);
                    pageWidget.setBattery(100 - level);
                } else if (Intent.ACTION_TIME_TICK.equals(intent.getAction())) {
                    pageWidget.setTime(sdf.format(new Date()));
                }
            }
        }
    }
}
