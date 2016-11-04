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
import android.widget.TextView;

import com.example.cxs.baseapp.R;
import com.example.cxs.baseapp.ui.base.BaseFragment;
import com.example.cxs.baseapp.ui.widget.readerview.OnReadStateChangeListener;
import com.example.cxs.baseapp.ui.widget.readerview.PageWidget;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chengxunshi on 2016/8/11.
 */
public class ChapterReadingFragment extends BaseFragment {

    private String chapter;
    private PageWidget pageWidget;
    private Receiver receiver = new Receiver();
    private IntentFilter intentFilter = new IntentFilter();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            chapter = getArguments().getString(ChapterFragment.CHAPTER, "内容缺失");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chapter_reading, container, false);
        init(view);
        return view;
    }

    private void init(View view){
        FrameLayout flReadWidget = (FrameLayout) view.findViewById(R.id.flReadWidget);
        pageWidget = new PageWidget(getActivity(), chapter, new OnReadStateChangeListener() {
            @Override
            public void onChapterChanged(int chapter) {
                Log.i("cxs", "-------onChapterChanged--------");
            }

            @Override
            public void onPageChanged(int chapter, int page) {
                Log.i("cxs", "-------onPageChanged--------");
            }

            @Override
            public void onLoadChapterFailure(int chapter) {
                Log.i("cxs", "-------onLoadChapterFailure--------");
            }

            @Override
            public void onCenterClick() {
                Log.i("cxs", "-------onCenterClick--------");
            }

            @Override
            public void onFlip() {
                Log.i("cxs", "-------onFlip--------");
            }
        });
        pageWidget.setTextColor(ContextCompat.getColor(getActivity(), R.color.chapter_content_night),
                ContextCompat.getColor(getActivity(), R.color.chapter_title_night));
        pageWidget.beginReading();
        flReadWidget.addView(pageWidget);
        getActivity().registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }

    class Receiver extends BroadcastReceiver {

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        @Override
        public void onReceive(Context context, Intent intent) {
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
