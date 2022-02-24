package com.moor.imkf.demo.utils.permission.callback;


import com.moor.imkf.demo.utils.permission.request.ExplainScope;

import java.util.List;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/1/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public interface ExplainReasonCallback {

    /**
     * Called when you should explain why you need these permissions.
     * @param scope
     *          Scope to show rationale dialog.
     * @param deniedList
     *          Permissions that you should explain.
     */
    void onExplainReason(ExplainScope scope, List<String> deniedList);

}
