package com.moor.imkf.demo.utils.permission.request;

import android.support.annotation.NonNull;

import com.moor.imkf.demo.view.permissiondialog.RationaleDialog;
import com.moor.imkf.demo.view.permissiondialog.RationaleDialogFragment;

import java.util.List;


public class ForwardScope {

    private final PermissionBuilder pb;

    private final ChainTask chainTask;

    ForwardScope(PermissionBuilder pb, ChainTask chainTask) {
        this.pb = pb;
        this.chainTask = chainTask;
    }

    /**
     * Show a rationale dialog to tell user to allow these permissions in settings.
     * @param permissions
     *          Permissions that to request.
     * @param message
     *          Message that show to user.
     * @param positiveText
     *          Text on the positive button. When user click, PermissionX will forward to settings page of your app.
     * @param negativeText
     *          Text on the negative button. When user click, PermissionX will finish request.
     */
    public void showForwardToSettingsDialog(List<String> permissions, String message, String positiveText, String negativeText) {
        pb.showHandlePermissionDialog(chainTask, false, permissions, message, positiveText, negativeText);
    }

    /**
     * Show a rationale dialog to tell user to allow these permissions in settings.
     * @param permissions
     *          Permissions that to request.
     * @param message
     *          Message that show to user.
     * @param positiveText
     *          Text on the positive button. When user click, PermissionX will forward to settings page of your app.
     */
    public void showForwardToSettingsDialog(List<String> permissions, String message, String positiveText) {
        showForwardToSettingsDialog(permissions, message, positiveText, null);
    }

    /**
     * Show a rationale dialog to tell user to allow these permissions in settings.
     * @param dialog
     *          Dialog to explain to user why these permissions are necessary.
     */
    public void showForwardToSettingsDialog(@NonNull RationaleDialog dialog) {
        pb.showHandlePermissionDialog(chainTask, false, dialog);
    }

    /**
     * Show a rationale dialog to tell user to allow these permissions in settings.
     * @param dialogFragment
     *          DialogFragment to explain to user why these permissions are necessary.
     */
    public void showForwardToSettingsDialog(@NonNull RationaleDialogFragment dialogFragment) {
        pb.showHandlePermissionDialog(chainTask, false, dialogFragment);
    }
}
