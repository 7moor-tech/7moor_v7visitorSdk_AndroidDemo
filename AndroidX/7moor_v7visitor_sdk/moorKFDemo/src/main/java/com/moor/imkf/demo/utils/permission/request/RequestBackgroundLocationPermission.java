package com.moor.imkf.demo.utils.permission.request;

import android.Manifest;

import com.moor.imkf.demo.utils.permission.MoorPermissionX;

import java.util.ArrayList;
import java.util.List;


public class RequestBackgroundLocationPermission extends BaseTask {

    /**
     * Define the const to compat with system lower than Q.
     */
    public static final String ACCESS_BACKGROUND_LOCATION = "android.permission.ACCESS_BACKGROUND_LOCATION";

    RequestBackgroundLocationPermission(PermissionBuilder permissionBuilder) {
        super(permissionBuilder);
    }

    @Override
    public void request() {
        if (pb.requireBackgroundLocationPermission) {
            boolean accessFindLocationGranted = MoorPermissionX.isGranted(pb.activity, Manifest.permission.ACCESS_FINE_LOCATION);
            boolean accessCoarseLocationGranted = MoorPermissionX.isGranted(pb.activity, Manifest.permission.ACCESS_COARSE_LOCATION);
            if (accessFindLocationGranted || accessCoarseLocationGranted) {
                if (pb.explainReasonCallback != null || pb.explainReasonCallbackWithBeforeParam != null) {
                    List<String> requestList = new ArrayList<>();
                    requestList.add(ACCESS_BACKGROUND_LOCATION);
                    if (pb.explainReasonCallbackWithBeforeParam != null) {
                        // callback ExplainReasonCallbackWithBeforeParam prior to ExplainReasonCallback
                        pb.explainReasonCallbackWithBeforeParam.onExplainReason(explainReasonScope, requestList, true);
                    } else {
                        pb.explainReasonCallback.onExplainReason(explainReasonScope, requestList);
                    }
                } else {
                    // no implementation of explainReasonCallback, so we have to request ACCESS_BACKGROUND_LOCATION without explanation.
                    requestAgain(null);
                }
                return;
            }
        }
        // shouldn't request ACCESS_BACKGROUND_LOCATION at this time, so we call onResult() to finish this task.
        finish();
    }

    @Override
    public void requestAgain(List<String> permissions) {
        // don't care what the permissions param is, always request ACCESS_BACKGROUND_LOCATION
        pb.requestAccessBackgroundLocationNow(this);
    }

}
