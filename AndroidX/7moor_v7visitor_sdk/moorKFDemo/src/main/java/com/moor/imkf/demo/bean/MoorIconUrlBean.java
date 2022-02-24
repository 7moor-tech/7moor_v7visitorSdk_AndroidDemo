package com.moor.imkf.demo.bean;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2021/06/17
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public class MoorIconUrlBean {
    private String moreUrl;
    private String moreRotateUrl;
    private String emojiUrl;
    private String keyboardUrl;

    public String getMoreUrl() {
        return moreUrl;
    }

    public MoorIconUrlBean setMoreUrl(String moreUrl) {
        this.moreUrl = moreUrl;
        return this;
    }

    public String getMoreRotateUrl() {
        return moreRotateUrl;
    }

    public MoorIconUrlBean setMoreRotateUrl(String moreRotateUrl) {
        this.moreRotateUrl = moreRotateUrl;
        return this;
    }

    public String getEmojiUrl() {
        return emojiUrl;
    }

    public MoorIconUrlBean setEmojiUrl(String emojiUrl) {
        this.emojiUrl = emojiUrl;
        return this;
    }

    public String getKeyboardUrl() {
        return keyboardUrl;
    }

    public MoorIconUrlBean setKeyboardUrl(String keyboardUrl) {
        this.keyboardUrl = keyboardUrl;
        return this;
    }
}
