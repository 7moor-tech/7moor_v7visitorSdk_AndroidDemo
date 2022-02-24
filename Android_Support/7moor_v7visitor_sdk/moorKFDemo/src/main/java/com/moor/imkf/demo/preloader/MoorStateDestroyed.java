package com.moor.imkf.demo.preloader;


/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/19/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
class MoorStateDestroyed extends MoorStateBase {
    MoorStateDestroyed(MoorWorker<?> worker) {
        super(worker);
    }

    @Override
    public boolean destroy() {
        return false;
    }

    @Override
    public String name() {
        return "StateDestroyed";
    }
}
