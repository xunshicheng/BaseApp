package com.example.cxs.baseapp.mvp.module;

import com.example.cxs.baseapp.ui.base.BaseFragment;

import dagger.Module;

/**
 * Created by chengxunshi on 16/10/12.
 */

@Module
public class FragmentModule {

    private BaseFragment mFragment;

    public FragmentModule(BaseFragment fragment) {
        mFragment = fragment;
    }

//    //作用和@Inject public ChaptersPresenter(){} 一样; 即:用@Inject修饰构造方法 和 在Module中用@Provides生成对象效果是一样的
//    @Provides
//    ChaptersPresenter provideChaptersPresenter() {
//        return new ChaptersPresenter();
//    }

}
