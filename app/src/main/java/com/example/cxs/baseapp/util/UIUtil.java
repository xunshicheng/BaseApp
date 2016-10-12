package com.example.cxs.baseapp.util;

import android.app.FragmentManager;

import com.example.cxs.baseapp.App;
import com.example.cxs.baseapp.R;
import com.example.cxs.baseapp.ui.base.ProgressDialogFragment;

/**
 * Created by chengxunshi on 2016/8/11.
 */
public class UIUtil {

    public static ProgressDialogFragment showProgressDialog(FragmentManager fragmentManager, String tag, boolean cancelable) {
        ProgressDialogFragment progressDialogFragment = new ProgressDialogFragment();
        progressDialogFragment.setUpdateIcon(-1);
        progressDialogFragment.setDialogTag(tag);
        progressDialogFragment.setMessage(App.getContext().getString(R.string.dialog_loading_tip));
        progressDialogFragment.show(fragmentManager);
        progressDialogFragment.setCancelable(cancelable);
        return progressDialogFragment;
    }
}
