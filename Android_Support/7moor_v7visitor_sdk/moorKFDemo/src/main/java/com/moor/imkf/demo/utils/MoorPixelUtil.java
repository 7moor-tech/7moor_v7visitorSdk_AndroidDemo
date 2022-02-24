package com.moor.imkf.demo.utils;

import android.content.res.Resources;
import android.util.TypedValue;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/1/21
 *     @desc   : dp、px工具类
 *     @version: 1.0
 * </pre>
 */
public class MoorPixelUtil {

    /**
     * dp-->px
     *
     * @param dp
     * @return
     */
    public static int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

    public static float dp2pxF(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }
}
