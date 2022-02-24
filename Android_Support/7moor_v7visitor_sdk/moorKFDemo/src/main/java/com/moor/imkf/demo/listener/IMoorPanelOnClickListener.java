package com.moor.imkf.demo.listener;

import com.moor.imkf.moorsdk.bean.MoorPanelBean;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/1/21
 *     @desc   : 更多-面板点击事件
 *     @version: 1.0
 * </pre>
 */
public interface IMoorPanelOnClickListener {
    /**
     * 面板点击
     * @param panelBean 更多面板的数据对象
     */
    void onPanelClickListener(MoorPanelBean panelBean);
}
