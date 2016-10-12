package com.example.cxs.baseapp.ui.base;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.cxs.baseapp.R;


public class ProgressDialogFragment extends BaseDialogFragment {
    protected TextView mTextView;
    protected CharSequence mTip = "";
    private ImageView mProgress;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private int mIconId;

    public void setMessage(CharSequence message) {
        mTip = message;
        if (null != mTextView) {
            mTextView.setText(message);
        }
    }

    /**
     * 显示自定义的图标，Id小于0时使用默认菊花
     *
     * @param resImgId 资源ID
     */
    public void setUpdateIcon(int resImgId) {
        mIconId = resImgId;
        updateCustomIcon();
    }

    private void updateCustomIcon() {
        if (null == mImageView || null == mProgress) {
            return;
        }

        if (mIconId > 0) {
            mImageView.setBackgroundResource(mIconId);
            mImageView.setVisibility(View.VISIBLE);
            mProgress.setVisibility(View.GONE);
        } else {
            mImageView.setVisibility(View.GONE);
            mProgress.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = createViewImpl(inflater, container, savedInstanceState);
        mTextView = (TextView) v.findViewById(R.id.alert_text);
        mProgress = (ImageView) v.findViewById(R.id.alert_progress);
        mImageView = (ImageView) v.findViewById(R.id.alert_icon);
        mProgressBar = (ProgressBar) v.findViewById(R.id.progress_bar_dialog);
        updateCustomIcon();
        if (!TextUtils.isEmpty(mTip)) {
            mTextView.setText(mTip);
        }
        onViewCreated();
        return v;
    }

    /**
     * 用于创建View，子类可重写此方法以提供不同的布局
     */
    protected View createViewImpl(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_progress, container, false);
    }

    /**
     * View创建完成后调用，子类可重写此方法
     */
    protected void onViewCreated() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void updateProgress(int progress) {
        mProgressBar.setProgress(progress);
    }

}
