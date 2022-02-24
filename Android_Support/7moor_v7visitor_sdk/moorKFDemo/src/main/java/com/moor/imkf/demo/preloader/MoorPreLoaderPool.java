package com.moor.imkf.demo.preloader;


import com.moor.imkf.demo.preloader.interfaces.IMoorDataListener;
import com.moor.imkf.demo.preloader.interfaces.IMoorDataLoader;
import com.moor.imkf.demo.preloader.interfaces.IMoorGroupedDataListener;
import com.moor.imkf.demo.preloader.interfaces.IMoorGroupedDataLoader;
import com.moor.imkf.demo.preloader.interfaces.IWorker;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;




/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/19/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public class MoorPreLoaderPool {

    private static class Inner {
        static final MoorPreLoaderPool INSTANCE = new MoorPreLoaderPool();
    }

    public static MoorPreLoaderPool getDefault() {
        return Inner.INSTANCE;
    }

    private final AtomicInteger idMaker = new AtomicInteger(0);

    private final ConcurrentHashMap<Integer, IWorker> workerMap = new ConcurrentHashMap<>();

    public <T> int preLoad(IMoorDataLoader<T> loader) {
        MoorWorker<T> worker = new MoorWorker<>(loader, (IMoorDataListener<T>) null);
        return preLoadWorker(worker);
    }

    public <T> int preLoad(IMoorDataLoader<T> loader, IMoorDataListener<T> listener) {
        MoorWorker<T> worker = new MoorWorker<>(loader, listener);
        return preLoadWorker(worker);
    }

    public <T> int preLoad(IMoorDataLoader<T> loader, List<IMoorDataListener<T>> listeners) {
        MoorWorker<T> worker = new MoorWorker<>(loader, listeners);
        return preLoadWorker(worker);
    }

    private <T> int preLoadWorker(MoorWorker<T> worker) {
        int id = idMaker.incrementAndGet();
        workerMap.put(id, worker);
        worker.preLoad();
        return id;
    }

    public int preLoadGroup(IMoorGroupedDataLoader... loaders) {
        int id = idMaker.incrementAndGet();
        MoorWorkerGroup group = new MoorWorkerGroup(loaders);
        workerMap.put(id, group);
        group.preLoad();
        return id;
    }

    public boolean exists(int id) {
        return workerMap.containsKey(id);
    }

    public boolean listenData(int id) {
        IWorker worker = workerMap.get(id);
        return worker != null && worker.listenData();
    }

    public <T> boolean listenData(int id, IMoorDataListener<T> dataListener) {
        try {
            IWorker worker = workerMap.get(id);
            return worker != null && worker.listenData(dataListener);
        } catch (Exception e) {
        }
        return false;
    }

    public boolean listenData(int id, IMoorGroupedDataListener... listeners) {
        try {
            IWorker worker = workerMap.get(id);
            if (worker != null) {
                for (IMoorGroupedDataListener listener : listeners) {
                    worker.listenData(listener);
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    public <T> boolean removeListener(int id, IMoorDataListener<T> dataListener) {
        try {
            IWorker worker = workerMap.get(id);
            return worker != null && worker.removeListener(dataListener);
        } catch (Exception e) {
        }
        return false;
    }

    public boolean refresh(int id) {
        IWorker worker = workerMap.get(id);
        return worker != null && worker.refresh();
    }

    public boolean destroy(int id) {
        IWorker worker = workerMap.remove(id);
        return worker != null && worker.destroy();
    }

    public boolean destroyAll() {
        for (IWorker worker : workerMap.values()) {
            if (worker != null) {
                try {
                    worker.destroy();
                } catch (Exception e) {
                }
            }
        }
        workerMap.clear();
        idMaker.set(0);
        return true;
    }

}
