
package com.example.cxs.baseapp.manager;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Method;


public class EventManager {

    private static final String ON_EVENT_METHOD_NAME = "onEvent";

    public static void registEventBus(Object obj) {
        if (!getDefault().isRegistered(obj) && classContainsOnEventMethod(obj.getClass())) {
            getDefault().register(obj);
        }
    }

    public static void unRegistEventBus(Object obj) {
        if (getDefault().isRegistered(obj) && classContainsOnEventMethod(obj.getClass())) {
            getDefault().unregister(obj);
        }
    }

    public static void post(Object object) {
        if (object != null) {
            getDefault().post(object);
        } else {
            Log.e("EventManager", "object can not be null.", new NullPointerException());
        }
    }

    public static void postSticky(Object object) {
        getDefault().postSticky(object);
    }

    private static EventBus getDefault() {
        return EventBus.getDefault();
    }

    @SuppressWarnings("rawtypes")
    private static boolean classContainsOnEventMethod(Class clz) {
        Method[] ms = clz.getDeclaredMethods();
        for (Method method : ms) {
            String methodName = method.getName();
            if (methodName.startsWith(ON_EVENT_METHOD_NAME)) {
                return true;
            }
        }
        return false;
    }
}
