package com.moor.imkf.demo.panel;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Window;
import android.widget.LinearLayout;

import com.moor.imkf.demo.utils.MoorViewUtil;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/1/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public class MoorSwitchFSPanelLinearLayout extends LinearLayout implements IPanelHeightTarget,
        IFSPanelConflictLayout {

    private MoorSwitchFSPanelLayoutHandler panelHandler;

    public MoorSwitchFSPanelLinearLayout(Context context) {
        super(context);
        init();
    }

    public MoorSwitchFSPanelLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public MoorSwitchFSPanelLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MoorSwitchFSPanelLinearLayout(Context context, AttributeSet attrs, int defStyleAttr,
                                         int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        panelHandler = new MoorSwitchFSPanelLayoutHandler(this);
    }

    @Override
    public void refreshHeight(int panelHeight) {
        MoorViewUtil.refreshHeight(this, panelHeight);
    }

    @Override
    public void onKeyboardShowing(boolean showing) {
        panelHandler.onKeyboardShowing(showing);
    }


    @Override
    public void recordKeyboardStatus(final Window window) {
        panelHandler.recordKeyboardStatus(window);
    }
}
