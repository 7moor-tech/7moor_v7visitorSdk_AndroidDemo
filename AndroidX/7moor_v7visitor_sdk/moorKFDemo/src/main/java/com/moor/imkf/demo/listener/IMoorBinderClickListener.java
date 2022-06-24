package com.moor.imkf.demo.listener;

import android.view.View;

import com.moor.imkf.demo.bean.MoorEnumChatItemClickType;
import com.moor.imkf.moorsdk.bean.MoorMsgBean;

/**
 * MoorMultiTypeAdapter点击事件
 */
public interface IMoorBinderClickListener {
    /**
     * 点击事件
     *
     * @param v    view
     * @param item 条目的MoorMsgBean
     * @param type    传入事件需要携带的对象，可用作判断或传值
     */
    void onClick(View v, MoorMsgBean item, MoorEnumChatItemClickType type);

    /**
     * 长按事件
     *
     * @param v    view
     * @param item 条目的MoorMsgBean
     * @param type    传入事件需要携带的对象，可用作判断或传值
     */
    void onLongClick(View v, MoorMsgBean item, MoorEnumChatItemClickType type);
}
