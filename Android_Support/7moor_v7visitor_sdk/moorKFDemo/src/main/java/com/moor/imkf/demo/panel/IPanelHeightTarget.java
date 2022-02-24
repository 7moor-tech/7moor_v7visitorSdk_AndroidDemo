package com.moor.imkf.demo.panel;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 1/19/21
 *     @desc   : 为了使键盘的高度尽可能地与面板高度对齐
 *     @version: 1.0
 * </pre>
 */
public interface IPanelHeightTarget {

    /**
     * 用于处理面板的高度，等于上次保存的键盘高度
     */
    void refreshHeight(int panelHeight);

    /**
     * @return 获取目标视图的高度
     */

    int getHeight();

    /**
     * 由onGlobalLayoutListener回调调用
     *
     * @param showing 键盘是否显示
     */
    void onKeyboardShowing(boolean showing);
}
