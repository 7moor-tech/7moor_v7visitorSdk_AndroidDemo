package com.moor.imkf.demo.preloader;


import com.moor.imkf.demo.preloader.interfaces.IMoorDataListener;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/19/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
class MoorStateLoading extends MoorStateBase {
    MoorStateLoading(MoorWorker<?> worker) {
        super(worker);
    }

    /**
     * ready for get data
     *
     * @return true: {@link IMoorDataListener#onDataArrived(Object)} will be called
     * false: {@link MoorWorker} has no {@link IMoorDataListener}
     */
    @Override
    public boolean listenData() {
        super.listenData();
        return worker.doWaitForDataLoaderWork();
    }

    @Override
    public boolean listenData(IMoorDataListener listener) {
        super.listenData(listener);
        return worker.doWaitForDataLoaderWork(listener);
    }

    /**
     * data has loaded, waiting for {@link IMoorDataListener}
     *
     * @return true
     */
    @Override
    public boolean dataLoadFinished() {
        super.dataLoadFinished();
        return worker.doDataLoadFinishWork();
    }

    @Override
    public String name() {
        return "StateLoading";
    }
}
