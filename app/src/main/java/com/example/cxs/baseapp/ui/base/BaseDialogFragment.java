
package com.example.cxs.baseapp.ui.base;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;

import com.example.cxs.baseapp.manager.EventManager;


public class BaseDialogFragment extends DialogFragment {
    public static final String TAG = "dialog";
    protected String dialogTag;
    protected DialogInterface.OnClickListener mCallback;
    protected DialogInterface.OnDismissListener mDismissListener;

    public void setDialogTag(String dialogTag) {
        this.dialogTag = dialogTag;
        if (isVisible()) {
            Log.w(TAG, "Dialog is showing, new dialogTag will not take effect.");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventManager.registEventBus(this);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // super.onSaveInstanceState(outState);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (mDismissListener != null) {
            try {
                mDismissListener.onDismiss(dialog);
            } catch (Exception e) {
                // Ignore
                Log.e(TAG, "Dismiss error.", e);
            }
        }
        super.onDismiss(dialog);
    }

    @Override
    public void onDestroy() {
        EventManager.unRegistEventBus(this);
        super.onDestroy();
    }

    /**
     * 显示对话框, 如果当前有其他对话框则关闭之前的（使用默认tag的对话框）
     *
     * @param manager
     */
    public void show(FragmentManager manager) {
        show(manager, false);
    }

    /**
     * @param manager
     * @param append  如果当前已有对话框，append为true表示不关闭之前的对话框（使用默认tag的对话框）
     */
    public void show(FragmentManager manager, boolean append) {
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment prev = manager.findFragmentByTag(TAG);
        try {
            if (prev != null && !append) {
                transaction.remove(prev);
            }
            if (TextUtils.isEmpty(dialogTag)) {
                super.show(transaction, TAG);
            } else {
                super.show(transaction, dialogTag);
            }
        } catch (Exception e) {
            // 异常情况下，会在错误的时间被调用，一方面要求编码设计时考虑生命周期异常，另一方面需要优化异常处理方案，移除try-catch
            Log.e(TAG, "show fragment failed.", e);
        }
    }

    /**
     * 需要处理对话框确认/取消事件的对话框需要注册此回调
     *
     * @param callback
     */
    public void setOnDialogCallback(DialogInterface.OnClickListener callback) {
        this.mCallback = callback;
    }

    /**
     * 处理对话框消失的事件
     *
     * @param dismissListener
     */
    public void setOnDismissListener(DialogInterface.OnDismissListener dismissListener) {
        mDismissListener = dismissListener;
    }

    /**
     * 使用dismissAllowingStateLoss以防止异常调用
     */
    @Deprecated
    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void dismissAllowingStateLoss() {
        try {
            // DialogFragment的dismiss 时机预防在不可控调用时crash
            super.dismissAllowingStateLoss();
        } catch (Exception e) {
            Log.e(TAG, "[dismissAllowingStateLoss]" + e);
        }
    }
}
