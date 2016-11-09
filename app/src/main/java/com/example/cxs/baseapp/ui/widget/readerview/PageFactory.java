package com.example.cxs.baseapp.ui.widget.readerview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.example.cxs.baseapp.App;
import com.example.cxs.baseapp.R;
import com.example.cxs.baseapp.util.ScreenUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class PageFactory {
    /**
     * 屏幕宽高
     */
    private int mHeight, mWidth;
    /**
     * 文字区域宽高
     */
    private int mVisibleHeight, mVisibleWidth;
    /**
     * 间距
     */
    private int marginHeight, marginWidth;
    /**
     * 字体大小
     */
    private int mFontSize, mNumFontSize;
    /**
     * 每页行数
     */
    private int mPageLineCount;
    /**
     * 行间距
     **/
    private int mLineSpace;
    private int curEndPos = 0, curBeginPos = 0;
    private Vector<String> mLines = new Vector<>();

    private Paint mPaint;
    private Paint mTitlePaint;
    private Bitmap mBookPageBg;

    private DecimalFormat decimalFormat = new DecimalFormat("#0.00");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
    private int timeLen = 0, percentLen = 0;
    private String time;
    private int battery = 40;
    private Rect rectF;
    private ProgressBar batteryView;
    private Bitmap batteryBitmap;

    private String chapterContent;
    private int currentPage = 1;

    private OnReadStateChangeListener listener;

    public PageFactory(Context context, String chapterContent) {
        this(context, ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight(), 52, chapterContent);
    }

    public PageFactory(Context context, int width, int height, int fontSize, String chapterContent) {
        mWidth = width;
        mHeight = height;
        mFontSize = fontSize;
        mLineSpace = mFontSize / 5 * 2;
        mNumFontSize = ScreenUtils.dpToPxInt(16);
        marginWidth = ScreenUtils.dpToPxInt(8);
        marginHeight = ScreenUtils.dpToPxInt(16);
        mVisibleHeight = mHeight - marginHeight * 2 - mNumFontSize * 2 - mLineSpace * 2;
        mVisibleWidth = mWidth - marginWidth * 2;
        mPageLineCount = mVisibleHeight / (mFontSize + mLineSpace);
        rectF = new Rect(0, 0, mWidth, mHeight);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(mFontSize);
        mPaint.setTextSize(ContextCompat.getColor(context, R.color.chapter_content_day));
        mPaint.setColor(Color.BLACK);
        mTitlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTitlePaint.setTextSize(mFontSize);
        mTitlePaint.setColor(ContextCompat.getColor(App.getContext(), R.color.chapter_title_day));
        timeLen = (int) mTitlePaint.measureText("00:00");
        percentLen = (int) mTitlePaint.measureText("00");

        this.chapterContent = chapterContent;
        time = dateFormat.format(new Date());
        batteryView = (ProgressBar) LayoutInflater.from(context).inflate(R.layout.layout_battery_progress, null);
        setBattery(50);
    }

    public void refreshContent(String content) {
        chapterContent = content;
        curEndPos = 0;
        curBeginPos = 0;
        currentPage = 1;
        mLines.clear();
    }

    /**
     * 绘制阅读页面
     *
     * @param canvas
     */
    public synchronized void onDraw(Canvas canvas) {
        if (mLines.size() == 0) {
            curEndPos = curBeginPos;
            mLines = pageDown();
        }
        if (mLines.size() > 0) {
            int y = marginHeight;
            // 绘制背景
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//            if (mBookPageBg != null) {
//                canvas.drawBitmap(mBookPageBg, null, rectF, null);
//            } else {
//                canvas.drawColor(Color.WHITE);
//            }
            // 绘制标题
//            canvas.drawText("-----Title-----", marginWidth, y, mTitlePaint);
//            y += marginHeight;
            // 绘制阅读页面文字
            for (String line : mLines) {
                y += mLineSpace;
                if (line.endsWith("@")) {
                    canvas.drawText(line.substring(0, line.length() - 1), marginWidth, y, mPaint);
                    y += mLineSpace;
                } else {
                    canvas.drawText(line, marginWidth, y, mPaint);
                }
                y += mFontSize;
            }
            // 绘制提示内容
            if (batteryBitmap != null) {
                canvas.drawBitmap(batteryBitmap, marginWidth + 2,
                        mHeight - marginHeight * 2 - ScreenUtils.dpToPxInt(12), mTitlePaint);
            }

            String pageStr = currentPage < 10 ? "0" + currentPage : "" + currentPage;
            canvas.drawText(pageStr, (mWidth - percentLen) / 2,
                    mHeight - marginHeight * 2, mTitlePaint);

            String mTime = dateFormat.format(new Date());
            canvas.drawText(mTime, mWidth - marginWidth - timeLen, mHeight - marginHeight * 2, mTitlePaint);
        }
    }

    /**
     * 指针移到上一页页首
     */
    private void pageUp() {
        String strParagraph = "";
        Vector<String> lines = new Vector<>(); // 页面行
        int paraSpace = 0;
        mPageLineCount = mVisibleHeight / (mFontSize + mLineSpace);
        while ((lines.size() < mPageLineCount) && (curBeginPos > 0)) {
            Vector<String> paraLines = new Vector<>(); // 段落行
            strParagraph = readParagraphBack(curBeginPos); // 1.读取上一个段落
            curBeginPos -= strParagraph.length(); // 2.变换起始位置指针
            strParagraph = strParagraph.replaceAll("\r\n", "  ");
            strParagraph = strParagraph.replaceAll("\n", " ");

            while (strParagraph.length() > 0) { // 3.逐行添加到lines
                int paintSize = mPaint.breakText(strParagraph, true, mVisibleWidth, null);
                paraLines.add(strParagraph.substring(0, paintSize));
                strParagraph = strParagraph.substring(paintSize);
            }
            lines.addAll(0, paraLines);

            while (lines.size() > mPageLineCount) { // 4.如果段落添加完，但是超出一页，则超出部分需删减
                curBeginPos += lines.get(0).length(); // 5.删减行数同时起始位置指针也要跟着偏移
                lines.remove(0);
            }
            curEndPos = curBeginPos; // 6.最后结束指针指向下一段的开始处
            paraSpace += mLineSpace;
            mPageLineCount = (mVisibleHeight - paraSpace) / (mFontSize + mLineSpace); // 添加段落间距，实时更新容纳行数
        }
    }

    /**
     * 根据起始位置指针，读取一页内容
     *
     * @return
     */
    private Vector<String> pageDown() {
        String strParagraph = "";
        Vector<String> lines = new Vector<>();
        int paraSpace = 0;
        mPageLineCount = mVisibleHeight / (mFontSize + mLineSpace);
        while ((lines.size() < mPageLineCount) && (curEndPos < chapterContent.length())) {
            strParagraph = readParagraphForward(curEndPos);
            curEndPos += strParagraph.length();
            strParagraph = strParagraph.replaceAll("\r\n", "  ")
                    .replaceAll("\n", " "); // 段落中的换行符去掉，绘制的时候再换行

            while (strParagraph.length() > 0) {
                int paintSize = mPaint.breakText(strParagraph, true, mVisibleWidth, null);
                lines.add(strParagraph.substring(0, paintSize));
                strParagraph = strParagraph.substring(paintSize);
                if (lines.size() >= mPageLineCount) {
                    break;
                }
            }
            lines.set(lines.size() - 1, lines.get(lines.size() - 1) + "@");
            curEndPos -= strParagraph.length();
            paraSpace += mLineSpace;
            mPageLineCount = (mVisibleHeight - paraSpace) / (mFontSize + mLineSpace);
        }
        return lines;
    }

    /**
     * 获取最后一页的内容。比较繁琐，待优化
     *
     * @return
     */
    public Vector<String> pageLast() {
        String strParagraph = "";
        Vector<String> lines = new Vector<>();
        currentPage = 0;
        while (curEndPos < chapterContent.length()) {
            int paraSpace = 0;
            mPageLineCount = mVisibleHeight / (mFontSize + mLineSpace);
            curBeginPos = curEndPos;
            while ((lines.size() < mPageLineCount) && (curEndPos < chapterContent.length())) {
                strParagraph = readParagraphForward(curEndPos);
                curEndPos += strParagraph.length();
                strParagraph = strParagraph.replaceAll("\r\n", "  ");
                strParagraph = strParagraph.replaceAll("\n", " "); // 段落中的换行符去掉，绘制的时候再换行

                while (strParagraph.length() > 0) {
                    int paintSize = mPaint.breakText(strParagraph, true, mVisibleWidth, null);
                    lines.add(strParagraph.substring(0, paintSize));
                    strParagraph = strParagraph.substring(paintSize);
                    if (lines.size() >= mPageLineCount) {
                        break;
                    }
                }
                lines.set(lines.size() - 1, lines.get(lines.size() - 1) + "@");

                if (strParagraph.length() != 0) {
                    curEndPos -= strParagraph.length();
                }
                paraSpace += mLineSpace;
                mPageLineCount = (mVisibleHeight - paraSpace) / (mFontSize + mLineSpace);
            }
            if (curEndPos < chapterContent.length()) {
                lines.clear();
            }
            currentPage++;
        }
        return lines;
    }

    /**
     * 读取下一段落
     *
     * @param curEndPos 当前页结束位置指针
     * @return
     */
    private String readParagraphForward(int curEndPos) {
        int i = curEndPos;
        while (i < chapterContent.length()) {
            if (0x0a == chapterContent.charAt(i++)) {
                break;
            }
        }
        return chapterContent.substring(curEndPos, i);
    }

    /**
     * 读取上一段落
     *
     * @param curBeginPos 当前页起始位置指针
     * @return
     */
    private String readParagraphBack(int curBeginPos) {
        int i = curBeginPos;
        while (i > 0) {
            if (0x0a == chapterContent.charAt(i--)) {
                break;
            }
        }
        return chapterContent.substring(i, curBeginPos);
    }

    public boolean hasNextPage() {
        return curEndPos < chapterContent.length();
    }

    public boolean hasPrePage() {
        return curBeginPos > 0;
    }

    /**
     * 跳转下一页
     */
    public boolean nextPage() {
        if (!hasNextPage()) { // 最后一章的结束页
            return false;
        } else {
            if (curEndPos >= chapterContent.length()) { // 中间章节结束页
                return false;
            }
            mLines.clear();
            curBeginPos = curEndPos; // 起始指针移到结束位置
            mLines = pageDown(); // 读取一页内容
            onPageChanged(0, ++currentPage);
        }
        return true;
    }

    /**
     * 跳转上一页
     */
    public boolean prePage() {
        if (!hasPrePage()) { // 第一章第一页
            return false;
        } else {
            // 保存当前页的值
            if (curBeginPos <= 0) {
                return false;
            }
            mLines.clear();
            pageUp(); // 起始指针移到上一页开始处
            mLines = pageDown(); // 读取一页内容
            onPageChanged(0, --currentPage);
        }
        return true;
    }

    public void cancelPage() {
        curBeginPos = 0;
        curEndPos = 0;
        mLines.clear();
        mLines = pageDown();
    }

    /**
     * 设置字体大小
     *
     * @param fontsize 单位：px
     */
    public void setTextFont(int fontsize) {
        mFontSize = fontsize;
        mLineSpace = mFontSize / 5 * 2;
        mPaint.setTextSize(mFontSize);
        mPageLineCount = mVisibleHeight / (mFontSize + mLineSpace);
        curEndPos = curBeginPos;
        nextPage();
    }

    /**
     * 设置字体颜色
     *
     * @param textColor
     * @param titleColor
     */
    public void setTextColor(int textColor, int titleColor) {
        mPaint.setColor(textColor);
        mTitlePaint.setColor(titleColor);
    }

    public int getTextFont() {
        return mFontSize;
    }

    /**
     * 根据百分比，跳到目标位置
     *
     * @param persent
     */
    public void setPercent(int persent) {
        float a = (float) (chapterContent.length() * persent) / 100;
        curEndPos = (int) a;
        if (curEndPos == 0) {
            nextPage();
        } else {
            nextPage();
            prePage();
            nextPage();
        }
    }

    public void setBgBitmap(Bitmap BG) {
        mBookPageBg = BG;
    }

    public void setOnReadStateChangeListener(OnReadStateChangeListener listener) {
        this.listener = listener;
    }

    void onChapterChanged(int chapter) {
        if (listener != null)
            listener.onChapterChanged(chapter);
    }

    void onPageChanged(int chapter, int page) {
        if (listener != null)
            listener.onPageChanged(chapter, page);
    }

    void onLoadChapterFailure(int chapter) {
        if (listener != null)
            listener.onLoadChapterFailure(chapter);
    }

    public Bitmap convertBetteryBitmap() {
        batteryView.setProgress(battery);
        batteryView.setDrawingCacheEnabled(true);
        batteryView.measure(View.MeasureSpec.makeMeasureSpec(ScreenUtils.dpToPxInt(26), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(ScreenUtils.dpToPxInt(14), View.MeasureSpec.EXACTLY));
        batteryView.layout(0, 0, batteryView.getMeasuredWidth(), batteryView.getMeasuredHeight());
        batteryView.buildDrawingCache();
        return batteryView.getDrawingCache();
    }

    public void setBattery(int battery) {
        this.battery = battery;
        batteryBitmap = convertBetteryBitmap();
    }

    public void setTime(String time) {
        this.time = time;
    }
}
