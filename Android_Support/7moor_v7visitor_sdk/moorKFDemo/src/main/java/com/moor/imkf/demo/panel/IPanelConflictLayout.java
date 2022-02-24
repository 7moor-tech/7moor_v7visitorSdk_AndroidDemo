package com.moor.imkf.demo.panel;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 1/19/21
 *     @desc   : 用于面板容器布局的界面，非全屏情况下使用主题窗口
 *     @version: 1.0
 * </pre>
 */
public interface IPanelConflictLayout {
    /**
     * 键盘是否显示
     *
     * @return
     */
    boolean isKeyboardShowing();

    /**
     * 可见与否的真实状态
     *
     * @return
     */
    boolean isVisible();

    /**
     * 键盘->面板
     */
    void handleShow();

    /**
     * 面板->键盘
     */
    void handleHide();

    /**
     * 忽略保证面板高度等于键盘的高度
     *
     * @param isIgnoreRecommendHeight
     */
    void setIgnoreRecommendHeight(boolean isIgnoreRecommendHeight);
}
