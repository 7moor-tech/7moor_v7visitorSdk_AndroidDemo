package com.moor.imkf.demo.panel;

import android.app.Activity;
import android.view.View;

import com.moor.imkf.demo.utils.MoorViewUtil;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 1/19/21
 *     @desc   : 控制面板和键盘的非布局冲突
 *     @version: 1.0
 * </pre>
 */
public class MoorSwitchConflictUtil {

    private static boolean mIsInMultiWindowMode = false;

//    public static void attach(final View panelLayout,
//                              final View switchPanelKeyboardBtn,
//                              final View focusView) {
//        attach(panelLayout, switchPanelKeyboardBtn, focusView, null);
//    }
//
//    /**
//     * @param panelLayout            面板的布局
//     * @param switchPanelKeyboardBtn 该视图将用于触发面板和键盘之间的切换
//     * @param focusView              视图将被聚焦或失去焦点
//     * @param switchClickListener    点击监听器用于监听点击事件
//     */
//    public static void attach(final View panelLayout,
//                              final View switchPanelKeyboardBtn,
//                              final View focusView,
//                              final SwitchClickListener switchClickListener) {
//        final Activity activity = (Activity) panelLayout.getContext();
//
//        if (switchPanelKeyboardBtn != null) {
//            switchPanelKeyboardBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    final boolean switchToPanel = switchPanelAndKeyboard(panelLayout, focusView);
//                    if (switchClickListener != null) {
//                        switchClickListener.onClickSwitch(v, switchToPanel);
//                    }
//                }
//            });
//        }
//
//        if (isHandleByPlaceholder(activity)) {
//            focusView.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    if (event.getAction() == MotionEvent.ACTION_UP) {
//                        panelLayout.setVisibility(View.INVISIBLE);
//                    }
//                    return false;
//                }
//            });
//        }
//    }
//
//
//    public static void attach(final View panelLayout,
//                              final View focusView,
//                              SubPanelAndTrigger... subPanelAndTriggers) {
//        attach(panelLayout, focusView, null, subPanelAndTriggers);
//    }

    /**
     * @param panelLayout         面板的布局
     * @param focusView           focusView视图将被聚焦或失去焦点
     * @param switchClickListener 用于监听面板是显示与否;键盘显示时会切换面板/键盘状态。
     * @param subPanelAndTriggers 触发切换视图的数组和其绑定的子面板
     */
    public static void attach(final View panelLayout,
                              final View focusView,
                              /** Nullable **/final SwitchClickListener switchClickListener,
                              SubPanelAndTrigger... subPanelAndTriggers) {
        for (SubPanelAndTrigger subPanelAndTrigger : subPanelAndTriggers) {
            bindSubPanel(subPanelAndTrigger, subPanelAndTriggers,
                    focusView, panelLayout, switchClickListener);
        }
    }


    public static class SubPanelAndTrigger {

        final View subPanelView;

        final View triggerView;

        public SubPanelAndTrigger(View subPanelView, View triggerView) {
            this.subPanelView = subPanelView;
            this.triggerView = triggerView;
        }
    }

    /**
     * 显示面板
     *
     * @param panelLayout
     */
    public static void showPanel(final View panelLayout) {
        final Activity activity = (Activity) panelLayout.getContext();
        panelLayout.setVisibility(View.VISIBLE);
        if (activity.getCurrentFocus() != null) {
            MoorKeyboardUtil.hideKeyboard(activity.getCurrentFocus());
        }
    }


    /**
     * 显示键盘
     *
     * @param panelLayout
     * @param focusView
     */
    public static void showKeyboard(final View panelLayout, final View focusView) {
        final Activity activity = (Activity) panelLayout.getContext();

        MoorKeyboardUtil.showKeyboard(focusView);
        if (isHandleByPlaceholder(activity)) {
            panelLayout.setVisibility(View.INVISIBLE);
        } else if (mIsInMultiWindowMode) {
            panelLayout.setVisibility(View.GONE);
        }
    }

