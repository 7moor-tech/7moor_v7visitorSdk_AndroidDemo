package com.moor.imkf.demo.panel;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import com.moor.imkf.demo.utils.MoorStatusBarUtil;
import com.moor.imkf.demo.utils.MoorViewUtil;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/1/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public class MoorSwitchRootLayoutHandler {

    private int mOldHeight = -1;
    private final View mTargetRootView;

    private final int mStatusBarHeight;
    private final boolean mIsTranslucentStatus;

    public MoorSwitchRootLayoutHandler(final View rootView) {
        this.mTargetRootView = rootView;
        this.mStatusBarHeight = MoorStatusBarUtil.getStatusBarHeight(rootView.getContext());
        final Activity activity = (Activity) rootView.getContext();
        this.mIsTranslucentStatus = MoorViewUtil.isTranslucentStatus(activity);
    }

    @SuppressLint("LongLogTag")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void handleBeforeMeasure(final int width, int height) {
        if (mIsTranslucentStatus && mTargetRootView.getFitsSystemWindows()) {
            // In this case, the height is always the same one, so, we have to calculate below.
            final Rect rect = new Rect();
            mTargetRootView.getWindowVisibleDisplayFrame(rect);
            height = rect.bottom - rect.top;
        }

        if (height < 0) {
            return;
        }

        if (mOldHeight < 0) {
            mOldHeight = height;
            return;
        }

        final int offset = mOldHeight - height;

        if (offset == 0) {
            return;
        }

        if (Math.abs(offset) == mStatusBarHeight) {
            return;
        }

        mOldHeight = height;
        final IPanelConflictLayout panel = getPanelLayout(mTargetRootView);

        if (panel == null) {
            return;
        }

        // 检测到布局变化非键盘引起
        if (Math.abs(offset) < MoorKeyboardUtil.getMinKeyboardHeight(mTargetRootView.getContext())) {
            return;
        }

        if (offset > 0) {
            //键盘弹起 (offset > 0，高度变小)
            panel.handleHide();
        } else if (panel.isKeyboardShowing() && panel.isVisible()) {
            panel.handleShow();
        }
    }

    private IPanelConflictLayout mPanelLayout;

    private IPanelConflictLayout getPanelLayout(final View view) {
        if (mPanelLayout != null) {
            return mPanelLayout;
        }

        if (view instanceof IPanelConflictLayout) {
            mPanelLayout = (IPanelConflictLayout) view;
            return mPanelLayout;
        }

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                IPanelConflictLayout v = getPanelLayout(((ViewGroup) view).getChildAt(i));
                if (v != null) {
                    mPanelLayout = v;
                    return mPanelLayout;
                }
            }
        }

        return null;
    }
}
