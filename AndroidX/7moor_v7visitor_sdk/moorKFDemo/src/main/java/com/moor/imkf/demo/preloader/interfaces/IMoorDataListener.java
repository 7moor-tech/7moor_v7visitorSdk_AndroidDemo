package com.moor.imkf.demo.preloader.interfaces;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/19/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public interface IMoorDataListener<DATA> {
    /**
     * do something with loaded data
     * Note: this method runs in main-thread
     *
     * @param data loaded data (maybe null when load failed)
     */
    void onDataArrived(DATA data);
}
