package com.moor.imkf.demo.preloader.interfaces;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/19/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public interface IMoorGroupedDataLoader<DATA> extends IMoorDataLoader<DATA> {
    /**
     * key of this data-loader in the group
     * should be unique
     *
     * @return unique key in the group
     */
    String keyInGroup();
}
