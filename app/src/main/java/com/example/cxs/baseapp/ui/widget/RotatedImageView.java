package com.example.cxs.baseapp.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.example.cxs.baseapp.R;

/**
 * 自动按中心旋转的imageview
 */
public class RotatedImageView extends ImageView {
    /**
     * 每次旋转的角度
     */
    private int degreeStep = 1;
    /**
     * 旋转一周(360°)所需的时间
     */
    private long cycleInterval = 3000;

    private final Runnable rotateTask = new Runnable() {
        @Override
        public void run() {
            setRotation(getRotation() + degreeStep);
            postDelayed(this, cycleInterval / (360 / degreeStep));
        }
    };

    public RotatedImageView(Context context) {
        this(context, null);
    }

    public RotatedImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RotatedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        //读取参数
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.RotatedImageView);
            long cl = a.getInt(R.styleable.RotatedImageView_cycle_interval, (int) cycleInterval);
            setCycleInterval(cl);
            int ds = a.getInt(R.styleable.RotatedImageView_degree_step, degreeStep);
            setRotateStep(ds);
            a.recycle();
        }
    }

    /**
     * 设置每次旋转的角度
     *
     * @param degreeStep 角度
     */
    public void setRotateStep(int degreeStep) {
        if (degreeStep < 0 || degreeStep > 360) {
            throw new IllegalArgumentException("degreeStep must be greater than 0 and less than 360.");
        }
        this.degreeStep = degreeStep;
        scheduleRotateTask();
    }

    /**
     * 设置旋转一周所需时间
     *
     * @param cycleInterval 时间(毫秒)
     */
    public void setCycleInterval(long cycleInterval) {
        if (cycleInterval < 0) {
            throw new IllegalArgumentException("cycleInterval must be greater than 0.");
        }
        this.cycleInterval = cycleInterval;
        scheduleRotateTask();
    }

    /**
     * 开始任务
     */
    private void scheduleRotateTask() {
        removeCallbacks(rotateTask);
        if (getVisibility() == VISIBLE) {
            //只有可见时旋转
            post(rotateTask);
        }
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        //Log.w("Kuloud", "onVisibilityChanged: " + changedView + ", "+visibility);
        super.onVisibilityChanged(changedView, visibility);
        scheduleRotateTask();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        //Log.w("Kuloud", "onWindowVisibilityChanged: " + visibility);
        if (visibility != VISIBLE) {
            removeCallbacks(rotateTask);
        }
    }
}
