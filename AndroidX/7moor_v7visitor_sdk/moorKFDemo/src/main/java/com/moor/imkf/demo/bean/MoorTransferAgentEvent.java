package com.moor.imkf.demo.bean;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2021/06/22
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public class MoorTransferAgentEvent {
    private String key;

    public MoorTransferAgentEvent(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public MoorTransferAgentEvent setKey(String key) {
        this.key = key;
        return this;
    }
}
