package com.example.cxs.baseapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cxs.baseapp.R;
import com.example.cxs.baseapp.ui.base.BaseFragment;

/**
 * Created by chengxunshi on 2016/8/11.
 */
public class ChapterReadingFragment extends BaseFragment {

    private String chapter;

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
        TextView chapterTV = (TextView) view.findViewById(R.id.tv_chapter_content);
        chapterTV.setText(chapter);
    }
}
