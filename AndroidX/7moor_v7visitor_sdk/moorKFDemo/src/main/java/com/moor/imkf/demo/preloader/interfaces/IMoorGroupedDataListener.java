package com.moor.imkf.demo.preloader.interfaces;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/19/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public interface IMoorGroupedDataListener<DATA> extends IMoorDataListener<DATA> {
    /**
     * @return
     */
    String keyInGroup();
}
