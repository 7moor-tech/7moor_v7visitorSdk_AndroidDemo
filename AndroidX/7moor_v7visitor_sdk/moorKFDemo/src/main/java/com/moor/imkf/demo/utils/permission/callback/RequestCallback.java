package com.moor.imkf.demo.utils.permission.callback;


import java.util.List;


public interface RequestCallback {

    /**
     * Callback for the request result.
     * @param allGranted
     *          Indicate if all permissions that are granted.
     * @param grantedList
     *          All permissions that granted by user.
     * @param deniedList
     *          All permissions that denied by user.
     */
    void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList);

}
