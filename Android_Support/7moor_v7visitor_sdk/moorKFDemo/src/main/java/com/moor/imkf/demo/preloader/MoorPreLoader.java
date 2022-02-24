package com.moor.imkf.demo.preloader;

import com.moor.imkf.demo.preloader.interfaces.IMoorDataListener;
import com.moor.imkf.demo.preloader.interfaces.IMoorDataLoader;
import com.moor.imkf.demo.preloader.interfaces.IMoorGroupedDataListener;
import com.moor.imkf.demo.preloader.interfaces.IMoorGroupedDataLoader;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/19/21
 *     @desc   : 数据预加载
 *     @version: 1.0
 * </pre>
 */
public class MoorPreLoader {

    /**
     * start a pre-loader and listen data in the future
     */
    public static <E> MoorPreLoaderWrapper<E> just(IMoorDataLoader<E> loader) {
        return just(loader, (IMoorDataListener<E>) null);
    }

    /**
     * start a pre-loader for {@link IMoorDataListener}
     * you can handle the {@link MoorPreLoaderWrapper} object to do whatever you want
     *
     * @param loader   data loader
     * @param listener data listener
     * @return {@link MoorPreLoaderWrapper}
     */
    public static <E> MoorPreLoaderWrapper<E> just(IMoorDataLoader<E> loader, IMoorDataListener<E> listener) {
        MoorPreLoaderWrapper<E> preLoader = new MoorPreLoaderWrapper<>(loader, listener);
        preLoad();
        return preLoader;
    }

    public static <E> MoorPreLoaderWrapper<E> just(IMoorDataLoader<E> loader, List<IMoorDataListener<E>> listeners) {
        MoorPreLoaderWrapper<E> preLoader = new MoorPreLoaderWrapper<>(loader, listeners);
        preLoad();
        return preLoader;
    }

    /**
     * set a custom thread pool for all pre-load tasks
     *
     * @param threadPoolExecutor thread pool
     */
    public static void setDefaultThreadPoolExecutor(ExecutorService threadPoolExecutor) {
        MoorWorker.setDefaultThreadPoolExecutor(threadPoolExecutor);
    }

    /**
     * provide a entrance for pre-load data in singleton {@link MoorPreLoaderPool} <br>
     *
     * @param loader    data loader
     * @param listeners listeners start work after both of loader.loadData() completed and listenData(id) called
     * @return id for this loader
     */
    public static <E> int preLoad(IMoorDataLoader<E> loader, List<IMoorDataListener<E>> listeners) {
        return MoorPreLoaderPool.getDefault().preLoad(loader, listeners);
    }

    public static <E> int preLoad(IMoorDataLoader<E> loader, IMoorDataListener<E> listener) {
        return MoorPreLoaderPool.getDefault().preLoad(loader, listener);
    }

    /**
     * provide a entrance for pre-load data in singleton {@link MoorPreLoaderPool} <br>
     *
     * @param loader data loader
     * @return id for this loader
     */
    public static <E> int preLoad(IMoorDataLoader<E> loader) {
        return MoorPreLoaderPool.getDefault().preLoad(loader);
    }

    /**
     * pre-load data with a group loaders in only one pre-load-id
     * eg. pre-load data in an activity with lots of {@link IMoorDataLoader}
     *
     * @param loaders
     * @return
     */
    public static int preLoad(IMoorGroupedDataLoader... loaders) {
        return MoorPreLoaderPool.getDefault().preLoadGroup(loaders);
    }

    /**
     * provide a entrance for create a new {@link MoorPreLoaderPool} object
     *
     * @return a new object of {@link MoorPreLoaderPool}
     */
    public static MoorPreLoaderPool newPool() {
        return new MoorPreLoaderPool();
    }

    /**
     * start to listen data by id<br>
     * 1. if pre-load task starts via {@link #preLoad(IMoorDataLoader, IMoorDataListener)} or {@link #preLoad(IMoorDataLoader, List)}
     * the listeners will be called when {@link IMoorDataLoader#loadData()} completed<br>
     * 2. if pre-load task starts via {@link #preLoad(IMoorDataLoader)},
     * and call this method without any {@link IMoorDataListener},
     * the pre-load only change the state to {@link MoorStateDone},
     * you can get data later by {@link #preLoad(IMoorDataLoader, IMoorDataListener)} and {@link #preLoad(IMoorDataLoader, List)}
     *
     * @param id the id returns by {@link #preLoad(IMoorDataLoader)}
     * @return success or not
     */
    public static boolean listenData(int id) {
        return MoorPreLoaderPool.getDefault().listenData(id);
    }

    /**
     * start to listen data with {@link IMoorDataListener} by id<br>
     *
     * @param id           the id returns by {@link #preLoad(IMoorDataLoader)}
     * @param dataListener the dataListener.onDataArrived(data) will be called if data load is completed
     * @return success or not
     * @see #listenData(int)  listenData(int) for more details
     */
    public static <T> boolean listenData(int id, IMoorDataListener<T> dataListener) {
        return MoorPreLoaderPool.getDefault().listenData(id, dataListener);
    }

    public static boolean listenData(int id, IMoorGroupedDataListener... listeners) {
        return MoorPreLoaderPool.getDefault().listenData(id, listeners);
    }

    /**
     * remove a specified {@link IMoorDataListener} for the pre-load task by id
     *
     * @param id           the id returns by {@link #preLoad(IMoorDataLoader)}
     * @param dataListener the listener to remove
     * @return success or not
     */
    public static <T> boolean removeListener(int id, IMoorDataListener<T> dataListener) {
        return MoorPreLoaderPool.getDefault().removeListener(id, dataListener);
    }

    /**
     * check the pre-load task is exists in singleton {@link MoorPreLoaderPool}
     *
     * @param id the id returns by {@link #preLoad(IMoorDataLoader)}
     * @return exists or not
     */
    public static boolean exists(int id) {
        return MoorPreLoaderPool.getDefault().exists(id);
    }

    /**
     * re-load data for all listeners
     *
     * @return success
     */
    public static boolean refresh(int id) {
        return MoorPreLoaderPool.getDefault().refresh(id);
    }

    /**
     * destroy the pre-load task by id.
     * call this method for remove loader and all listeners, and will not accept any listeners
     *
     * @param id the id returns by {@link #preLoad(IMoorDataLoader)}
     * @return success or not
     */
    public static boolean destroy(int id) {
        return MoorPreLoaderPool.getDefault().destroy(id);
    }

    /**
     * destroy all pre-load tasks in singleton {@link MoorPreLoaderPool}
     *
     * @return success or not
     */
    public static boolean destroyAll() {
        return MoorPreLoaderPool.getDefault().destroyAll();
    }
}