    public static boolean switchPanelAndKeyboard(final View panelLayout, final View focusView) {
        boolean switchToPanel = panelLayout.getVisibility() != View.VISIBLE;
        if (!switchToPanel) {
            showKeyboard(panelLayout, focusView);
        } else {
            showPanel(panelLayout);
        }

        return switchToPanel;
    }


    public static void hidePanelAndKeyboard(final View panelLayout) {
        final Activity activity = (Activity) panelLayout.getContext();

        final View focusView = activity.getCurrentFocus();
        if (focusView != null) {
            MoorKeyboardUtil.hideKeyboard(activity.getCurrentFocus());
            focusView.clearFocus();
        }

        panelLayout.setVisibility(View.GONE);
    }


    public interface SwitchClickListener {

        void onClickSwitch(View v, int switchToPanel);
    }

    public static boolean isHandleByPlaceholder(boolean isFullScreen, boolean isTranslucentStatus,
                                                boolean isFitsSystem) {
        return isFullScreen || (isTranslucentStatus && !isFitsSystem);
    }

    static boolean isHandleByPlaceholder(final Activity activity) {
        return isHandleByPlaceholder(MoorViewUtil.isFullScreen(activity),
                MoorViewUtil.isTranslucentStatus(activity), MoorViewUtil.isFitsSystemWindows(activity));
    }

    private static void bindSubPanel(final SubPanelAndTrigger subPanelAndTrigger,
                                     final SubPanelAndTrigger[] subPanelAndTriggers,
                                     final View focusView, final View panelLayout,
            /* Nullable */final SwitchClickListener switchClickListener) {

        final View triggerView = subPanelAndTrigger.triggerView;
        final View boundTriggerSubPanelView = subPanelAndTrigger.subPanelView;

        triggerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int switchToPanel;
                if (panelLayout.getVisibility() == View.VISIBLE) {
                    // panel is visible.

                    if (boundTriggerSubPanelView.getVisibility() == View.VISIBLE) {

                        ////面板正在显示，此时点击要显示键盘
                        // bound-trigger panel is visible.
                        // to show keyboard.
                        MoorSwitchConflictUtil.showKeyboard(panelLayout, focusView);
                        switchToPanel = 0;

                    } else {
                        // bound-trigger panel is invisible.
                        // to show bound-trigger panel.
                        //面板正在显示，此时是切换面板事件
                        showBoundTriggerSubPanel(boundTriggerSubPanelView, subPanelAndTriggers);
                        triggerView.setSelected(true);
                        switchToPanel = 1;
                    }
                } else {
                    // panel is gone.
                    // to show panel.
                    //面板要显示了
                    MoorSwitchConflictUtil.showPanel(panelLayout);
                    switchToPanel = 2;

                    // to show bound-trigger panel.
                    showBoundTriggerSubPanel(boundTriggerSubPanelView, subPanelAndTriggers);
                    triggerView.setSelected(true);
                }

                if (switchClickListener != null) {
                    switchClickListener.onClickSwitch(v, switchToPanel);
                }
            }
        });
    }

    private static void showBoundTriggerSubPanel(final View boundTriggerSubPanelView,
                                                 final SubPanelAndTrigger[] subPanelAndTriggers) {
        // to show bound-trigger panel.
        for (SubPanelAndTrigger panelAndTrigger : subPanelAndTriggers) {
            if (panelAndTrigger.subPanelView != boundTriggerSubPanelView) {
                // other sub panel.
                panelAndTrigger.subPanelView.setVisibility(View.GONE);
                panelAndTrigger.triggerView.setSelected(false);
            }
        }
        boundTriggerSubPanelView.setVisibility(View.VISIBLE);
    }

    public static void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        mIsInMultiWindowMode = isInMultiWindowMode;
    }

}
