package com.example.cxs.baseapp.mvp.component;

import com.example.cxs.baseapp.mvp.module.FragmentModule;
import com.example.cxs.baseapp.ui.ChapterFragment;
import com.example.cxs.baseapp.ui.NovelFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by chengxunshi on 16/10/12.
 */

@Singleton
@Component(dependencies = AppComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {

    void inject(NovelFragment fragment);
    void inject(ChapterFragment chapterFragment);

}
