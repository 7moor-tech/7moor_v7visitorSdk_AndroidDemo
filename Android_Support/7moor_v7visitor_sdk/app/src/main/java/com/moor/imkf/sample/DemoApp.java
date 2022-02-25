package com.moor.imkf.sample;

import android.app.Application;

import com.moor.imkf.moorsdk.utils.MoorUtils;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2021/08/26
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public class DemoApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //演示demo bugly（可移除）
        CrashReport.initCrashReport(getApplicationContext(), "c634718489", false);

        /*
         * 初始化 MoorUtils
         */
        MoorUtils.init(this);
    }
}
