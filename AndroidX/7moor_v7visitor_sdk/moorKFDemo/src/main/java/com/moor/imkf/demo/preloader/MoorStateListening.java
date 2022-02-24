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
class MoorStateListening extends MoorStateBase {
    MoorStateListening(MoorWorker<?> worker) {
        super(worker);
    }

    @Override
    public boolean dataLoadFinished() {
        super.dataLoadFinished();
        return worker.doSendLoadedDataToListenerWork();
    }

    @Override
    public boolean listenData(IMoorDataListener listener) {
        super.listenData(listener);
        return worker.doAddListenerWork(listener);
    }

    @Override
    public String name() {
        return "StateListening";
    }
}
