package com.moor.imkf.demo.utils.permission.callback;


import com.moor.imkf.demo.utils.permission.request.ForwardScope;

import java.util.List;


public interface ForwardToSettingsCallback {

    /**
     * Called when you should tell user to allow these permissions in settings.
     * @param scope
     *          Scope to show rationale dialog.
     * @param deniedList
     *          Permissions that should allow in settings.
     */
    void onForwardToSettings(ForwardScope scope, List<String> deniedList);

}