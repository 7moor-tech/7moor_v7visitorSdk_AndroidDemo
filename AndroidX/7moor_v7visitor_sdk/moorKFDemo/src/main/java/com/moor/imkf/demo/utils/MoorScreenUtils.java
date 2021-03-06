package com.moor.imkf.demo.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.Display;
import android.view.WindowManager;

import com.moor.imkf.lib.utils.MoorDensityUtil;
import com.moor.imkf.lib.utils.MoorSdkVersionUtil;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/1/21
 *     @desc   : 获取屏幕宽、高
 *     @version: 1.0
 * </pre>
 */
public class MoorScreenUtils extends MoorDensityUtil {
    private static final int PORTRAIT = 0;
    private static final int LANDSCAPE = 1;
    private volatile static boolean mHasCheckAllScreen;
    private volatile static boolean mIsAllScreenDevice;
    @NonNull
    private static final Point[] mRealSizes = new Point[2];

    private static int getScreenRealHeight(@Nullable Context context) {

        int orientation = context.getResources().getConfiguration().orientation;
        orientation = orientation == Configuration.ORIENTATION_PORTRAIT ? PORTRAIT : LANDSCAPE;

        if (mRealSizes[orientation] == null) {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (windowManager == null) {
                return getScreenHeight(context);
            }
            Display display = windowManager.getDefaultDisplay();
            Point point = new Point();
            display.getRealSize(point);
            mRealSizes[orientation] = point;
        }
        return mRealSizes[orientation].y;
    }


    public static int getFullActivityHeight(@Nullable Context context) {
        if (!isAllScreenDevice(context)) {
            return getScreenHeight(context);
        }
        return getScreenRealHeight(context);
    }
    private static boolean isAllScreenDevice(Context context) {
        if (mHasCheckAllScreen) {
            return mIsAllScreenDevice;
        }
        mHasCheckAllScreen = true;
        mIsAllScreenDevice = false;
        // 低于 API 21的，都不会是全面屏。。。
        if (MoorSdkVersionUtil.over21()) {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (windowManager != null) {
                Display display = windowManager.getDefaultDisplay();
                Point point = new Point();
                display.getRealSize(point);
                float width, height;
                if (point.x < point.y) {
                    width = point.x;
                    height = point.y;
                } else {
                    width = point.y;
                    height = point.x;
                }
                if (height / width >= 1.97f) {
                    mIsAllScreenDevice = true;
                }
            }
            return mIsAllScreenDevice;
        } else {
            return false;

        }

    }
}
