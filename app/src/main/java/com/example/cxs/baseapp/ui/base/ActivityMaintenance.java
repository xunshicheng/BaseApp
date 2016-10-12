
package com.example.cxs.baseapp.ui.base;

import android.app.Activity;
import android.content.Intent;

import com.example.cxs.baseapp.App;

import java.util.ArrayList;
import java.util.List;


/**
 * 用于管理Activity实例
 */
public class ActivityMaintenance {

    private static ActivityMaintenance sInstance;

    private List<Activity> mActivities = new ArrayList<Activity>();

    private static final Object LOCK = ActivityMaintenance.class;

    private ActivityMaintenance() {
    }

    public static ActivityMaintenance getInstance() {
        if (sInstance == null) {
            sInstance = new ActivityMaintenance();
        }
        return sInstance;
    }

    public void registerActivity(Activity activity) {
        synchronized (LOCK) {
            if (!mActivities.contains(activity)) {
                mActivities.add(activity);
            }
        }
    }

    public void unregisterActivity(Activity activity) {
        synchronized (LOCK) {
            if (mActivities.contains(activity)) {
                mActivities.remove(activity);
            }
        }
    }

    public void finishAllActivities() {
        synchronized (LOCK) {
            for (Activity activity : mActivities) {
                activity.finish();
            }
            mActivities.clear();
        }
    }

    public void startActivityAndFinishOthers(Intent intent) {
        synchronized (LOCK) {
            if (mActivities.isEmpty()) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                App.getContext().startActivity(intent);
            } else {
                List<Activity> oldAcs = new ArrayList<Activity>(mActivities);
                mActivities.get(mActivities.size() - 1).startActivity(intent);
                for (Activity activity : oldAcs) {
                    activity.finish();
                }
                mActivities.removeAll(oldAcs);
            }
        }
    }

    public int getActivityCount() {
        return mActivities.size();
    }

    /**
     * activity是否可见，锁屏、在后台均视为不可见
     */
    public boolean isActivityVisiable() {
        boolean visiable = false;
        if (!mActivities.isEmpty()) {
            for (Activity activity : mActivities) {
                if (activity instanceof BaseActivity) {
                    if (((BaseActivity) activity).isForeground()) {
                        visiable = true;
                        break;
                    }
                }
            }
        }
        return visiable;
    }
}
