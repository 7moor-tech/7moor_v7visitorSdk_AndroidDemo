package com.moor.imkf.demo.view.permissiondialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.List;
/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/1/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public abstract class RationaleDialog extends Dialog {

    public RationaleDialog(@NonNull Context context) {
        super(context);
    }

    public RationaleDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected RationaleDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    abstract public @NonNull View getPositiveButton();


    abstract public @Nullable View getNegativeButton();


    abstract public @NonNull List<String> getPermissionsToRequest();

}