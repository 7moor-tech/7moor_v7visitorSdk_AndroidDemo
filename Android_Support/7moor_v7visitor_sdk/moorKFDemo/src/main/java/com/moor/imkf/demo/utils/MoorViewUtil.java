package com.moor.imkf.demo.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.moor.imkf.demo.constans.MoorDemoConstants;
import com.moor.imkf.demo.panel.MoorKeyboardUtil;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/1/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public class MoorViewUtil {

    public static boolean refreshHeight(final View view, final int aimHeight) {
        if (view.isInEditMode()) {
            return false;
        }

        if (view.getHeight() == aimHeight) {
            return false;
        }

        if (Math.abs(view.getHeight() - aimHeight)
                == MoorStatusBarUtil.getStatusBarHeight(view.getContext())) {
            return false;
        }

        final int validPanelHeight = MoorKeyboardUtil.getValidPanelHeight(view.getContext());
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    validPanelHeight);
            view.setLayoutParams(layoutParams);
        } else {
            layoutParams.height = validPanelHeight;
            view.requestLayout();
        }

        return true;
    }

    public static boolean isFullScreen(final Activity activity) {
        return (activity.getWindow().getAttributes().flags
                & WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean isTranslucentStatus(final Activity activity) {
        //noinspection SimplifiableIfStatement
//        if (MoorSdkVersionUtil.over_19()) {
//            return (activity.getWindow().getAttributes().flags
//                    & WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) != 0;
//        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean isFitsSystemWindows(final Activity activity) {
        //noinspection SimplifiableIfStatement
        return ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0).
                getFitsSystemWindows();
    }

    /**
     * 根据 语音消息的时长 计算view宽度，默认60dp，最大100dp
     *
     * @param time
     * @return
     */
    public static int getVoiceLongWitdth(int time) {
        int width = 60;
        if (time >= MoorDemoConstants.VOICE_RECOERD_LONG_MAX) {
            return 100;
        } else if (time > 0 && time <= 5) {
            return 60;
        }

        int one = (MoorDemoConstants.VOICE_RECOERD_LONG_MAX - 5) / 40;

        int interval = time * one;
        if (interval > 40) {
            width = width + 40;
        } else {
            width = width + interval;
        }
        return width;
    }
}
