package com.moor.imkf.demo.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

import com.moor.imkf.demo.utils.MoorPixelUtil;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2021/07/21
 *     @desc   : 评价弹框中使用
 *     @version: 1.0
 * </pre>
 */
public class MoorEvaScrollView extends NestedScrollView {
    public MoorEvaScrollView(Context context) {
        super(context);
    }

    public MoorEvaScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MoorEvaScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        //增加滚动偏移量
        return super.computeScrollDeltaToGetChildRectOnScreen(rect) + MoorPixelUtil.dp2px(150f);
    }
}
