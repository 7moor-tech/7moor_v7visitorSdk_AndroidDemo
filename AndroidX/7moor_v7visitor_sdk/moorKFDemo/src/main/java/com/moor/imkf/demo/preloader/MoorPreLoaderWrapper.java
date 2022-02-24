package com.moor.imkf.demo.preloader;

import com.moor.imkf.demo.preloader.interfaces.IMoorDataListener;
import com.moor.imkf.demo.preloader.interfaces.IMoorDataLoader;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/19/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public class MoorPreLoaderWrapper<T> {

    private final MoorWorker<T> worker;

    MoorPreLoaderWrapper(IMoorDataLoader<T> loader, IMoorDataListener<T> listener) {
        this.worker = new MoorWorker<>(loader, listener);
    }

    MoorPreLoaderWrapper(IMoorDataLoader<T> loader, List<IMoorDataListener<T>> listeners) {
        this.worker = new MoorWorker<>(loader, listeners);
    }

    boolean preLoad() {
        return this.worker.preLoad();
    }

    public void setThreadPoolExecutor(ExecutorService threadPoolExecutor) {
        worker.setThreadPoolExecutor(threadPoolExecutor);
    }

    /**
     * start to listen data with this dataListener
     *
     * @return true: {@link IMoorDataListener#onDataArrived(Object)} will be called<br>
     * false: there is no {@link IMoorDataListener}
     */
    public boolean listenData() {
        return worker.listenData();
    }

    /**
     * start to listen data with this dataListener
     *
     * @param dataListener listener
     * @return true: {@link IMoorDataListener#onDataArrived(Object)} will be called<br>
     * false: there is no {@link IMoorDataListener}
     */
    public boolean listenData(IMoorDataListener<T> dataListener) {
        return worker.listenData(dataListener);
    }

    public boolean removeListener(IMoorDataListener<T> dataListener) {
        return worker.removeListener(dataListener);
    }

    /**
     * re-load data for all listeners
     *
     * @return success
     */
    public boolean refresh() {
        return worker.refresh();
    }

    public boolean destroy() {
        return worker.destroy();
    }
}
