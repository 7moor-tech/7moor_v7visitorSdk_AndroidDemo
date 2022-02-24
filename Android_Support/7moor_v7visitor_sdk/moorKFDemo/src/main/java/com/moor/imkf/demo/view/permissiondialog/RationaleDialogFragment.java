package com.moor.imkf.demo.view.permissiondialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
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
public abstract class RationaleDialogFragment extends DialogFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            dismiss();
        }
    }

    abstract public @NonNull View getPositiveButton();


    abstract public @Nullable View getNegativeButton();

    abstract public @NonNull List<String> getPermissionsToRequest();

}