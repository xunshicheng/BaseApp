package com.example.cxs.baseapp.mvp.component;

import com.example.cxs.baseapp.mvp.module.FragmentModule;
import com.example.cxs.baseapp.ui.ChapterFragment;

import dagger.Component;

/**
 * Created by chengxunshi on 16/10/12.
 */
@Component(modules = FragmentModule.class)
public interface ChaptersComponent {

    void inject(ChapterFragment chapterFragment);

}
