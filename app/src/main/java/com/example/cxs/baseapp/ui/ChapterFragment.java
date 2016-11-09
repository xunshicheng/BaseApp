package com.example.cxs.baseapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cxs.baseapp.App;
import com.example.cxs.baseapp.R;
import com.example.cxs.baseapp.manager.http.ErrorBean;
import com.example.cxs.baseapp.manager.http.response.DazhuzaiResponse;
import com.example.cxs.baseapp.mvp.component.DaggerFragmentComponent;
import com.example.cxs.baseapp.mvp.interfaces.ChaptersFragmentInterface;
import com.example.cxs.baseapp.mvp.module.FragmentModule;
import com.example.cxs.baseapp.mvp.presenter.ChaptersPresenter;
import com.example.cxs.baseapp.ui.base.BaseFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chengxunshi on 2016/8/11.
 */
public class ChapterFragment extends BaseFragment implements ChaptersFragmentInterface {

    public static final String TAG = "ChapterFragment";
    public static final String CHAPTER_NUM = "chapter_num";
    private SwipeRefreshLayout rootLayout;
    private ChapterAdapter adapter;

    @Inject
    ChaptersPresenter mChaptersPresenter;

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
        rootLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_chapters, container, false);
        ButterKnife.bind(this, rootLayout);
        init();
        return rootLayout;
    }

    private void init() {
        RecyclerView recyclerView = (RecyclerView) rootLayout.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ChapterAdapter();
        adapter.setData(App.getChapters());
        adapter.setItemListener(new ChapterAdapter.RecyclerViewItemListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getActivity(), ChapterReadingFragment.class);
                intent.putExtra(CHAPTER_NUM, position);
                startFragment(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        rootLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "====onRefresh======");
                mChaptersPresenter.getChapterList();
            }
        });
    }

    @OnClick(R.id.fab_reverse)
    void reverseChapterList() {
        Log.i(TAG, "------reverseChapterList------");
        List chapters = App.getChapters();
        Collections.reverse(chapters);
        adapter.setData(chapters);
    }


    @Subscribe(threadMode= ThreadMode.MAIN)
    public void onEvent(ErrorBean errorBean){
        dismissDialogIfExist(null);
        if (rootLayout.isRefreshing()) {
            rootLayout.setRefreshing(false);
        }
        Toast.makeText(getActivity(), errorBean.msg, Toast.LENGTH_LONG).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DazhuzaiResponse response) {
        Log.i(TAG, "--------DazhuzaiResponse--------");
        if (rootLayout.isRefreshing()) {
            rootLayout.setRefreshing(false);
        }
        adapter.setData(response.mixToc.chapters);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mChaptersPresenter.detachView();
    }

    @Override
    public void refreshUI() {

    }

}
