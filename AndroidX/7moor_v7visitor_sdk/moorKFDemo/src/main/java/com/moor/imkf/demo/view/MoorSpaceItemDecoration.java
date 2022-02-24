package com.moor.imkf.demo.view;

import android.graphics.Canvas;
import android.graphics.Rect;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 6/4/21
 *     @desc   : 底部按钮分割线
 *     @version: 1.0
 * </pre>
 */
public class MoorSpaceItemDecoration extends RecyclerView.ItemDecoration {
    /**
     * leftRight为横向间的距离 topBottom为纵向间距离
     */
    private final int left;
    private final int top;
    private final int right;
    private final int bottom;

    public MoorSpaceItemDecoration(int leftRight, int topBottom) {
        this.left = leftRight;
        this.top = topBottom;
        this.right = leftRight;
        this.bottom = topBottom;
    }

    public MoorSpaceItemDecoration(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        //竖直方向的
        if (layoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
            //最后一项需要 bottom
            if (parent.getChildAdapterPosition(view) == layoutManager.getItemCount() - 1) {
                outRect.bottom = bottom;
            }
            outRect.top = top;
            outRect.left = left;
            outRect.right = right;
        } else {
            //最后一项需要right
            if (parent.getChildAdapterPosition(view) == layoutManager.getItemCount() - 1) {
                outRect.right = right;
            }
            outRect.top = top;
            outRect.left = left;
            outRect.bottom = bottom;
        }
    }

}
