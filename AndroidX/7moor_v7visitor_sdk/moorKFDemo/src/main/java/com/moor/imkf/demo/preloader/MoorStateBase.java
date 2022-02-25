package com.moor.imkf.demo.preloader;


import com.moor.imkf.demo.preloader.interfaces.IMoorDataListener;
import com.moor.imkf.demo.preloader.interfaces.IMoorState;
import com.moor.imkf.lib.utils.MoorLogUtils;


/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/19/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public abstract class MoorStateBase implements IMoorState {

    protected MoorWorker<?> worker;

    MoorStateBase(MoorWorker<?> worker) {
        this.worker = worker;
    }

    @Override
    public boolean destroy() {
        log("destroy");
        if (this instanceof MoorStateDestroyed) {
            return false;
        } else {
            return worker.doDestroyWork();
        }
    }

    @Override
    public boolean startLoad() {
        log("startLoad()");
        return false;
    }

    @Override
    public boolean listenData() {
        log("listenData()");
        return false;
    }

    @Override
    public boolean listenData(IMoorDataListener listener) {
        log("listenData(listener)");
        return false;
    }

    @Override
    public boolean removeListener(IMoorDataListener listener) {
        log("removeListener");
        return worker.doRemoveListenerWork(listener);
    }

    @Override
    public boolean dataLoadFinished() {
        log("dataLoadFinished()");
        return false;
    }

    @Override
    public boolean refresh() {
        log("refresh()");
        return false;
    }

    private void log(String str) {
        MoorLogUtils.d(name() + "--->>> " + str);
    }
}
