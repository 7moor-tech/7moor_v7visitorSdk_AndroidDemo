package com.moor.imkf.demo.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.moor.imkf.demo.utils.MoorScreenUtils;
import com.moor.imkf.moorsdk.utils.MoorUtils;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2021/06/10
 *     @desc   : 设置最大宽度的 LinearLayout
 *     @version: 1.0
 * </pre>
 */
public class MoorMaxWidthLinearLayout extends LinearLayout {
    int maxwidth = (int) (MoorScreenUtils.getScreenWidthOrHeight(MoorUtils.getApp()) * 0.65);

    public MoorMaxWidthLinearLayout(Context context) {
        super(context);
    }

    public MoorMaxWidthLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MoorMaxWidthLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getMeasuredWidth() > maxwidth) {
            setMeasuredDimension(maxwidth, getMeasuredHeight());
        }
    }
}
