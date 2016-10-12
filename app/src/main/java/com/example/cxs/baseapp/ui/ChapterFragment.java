package com.example.cxs.baseapp.ui;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.cxs.baseapp.manager.http.HttpManager;
import com.example.cxs.baseapp.manager.http.ResponseBean;
import com.example.cxs.baseapp.manager.http.response.DazhuzaiChapterResp;
import com.example.cxs.baseapp.manager.http.response.DazhuzaiResponse;
import com.example.cxs.baseapp.ui.base.BaseFragment;
import com.example.cxs.baseapp.util.UIUtil;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by chengxunshi on 2016/8/11.
 */
public class ChapterFragment extends BaseFragment {

    public static final String TAG = "ChapterFragment";
    public static final String CHAPTER = "chapter";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chapters, container, false);
        init(view);
        return view;
    }

    private void init(View view){
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        final ChapterAdapter adapter = new ChapterAdapter();
        adapter.setData(App.getChapters());
        adapter.setItemListener(new ChapterAdapter.RecyclerViewItemListener() {
            @Override
            public void onClick(View view, int position) {
                DazhuzaiResponse.Chapter chapter = adapter.getChapter(position);
                requestChapter(chapter);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    public void requestChapter(DazhuzaiResponse.Chapter chapter){
        if(null == chapter){
            return;
        }
        int start = chapter.url.lastIndexOf("/");
        String subUrl = chapter.url.substring(start, chapter.url.length());
        String url = App.BaseUrl + subUrl;
        UIUtil.showProgressDialog(getFragmentManager(), null, true);
        HttpManager.sendRequest(url,
                new TypeToken<ResponseBean<DazhuzaiChapterResp>>() {}.getType());
    }

    @Subscribe(threadMode= ThreadMode.MAIN)
    public void onEvent(ErrorBean errorBean){
        dismissDialogIfExist(null);
        Toast.makeText(getActivity(), errorBean.msg, Toast.LENGTH_LONG).show();
    }

    @Subscribe
    public void onEvent(DazhuzaiChapterResp response){
        Log.i(TAG, "--------DazhuzaiChapterResp--------");
        dismissDialogIfExist(null);
        Intent intent = new Intent(getActivity(), ChapterReadingFragment.class);
        intent.putExtra(CHAPTER, response.text);
        startFragment(intent);
    }

}
