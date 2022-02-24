package com.moor.imkf.demo.preloader;


/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/19/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
class MoorStatusInitialed extends MoorStateBase {

    MoorStatusInitialed(MoorWorker<?> worker) {
        super(worker);
    }

    @Override
    public boolean startLoad() {
        super.startLoad();
        return worker.doStartLoadWork();
    }

    @Override
    public String name() {
        return "StatusInitialed";
    }
}
