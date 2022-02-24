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
class MoorStateLoadCompleted extends MoorStateBase {
    MoorStateLoadCompleted(MoorWorker<?> worker) {
        super(worker);
    }

    @Override
    public boolean refresh() {
        super.refresh();
        return worker.doStartLoadWork();
    }

    @Override
    public boolean listenData() {
        super.listenData();
        return worker.doSendLoadedDataToListenerWork();
    }

    @Override
    public boolean listenData(IMoorDataListener listener) {
        super.listenData(listener);
        return worker.doSendLoadedDataToListenerWork(listener);
    }

    @Override
    public String name() {
        return "StateLoadCompleted";
    }
}
