package com.moor.imkf.demo.preloader;

import android.text.TextUtils;

import com.moor.imkf.demo.preloader.interfaces.IMoorDataListener;
import com.moor.imkf.demo.preloader.interfaces.IMoorGroupedDataListener;
import com.moor.imkf.demo.preloader.interfaces.IMoorGroupedDataLoader;
import com.moor.imkf.demo.preloader.interfaces.IWorker;
import com.moor.imkf.moorsdk.utils.MoorLogUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;


/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/19/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
class MoorWorkerGroup implements IWorker {
    private final Collection<MoorWorker> workers;

    MoorWorkerGroup(IMoorGroupedDataLoader[] loaders) {
        if (loaders != null) {
            HashMap<String, MoorWorker> map = new HashMap<>(loaders.length);
            for (IMoorGroupedDataLoader loader : loaders) {
                String key = loader.keyInGroup();
                if (TextUtils.isEmpty(key)) {
                    MoorLogUtils.w("GroupedDataLoader with no key:"
                            + loader.getClass().getName());
                }
                MoorWorker old = map.put(key, new MoorWorker(loader, (IMoorDataListener) null));
                if (old != null) {
                    MoorLogUtils.e("More than 1 loaders with same key:("
                            + loader.getClass().getName()
                            + ", " + old.getClass().getName()
                            + "). " + old.getClass().getName() + " will be skipped."
                    );
                }
            }
            this.workers = map.values();
        } else {
            this.workers = new ArrayList<>();
        }
    }

    @Override
    public void setThreadPoolExecutor(ExecutorService threadPoolExecutor) {
        for (MoorWorker worker : workers) {
            worker.setThreadPoolExecutor(threadPoolExecutor);
        }
    }

    @Override
    public boolean preLoad() {
        boolean success = true;
        for (MoorWorker worker : workers) {
            success &= worker.preLoad();
        }
        return success;
    }

    @Override
    public boolean refresh() {
        boolean success = true;
        for (MoorWorker worker : workers) {
            success &= worker.refresh();
        }
        return success;
    }

    @Override
    public boolean listenData(IMoorDataListener dataListener) {
        boolean success = true;
        String key = null;
        if (dataListener != null && dataListener instanceof IMoorGroupedDataListener) {
            key = ((IMoorGroupedDataListener) dataListener).keyInGroup();
        }
        for (MoorWorker worker : workers) {
            if (!TextUtils.isEmpty(key) && worker.dataLoader instanceof IMoorGroupedDataLoader) {
                IMoorGroupedDataLoader loader = (IMoorGroupedDataLoader) worker.dataLoader;
                if (key.equals(loader.keyInGroup())) {
                    success &= worker.listenData(dataListener);
                }
            }
        }
        return success;
    }

    @Override
    public boolean listenData() {
        boolean success = true;
        for (MoorWorker worker : workers) {
            success &= worker.listenData();
        }
        return success;
    }

    @Override
    public boolean removeListener(IMoorDataListener listener) {
        boolean success = true;
        for (MoorWorker worker : workers) {
            success &= worker.removeListener(listener);
        }
        return success;
    }

    @Override
    public boolean destroy() {
        boolean success = true;
        for (MoorWorker worker : workers) {
            success &= worker.destroy();
        }
        workers.clear();
        return success;
    }
}
