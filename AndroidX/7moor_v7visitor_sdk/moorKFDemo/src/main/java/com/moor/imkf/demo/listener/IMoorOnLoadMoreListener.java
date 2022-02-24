package com.moor.imkf.demo.listener;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 1/18/21
 *     @desc   : 加载更多监听
 *     @version: 1.0
 * </pre>
 */
public interface IMoorOnLoadMoreListener {
    /**
     *  加载更多
     * @param page 页码
     */
    void onLoadMore(int page);
}
