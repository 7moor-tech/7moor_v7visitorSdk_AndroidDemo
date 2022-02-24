package com.moor.imkf.demo.helper;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.moor.imkf.moorsdk.utils.MoorUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/18/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public final class MoorActivityHolder implements Application.ActivityLifecycleCallbacks {

    private static MoorActivityHolder INSTANCE = null;

    @NonNull
    private final Application mApplication;
    private final List<Activity> mActivityStack = new LinkedList<>();

    private MoorActivityHolder(@NonNull Application application) {
        mApplication = application;
        application.registerActivityLifecycleCallbacks(this);
    }

    public static void init(@NonNull Application application) {
        if (INSTANCE == null) {
            INSTANCE = new MoorActivityHolder(application);
        }
    }

    @NonNull
    public static MoorActivityHolder getInstance() {
        return MoorUtils.requireNonNull(INSTANCE, "请先在Application中初始化");
    }

    @NonNull
    public static Application getApplication() {
        return getInstance().mApplication;
    }

    @NonNull
    public static Activity requireActivity(@NonNull Class<Activity> clazz) {
        Activity activity = MoorActivityHolder.getActivity(clazz);
        MoorUtils.requireNonNull(activity, "请确保有已启动的Activity实例");
        return activity;
    }

    @Nullable
    public static Activity getActivity(@NonNull Class<Activity> clazz) {
        if (getInstance().mActivityStack.isEmpty()) {
            return null;
        }
        final int size = getInstance().mActivityStack.size();
        for (int i = size - 1; i >= 0; i--) {
            Activity activity = getInstance().mActivityStack.get(i);
            if (TextUtils.equals(clazz.getName(), activity.getClass().getName())) {
                return activity;
            }
        }
        return null;
    }

    @NonNull
    public static Activity requireCurrentActivity() {
        Activity activity = MoorActivityHolder.getCurrentActivity();
        MoorUtils.requireNonNull(activity, "请确保有已启动的Activity实例");
        return activity;
    }

    @Nullable
    public static Activity getCurrentActivity() {
        if (getInstance().mActivityStack.isEmpty()) {
            return null;
        }
        return getInstance().mActivityStack.get(getInstance().mActivityStack.size() - 1);
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, Bundle savedInstanceState) {
        mActivityStack.add(activity);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        mActivityStack.remove(activity);
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
    }
}
