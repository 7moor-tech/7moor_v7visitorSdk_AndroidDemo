package com.moor.imkf.demo.panel;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 1/19/21
 *     @desc   : 此布局必须是活动中的根布局;通过切换键盘和面板来解决布局冲突
 *     @version: 1.0
 * </pre>
 */
public class MoorSwitchRootLinearLayout extends LinearLayout {

    private MoorSwitchRootLayoutHandler conflictHandler;

    public MoorSwitchRootLinearLayout(Context context) {
        super(context);
        init();
    }

    public MoorSwitchRootLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public MoorSwitchRootLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MoorSwitchRootLinearLayout(Context context, AttributeSet attrs, int defStyleAttr,
                                      int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        conflictHandler = new MoorSwitchRootLayoutHandler(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        conflictHandler.handleBeforeMeasure(MeasureSpec.getSize(widthMeasureSpec),
                MeasureSpec.getSize(heightMeasureSpec));
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
