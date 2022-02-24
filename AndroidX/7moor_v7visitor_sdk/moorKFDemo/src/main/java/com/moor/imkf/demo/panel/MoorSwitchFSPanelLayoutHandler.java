package com.moor.imkf.demo.panel;

import android.view.View;
import android.view.Window;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/1/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public class MoorSwitchFSPanelLayoutHandler implements IFSPanelConflictLayout {

    private final View panelLayout;
    private boolean isKeyboardShowing;

    public MoorSwitchFSPanelLayoutHandler(final View panelLayout) {
        this.panelLayout = panelLayout;
    }

    public void onKeyboardShowing(boolean showing) {
        isKeyboardShowing = showing;
        if (!showing && panelLayout.getVisibility() == View.INVISIBLE) {
            panelLayout.setVisibility(View.GONE);
        }

        if (!showing && recordedFocusView != null) {
            restoreFocusView();
            recordedFocusView = null;
        }
    }

    @Override
    public void recordKeyboardStatus(Window window) {
        final View focusView = window.getCurrentFocus();
        if (focusView == null) {
            return;
        }

        if (isKeyboardShowing) {
            saveFocusView(focusView);
        } else {
            focusView.clearFocus();
        }
    }

    private View recordedFocusView;

    private void saveFocusView(final View focusView) {
        recordedFocusView = focusView;
        focusView.clearFocus();
        panelLayout.setVisibility(View.GONE);
    }

    private void restoreFocusView() {
        panelLayout.setVisibility(View.INVISIBLE);
        MoorKeyboardUtil.showKeyboard(recordedFocusView);

    }
}
