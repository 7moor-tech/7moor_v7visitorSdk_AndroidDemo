package com.moor.imkf.demo.bean;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 6/1/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public enum MoorEnumChatItemClickType {
    /**
     * 满意度评价点击
     */
    TYPE_CSR_CLICK(""),
    /**
     * 附件点击
     */
    TYPE_FILE_CLICK(""),
    /**
     * flowList列表点击
     */
    TYPE_FLOW_LIST(""),
    /**
     * flowList 文字点击
     */
    TYPE_FLOW_LIST_TEXT(""),
    /**
     * flowList 多选点击
     */
    TYPE_FLOW_LIST_MULTI(""),
    /**
     * 点赞、点踩
     */
    TYPE_USEFUL(""),
    /**
     * xbot分组常见问题 点击发送
     */
    TYPE_XBOT_TABQUESTION_ITEM(""),
    /**
     * xbot分组常见问题 查看更多
     */
    TYPE_XBOT_TABQUESTION_MORE(""),
    /**
     * 物流节点查看更多
     */
    TYPE_LOGISTICS_MORE(""),
    /**
     * 消息重发
     */
    TYPE_RESEND_MSG(""),

    /**
     * fastList点击
     */
    TYPE_FAST_BTN_LIST(""),

    /**
     * 订单卡片点击事件
     * 分三种，中间商品部分点击，L按钮点击，R按钮点击
     */
    TYPE_ORDER_CARD_CLICK(""),

    /**
     * xbot quickmenu点击事件
     */
    TYPE_XBOT_QUICKMENU_CLICK("");

    private Object obj;

    MoorEnumChatItemClickType(Object... obj) {
        this.obj = obj;
    }

    public Object getObj() {
        return obj;
    }

    public MoorEnumChatItemClickType setObj(Object... obj) {
        this.obj = obj;
        return this;
    }
}
