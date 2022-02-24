package com.moor.imkf.demo.preloader;

import android.os.Handler;
import android.os.Looper;

import com.moor.imkf.demo.preloader.interfaces.IMoorDataListener;
import com.moor.imkf.demo.preloader.interfaces.IMoorDataLoader;
import com.moor.imkf.demo.preloader.interfaces.IMoorState;
import com.moor.imkf.demo.preloader.interfaces.IWorker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;



/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/19/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
class MoorWorker<T> implements Runnable, IWorker {

    private static final ThreadFactory FACTORY = new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("pre-loader-pool-" + thread.getId());
            return thread;
        }
    };

    private static ExecutorService defaultThreadPoolExecutor = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
            60L, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>(), FACTORY);

    private ExecutorService threadPoolExecutor;

    private T loadedData;
    private final List<IMoorDataListener<T>> dataListeners = new CopyOnWriteArrayList<>();
    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());
    IMoorDataLoader<T> dataLoader;
    private volatile IMoorState state;

    MoorWorker(IMoorDataLoader<T> loader, IMoorDataListener<T> listener) {
        init(loader);
        if (listener != null) {
            this.dataListeners.add(listener);
        }
    }

    MoorWorker(IMoorDataLoader<T> loader, List<IMoorDataListener<T>> listeners) {
        init(loader);
        if (listeners != null) {
            this.dataListeners.addAll(listeners);
        }
    }

    private void init(IMoorDataLoader<T> loader) {
        this.dataLoader = loader;
        setState(new MoorStatusInitialed(this));
    }

    static void setDefaultThreadPoolExecutor(ExecutorService threadPoolExecutor) {
        if (threadPoolExecutor != null) {
            defaultThreadPoolExecutor = threadPoolExecutor;
        }
    }

    @Override
    public void setThreadPoolExecutor(ExecutorService threadPoolExecutor) {
        if (threadPoolExecutor != null) {
            this.threadPoolExecutor = threadPoolExecutor;
        }
    }

    /**
     * start to load data
     */
    @Override
    public boolean preLoad() {
        return state.startLoad();
    }

    boolean doStartLoadWork() {
        if (threadPoolExecutor != null) {
            threadPoolExecutor.execute(this);
        } else {
            defaultThreadPoolExecutor.execute(this);
        }
        setState(new MoorStateLoading(this));
        return true;
    }

    @Override
    public boolean refresh() {
        return state.refresh();
    }

    @Override
    public boolean listenData(IMoorDataListener dataListener) {
        return state.listenData(dataListener);
    }

    @Override
    public boolean listenData() {
        return state.listenData();
    }

    @Override
    public boolean removeListener(IMoorDataListener listener) {
        return state.removeListener(listener);
    }

    boolean doRemoveListenerWork(IMoorDataListener<T> listener) {
        return dataListeners.remove(listener);
    }

    boolean doDataLoadFinishWork() {
        setState(new MoorStateLoadCompleted(this));
        return true;
    }

    boolean doSendLoadedDataToListenerWork() {
        return doSendLoadedDataToListenerWork(dataListeners);
    }

    boolean doAddListenerWork(IMoorDataListener<T> listener) {
        if (listener != null) {
            if (!this.dataListeners.contains(listener)) {
                this.dataListeners.add(listener);
            }
            return true;
        }
        return false;
    }

    boolean doSendLoadedDataToListenerWork(IMoorDataListener<T> listener) {
        doAddListenerWork(listener);
        List<IMoorDataListener<T>> listeners = null;
        if (listener != null) {
            listeners = new ArrayList<>(1);
            listeners.add(listener);
        }
        return doSendLoadedDataToListenerWork(listeners);
    }

    private boolean doSendLoadedDataToListenerWork(final List<IMoorDataListener<T>> listeners) {
        if (!(state instanceof MoorStateDone)) {
            setState(new MoorStateDone(this));
        }
        if (listeners != null && !listeners.isEmpty()) {
            if (isMainThread()) {
                safeListenData(listeners, loadedData);
            } else {
                mainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        safeListenData(listeners, loadedData);
                    }
                });
            }
        }
        return true;
    }

    boolean doWaitForDataLoaderWork(IMoorDataListener<T> listener) {
        if (listener != null) {
            dataListeners.add(listener);
        }
        return doWaitForDataLoaderWork();
    }

    /**
     * waiting for {@link IMoorDataLoader#loadData()} finish
     *
     * @return false if no {@link IMoorDataListener}, true otherwise
     */
    boolean doWaitForDataLoaderWork() {
        // change current state to StateListening
        setState(new MoorStateListening(this));
        return true;
    }

    @Override
    public boolean destroy() {
        return state.destroy();
    }

    boolean doDestroyWork() {
        setState(new MoorStateDestroyed(this));
        mainThreadHandler.removeCallbacksAndMessages(null);
        dataListeners.clear();
        dataLoader = null;
        threadPoolExecutor = null;
        return true;
    }

    /**
     * load data in thread-pool
     * if state is {@link MoorStateListening} : send data to {@link IMoorDataListener}
     * if state is {@link MoorStateLoading} : change state to {@link MoorStateLoadCompleted}
     */
    @Override
    public void run() {
        try {
            loadedData = null;
            //load data (from network or local i/o)
            loadedData = dataLoader.loadData();
        } catch (Exception e) {
        }
        state.dataLoadFinished();
    }

    private void safeListenData(List<IMoorDataListener<T>> listeners, T t) {
        for (IMoorDataListener<T> listener : listeners) {
            try {
                listener.onDataArrived(t);
            } catch (Exception e) {
            }
        }
    }

    private boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    private void setState(IMoorState state) {
        if (state != null) {
            if (this.state != null) {
                if (this.state.getClass() == state.getClass()) {
                    return;
                }
            }
            this.state = state;
        }
    }
}
