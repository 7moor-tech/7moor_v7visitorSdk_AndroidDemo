package com.moor.imkf.demo.utils;

import android.graphics.drawable.GradientDrawable;

public class MoorDrawableUtils {

    /**
     * 创建一个圆形的shape背景
     * @param strokeWidth 边框宽度Px
     * @param roundRadius 圆角半径Px
     * @param strokeColor 边框颜色
     * @param fillColor 填充颜色
     * @return
     */
    public static GradientDrawable getCircleShape(int strokeWidth,int roundRadius,int strokeColor,int fillColor){
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(fillColor);
        gd.setCornerRadius(roundRadius);
        gd.setShape(GradientDrawable.OVAL);
        gd.setStroke(strokeWidth, strokeColor);
        return  gd;
    }
}
