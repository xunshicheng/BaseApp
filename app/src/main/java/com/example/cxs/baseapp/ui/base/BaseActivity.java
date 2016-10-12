
package com.example.cxs.baseapp.ui.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.example.cxs.baseapp.R;
import com.example.cxs.baseapp.manager.EventManager;

/**
 * 应用程序Activity基类，定义Activity统一生命周期处理，如进出场动画。
 *
 * @author kuloud
 */
@SuppressLint("Registered")
public class BaseActivity extends Activity {
    private static final String TAG = "Activity";
    protected String mTag;
    private boolean foreground = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 小米手机通过manifest设置过场动画会被覆盖为小米的过渡动画，所以在此定义统一动画，特殊界面自行覆写
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_stick);
        mTag = getClass().getName();
        EventManager.registEventBus(this);
        ActivityMaintenance.getInstance().registerActivity(this);
        Log.i(TAG, "[onCreate] " + mTag);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "[onStart] " + mTag);
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "[onStop] " + mTag);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        EventManager.unRegistEventBus(this);
        ActivityMaintenance.getInstance().unregisterActivity(this);
        super.onDestroy();
        Log.i(TAG, "[onDestroy] " + mTag);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_stick, android.R.anim.slide_out_right);
    }

    @Override
    protected void onResume() {
        super.onResume();
        foreground = true;
    }

    @Override
    protected void onPause() {
        foreground = false;
        super.onPause();
    }

    /**
     * 当前界面是否正在展示
     */
    public final boolean isForeground() {
        return foreground;
    }

    /**
     * 获得当前正在显示的对话框，后续可能添加优先级策略，先提供一下这个接口
     *
     * @param tag
     * @return NULL if no showing dialog
     */
    public BaseDialogFragment getShowingDialog(String tag) {
        if (!isFinishing()) {
            if (TextUtils.isEmpty(tag)) {
                tag = BaseDialogFragment.TAG;
            }
            Fragment fragment = getFragmentManager().findFragmentByTag(tag);
            if (fragment instanceof BaseDialogFragment) {
                return (BaseDialogFragment) fragment;
            } else {
                return null;
            }
        } else {
            // 当前未在前端展示时直接返回null
            return null;
        }
    }

    /**
     * 尝试关闭当前的对话框
     */
    public void dismissDialogIfExist(String tag) {
        Log.d(tag, "[dismissDialogIfExist] tag: " + tag);
        BaseDialogFragment dialogFragment = getShowingDialog(tag);
        if (dialogFragment != null) {
            dialogFragment.dismissAllowingStateLoss();
        }
    }
}
