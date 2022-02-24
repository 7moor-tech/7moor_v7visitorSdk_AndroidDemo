package com.moor.imkf.demo.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2021/06/07
 *     @desc   : 解决嵌套滑动冲突
 *     @version: 1.0
 * </pre>
 */
public class MoorChildRecyclerView extends RecyclerView {
    public MoorChildRecyclerView(@NonNull Context context) {
        super(context);
    }

    public MoorChildRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MoorChildRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //父层ViewGroup不要拦截点击事件
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }
}
