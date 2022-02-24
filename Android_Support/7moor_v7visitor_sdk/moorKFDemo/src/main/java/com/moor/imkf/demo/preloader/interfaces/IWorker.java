package com.moor.imkf.demo.preloader.interfaces;


import java.util.concurrent.ExecutorService;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/19/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public interface IWorker {

    /**
     * set thread-pool executor for current worker
     *
     * @param threadPoolExecutor thread-pool executor
     */
    void setThreadPoolExecutor(ExecutorService threadPoolExecutor);

    /**
     * start to load data
     */
    boolean preLoad();

    /**
     * refresh worker
     */
    boolean refresh();

    /**
     * start to listen data with {@link IMoorDataListener}
     *
     * @param dataListener {@link IMoorDataListener}
     */
    boolean listenData(IMoorDataListener dataListener);

    /**
     * start to listen data with no {@link IMoorDataListener}
     * you can add {@link IMoorDataListener} later
     */
    boolean listenData();

    /**
     * remove {@link IMoorDataListener} for worker
     *
     * @param listener {@link IMoorDataListener}
     */
    boolean removeListener(IMoorDataListener listener);

    /**
     * destroy this worker
     */
    boolean destroy();
}
