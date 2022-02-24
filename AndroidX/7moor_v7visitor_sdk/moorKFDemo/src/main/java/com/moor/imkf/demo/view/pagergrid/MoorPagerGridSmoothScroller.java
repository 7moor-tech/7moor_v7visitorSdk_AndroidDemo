package com.moor.imkf.demo.view.pagergrid;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 6/7/21
 *     @desc   : 用于处理平滑滚动
 *     @version: 1.0
 * </pre>
 */
public class MoorPagerGridSmoothScroller extends LinearSmoothScroller {
    private RecyclerView mRecyclerView;

    public MoorPagerGridSmoothScroller(@NonNull RecyclerView recyclerView) {
        super(recyclerView.getContext());
        mRecyclerView = recyclerView;
    }

    @Override
    protected void onTargetFound(View targetView, RecyclerView.State state, Action action) {
        RecyclerView.LayoutManager manager = mRecyclerView.getLayoutManager();
        if (null == manager) {
            return;
        }
        if (manager instanceof MoorPagerGridLayoutManager) {
            MoorPagerGridLayoutManager layoutManager = (MoorPagerGridLayoutManager) manager;
            int pos = mRecyclerView.getChildAdapterPosition(targetView);
            int[] snapDistances = layoutManager.getSnapOffset(pos);
            final int dx = snapDistances[0];
            final int dy = snapDistances[1];
//            Logi("dx = " + dx);
//            Logi("dy = " + dy);
            final int time = calculateTimeForScrolling(Math.max(Math.abs(dx), Math.abs(dy)));
            if (time > 0) {
                action.update(dx, dy, time, mDecelerateInterpolator);
            }
        }
    }

    @Override
    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
        return MoorPagerConfig.getMillisecondsPreInch() / displayMetrics.densityDpi;
    }
}
