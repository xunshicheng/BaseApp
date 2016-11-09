package com.example.cxs.baseapp.ui.widget.readerview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.text.TextUtils;
import android.view.View;
import android.widget.Scroller;

import com.example.cxs.baseapp.common.Constant;
import com.example.cxs.baseapp.util.ScreenUtils;
import com.example.cxs.baseapp.util.ToastUtils;

public abstract class BaseReadView extends View {

    protected int mScreenWidth;
    protected int mScreenHeight;

    protected PointF mTouch = new PointF();

    protected Bitmap mCurPageBitmap, mNextPageBitmap;
    protected Canvas mCurrentPageCanvas, mNextPageCanvas;
    protected PageFactory pagefactory = null;

    protected OnReadStateChangeListener listener;
    protected String chapterContent, preChapter, nextChapter;
    protected int curChapterNum;
    public boolean isPrepared = false;
    LoadChapterListener loadChapterListener;

    Scroller mScroller;

    public BaseReadView(Context context, String chapterContent, OnReadStateChangeListener listener) {
        super(context);
        this.listener = listener;
        this.chapterContent = chapterContent;

        mScreenWidth = ScreenUtils.getScreenWidth();
        mScreenHeight = ScreenUtils.getScreenHeight();

        mCurPageBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.ARGB_8888);
        mNextPageBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.ARGB_8888);
        mCurrentPageCanvas = new Canvas(mCurPageBitmap);
        mNextPageCanvas = new Canvas(mNextPageBitmap);

        mScroller = new Scroller(getContext());

        pagefactory = new PageFactory(getContext(), chapterContent);
        pagefactory.setOnReadStateChangeListener(listener);
    }

    public void refreshContent(String content, int chapterNum) {
        if (!TextUtils.isEmpty(chapterContent)) {
            if (chapterNum < curChapterNum) {
                preChapter = null;
                nextChapter = chapterContent;
            } else if (chapterNum > curChapterNum) {
                preChapter = chapterContent;
                nextChapter = null;
            }
        }
        curChapterNum = chapterNum;
        chapterContent = content;
        pagefactory.refreshContent(content);
        beginReading();
    }

    public synchronized void beginReading() {
        try {
            pagefactory.onDraw(mCurrentPageCanvas);
            postInvalidate();
        } catch (Exception e) {
        }
        isPrepared = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        calcPoints();
        drawCurrentPageArea(canvas);
        drawNextPageAreaAndShadow(canvas);
        drawCurrentPageShadow(canvas);
        drawCurrentBackArea(canvas);
    }

    protected abstract void drawNextPageAreaAndShadow(Canvas canvas);

    protected abstract void drawCurrentPageShadow(Canvas canvas);

    protected abstract void drawCurrentBackArea(Canvas canvas);

    protected abstract void drawCurrentPageArea(Canvas canvas);

    protected abstract void calcPoints();

    public abstract void setTheme(int theme);

    public void jumpToChapter(int chapter) {
        mTouch.x = 0.1f;
        mTouch.y = 0.1f;
        pagefactory.onDraw(mCurrentPageCanvas);
        pagefactory.onDraw(mNextPageCanvas);
        postInvalidate();
    }

    public void nextPage() {
        if (!pagefactory.nextPage()) {
            ToastUtils.showSingleToast("没有下一页啦");
            return;
        }
        if (isPrepared) {
            pagefactory.onDraw(mCurrentPageCanvas);
            pagefactory.onDraw(mNextPageCanvas);
            postInvalidate();
        }
    }

    public void prePage() {
        if (!pagefactory.prePage()) {
            ToastUtils.showSingleToast("没有上一页啦");
            return;
        }
        if (isPrepared) {
            pagefactory.onDraw(mCurrentPageCanvas);
            pagefactory.onDraw(mNextPageCanvas);
            postInvalidate();
        }
    }

    public synchronized void setFontSize(final int fontSizePx) {
        mTouch.x = 0.1f;
        mTouch.y = 0.1f;
        pagefactory.setTextFont(fontSizePx);
        if (isPrepared) {
            pagefactory.onDraw(mCurrentPageCanvas);
            pagefactory.onDraw(mNextPageCanvas);
            postInvalidate();
        }
    }

    public synchronized void setTextColor(int textColor, int titleColor) {
        pagefactory.setTextColor(textColor, titleColor);
        if (isPrepared) {
            pagefactory.onDraw(mCurrentPageCanvas);
            pagefactory.onDraw(mNextPageCanvas);
            postInvalidate();
        }
    }

    public void setBattery(int battery) {
        pagefactory.setBattery(battery);
        if (isPrepared) {
            pagefactory.onDraw(mCurrentPageCanvas);
            postInvalidate();
        }
    }

    public void setTime(String time) {
        pagefactory.setTime(time);
    }

    public void setLoadChapterListener(LoadChapterListener listener) {
        loadChapterListener = listener;
    }

    public interface LoadChapterListener {
        public void loadPreChapter();

        public void loadNextChapter();
    }
}
