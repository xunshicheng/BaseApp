
package com.example.cxs.baseapp.ui.base;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.cxs.baseapp.R;
import com.example.cxs.baseapp.manager.EventManager;
import com.example.cxs.baseapp.mvp.base.IBaseView;

/**
 * Fragment基类，提供通用+“非常”常用的接口方法
 */
public class BaseFragment extends Fragment implements IBaseView {
    public static final int RESULT_CANCELED = 0;
    public static final int RESULT_OK = -1;
    private static final String TAG = "fragment";
    public String fragmentTag = getClass().getName();
    //上一个页面
    protected String referPageClassName = "";
    protected BaseFragment pre_fragment;
    protected int request_code;
    protected int result_code = RESULT_CANCELED;
    protected Intent result_intent;
    private boolean bForeground = false;

    private int mEnter = R.animator.fragment_slide_left_enter,
            mExit = 0,
            mPopEnter = 0,
            mPopExit = R.animator.fragment_slide_right_exit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "[onCreate] " + fragmentTag);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.i(TAG, "[onAttach] " + fragmentTag);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.i(TAG, "[onViewStateRestored] " + fragmentTag);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "[onStart] " + fragmentTag);
        EventManager.registEventBus(this);
    }

    public void onFragmentResult(int requestCode, int resultCode, Intent intent) {
        Log.i(TAG, "[onFragmentResult] " + fragmentTag + ", requestCode " + requestCode + ", resultCode " + resultCode + ", intent " + intent);
    }

    @Override
    public void onResume() {
        onFragmentResult(request_code, result_code, result_intent);
        super.onResume();
        bForeground = true;
        Log.i(TAG, "[onResume] " + fragmentTag);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "[onSaveInstanceState] " + fragmentTag);
    }

    @Override
    public void onPause() {
        bForeground = false;
        super.onPause();
        Log.i(TAG, "[onPause] " + fragmentTag);
    }

    @Override
    public void onStop() {
        Log.i(TAG, "[onStop] " + fragmentTag);
        EventManager.unRegistEventBus(this);
        super.onStop();
    }

    @Override
    public void onDetach() {
        Log.i(TAG, "[onDetach] " + fragmentTag);
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "[onDestroy] " + fragmentTag);
        super.onDestroy();

    }

    /**
     * 判断當前Activiy是否在运行
     *
     * @return
     */
    protected boolean isActivityRunning() {
        return getActivity() != null && !getActivity().isFinishing();
    }

    /**
     * 判斷當前fragment是否在運行
     *
     * @return
     */
    protected boolean isRunning() {
        return isActivityRunning() && bForeground;
    }

    /**
     * 跳转到新的fragment
     *
     * @param intent
     */
    protected void startFragment(Intent intent) {
        innerStartFragmentForResult(intent, null);
    }

    protected void startFragment(BaseFragment fragment) {
        startFragment(R.id.fragment_container, fragment, 0);
    }

    /**
     * 跳转到新的fragment
     *
     * @param fragment 新的fragment实例
     */
    protected void startFragment(BaseFragment fragment, int flag) {
        startFragment(R.id.fragment_container, fragment, flag);
    }

    /**
     * 通过传入动画资源，实现fragment的自定义动画
     */
    protected void startFragmentWithCustomAnim(int containerId, BaseFragment fragment,
                                               int enter, int exit,
                                               int popEnter, int popExit) {
        startFragmentWithCustomAnim(containerId, fragment, 0, enter, exit, popEnter, popExit);
    }

    protected void startFragmentWithCustomAnim(int containerId, BaseFragment fragment, int flag,
                                               int enter, int exit,
                                               int popEnter, int popExit) {
        mEnter = enter;
        mExit = exit;
        mPopEnter = popEnter;
        mPopExit = popExit;
        startFragment(containerId, fragment, flag);
    }

    /**
     * 跳转到新的fragment，可指定容器布局id
     *
     * @param containerId 容器布局id
     * @param fragment    新的fragment实例
     */
    protected void startFragment(int containerId, BaseFragment fragment, int flag) {
        FragmentManager fm = getActivity().getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        //下个页面的前一页就是本页
        fragment.referPageClassName = getClass().getName();
        if ((flag & Intent.FLAG_ACTIVITY_NO_ANIMATION) == Intent.FLAG_ACTIVITY_NO_ANIMATION) {
            ft.setCustomAnimations(0, 0, 0, 0);
        } else {
            ft.setCustomAnimations(mEnter, mExit, mPopEnter, mPopExit);
        }

        if (((flag & Intent.FLAG_ACTIVITY_CLEAR_TOP) == Intent.FLAG_ACTIVITY_CLEAR_TOP)
                && (fm.findFragmentByTag(fragment.fragmentTag) != null)) {
            fm.popBackStack(fragment.fragmentTag, 0);
        } else {
            ft.replace(containerId, fragment, fragment.fragmentTag);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.addToBackStack(fragment.fragmentTag);
        }

        dismissDialogIfExist(null);
        ft.commitAllowingStateLoss();
    }

    private void innerStartFragmentForResult(Intent intent, Integer requestCode) {
        if (!isActivityRunning()) return;
        Object fragmentObj = null;
        String targetFragmentClassName = intent.getComponent().getClassName();
        try {
            fragmentObj = Class.forName(targetFragmentClassName).newInstance();
        } catch (Exception e) {
            Log.e(TAG, "create failed.", e);
        }
        if (!(fragmentObj instanceof BaseFragment)) return;
        BaseFragment fragment = (BaseFragment) fragmentObj;
        fragment.setArguments(intent.getExtras());
        if (requestCode != null) {
            this.request_code = requestCode;
            fragment.pre_fragment = BaseFragment.this;
        }
        startFragment(fragment, intent.getFlags());
    }

    protected void setResult(int resultCode, Intent intent) {
        if (pre_fragment != null) {
            pre_fragment.result_code = resultCode;
            pre_fragment.result_intent = intent;
        }
    }

    /**
     */
    public void show() {
        if (!isActivityRunning()) return;
        FragmentManager fm = getActivity().getFragmentManager();
        FragmentTransaction transactionExit = fm.beginTransaction();
        transactionExit.show(this);
        transactionExit.commitAllowingStateLoss();
    }

    /**
     */
    public void hide() {
        if (!isActivityRunning()) return;
        FragmentManager fm = getActivity().getFragmentManager();
        FragmentTransaction transactionExit = fm.beginTransaction();
        transactionExit.hide(this);
        transactionExit.commitAllowingStateLoss();
    }

    /**
     */
    public void finish() {
        // 无fragment栈的fragment退出时销毁附属的activity
        if (isActivityRunning()) {
            FragmentManager fm = getActivity().getFragmentManager();
            if (fm.getBackStackEntryCount() > 1) {
                if (isRunning()) {
                    FragmentTransaction ft = fm.beginTransaction();
                    Log.d(TAG, "[finish] popBackStack " + fragmentTag);
                    fm.popBackStack(fragmentTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    ft.commitAllowingStateLoss();
                } else {
                    Log.d(TAG, "[finish] in finishing " + fragmentTag);
                }
            } else {
                getActivity().finish();
            }

        } else {
            Log.d(TAG, "[finish] isActivityRunning FALSE");
        }
    }

    /**
     * 获得当前正在显示的对话框，后续可能添加优先级策略，先提供一下这个接口
     *
     * @param tag
     * @return NULL if no showing dialog
     */
    public BaseDialogFragment getShowingDialog(String tag) {
        if (isActivityRunning()) {
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
        Log.d(TAG, "[dismissDialogIfExist] tag: " + tag + ", fragment: " + BaseFragment.this.fragmentTag);
        BaseDialogFragment dialogFragment = getShowingDialog(tag);
        if (dialogFragment != null) {
            dialogFragment.dismissAllowingStateLoss();
        }
    }

    @Override
    public void showErrorMsg(String msg) {
        if(!TextUtils.isEmpty(msg)) {
            Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
        }
    }
}
