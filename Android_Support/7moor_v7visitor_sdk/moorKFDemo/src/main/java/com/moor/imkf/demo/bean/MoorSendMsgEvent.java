package com.moor.imkf.demo.bean;

/**
 * @ClassName MoorSendMsgEvent
 * @Description 用于快速发送一条消息, 携带的内容可以自己在添加，注意确保原有逻辑的影响
 * @Author jiangbingxuan
 * @Date 2022/5/17 17:06
 * @Version 1.0
 */
public class MoorSendMsgEvent {

    private String content;


    public MoorSendMsgEvent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
