package com.moor.imkf.demo.preloader.interfaces;



/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/19/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public interface IMoorState {

    /**
     * start to load data
     *
     * @return
     */
    boolean startLoad();

    /**
     * 销毁
     *
     * @return
     */
    boolean destroy();

    /**
     * start to listen data
     *
     * @return
     */
    boolean listenData();

    /**
     * start to listen data with a listener
     *
     * @param listener listener of data
     * @return true if success
     */
    boolean listenData(IMoorDataListener listener);

    /**
     * remove listener
     *
     * @param listener listener of data
     * @return true
     */
    boolean removeListener(IMoorDataListener listener);

    /**
     * data load finished
     *
     * @return
     */
    boolean dataLoadFinished();

    /**
     * re-load data for all {@link IMoorDataListener}
     *
     * @return success
     */
    boolean refresh();

    /**
     * name of the state
     *
     * @return state name
     */
    String name();
}
