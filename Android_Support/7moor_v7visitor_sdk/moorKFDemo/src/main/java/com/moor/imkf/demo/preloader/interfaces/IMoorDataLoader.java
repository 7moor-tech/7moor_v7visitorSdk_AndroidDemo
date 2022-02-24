package com.moor.imkf.demo.preloader.interfaces;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/19/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public interface IMoorDataLoader<DATA> {

    /**
     * pre-load loaded data
     * Note: this method will runs in thread pool,
     *
     * @return load result data (maybe null when load failed)
     */
    DATA loadData();
}
