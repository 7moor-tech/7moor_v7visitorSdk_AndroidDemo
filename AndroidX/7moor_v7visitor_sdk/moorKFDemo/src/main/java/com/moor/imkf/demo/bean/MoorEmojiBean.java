package com.moor.imkf.demo.bean;

import java.util.List;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2021/05/25
 *     @desc   : 获取的emoji数据
 *     @version: 1.0
 * </pre>
 */
public class MoorEmojiBean {
    private String elvesFigureUrl;
    private List<MoorEmojiListBean> bookArray;
    private int emojiSize;
    private int columnNum;

    public String getElvesFigureUrl() {
        return elvesFigureUrl;
    }

    public List<MoorEmojiListBean> getBookArray() {
        return bookArray;
    }

    public int getEmojiSize() {
        return emojiSize;
    }

    public int getColumnNum() {
        return columnNum;
    }

}
