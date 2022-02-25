package com.moor.imkf.demo.view.loading;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.moor.imkf.demo.R;
import com.moor.imkf.moorsdk.manager.MoorActivityHolder;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 1/26/21
 *     @desc   : LoadingDialog
 *     @version: 1.0
 * </pre>
 */
public class MoorLoadingDialog {

    private ProgressDialog mProgressDialog;
    private final float mDimAmount;
    private boolean mCancellable;

    public MoorLoadingDialog(Context context) {
        mProgressDialog = new ProgressDialog(context);
        mDimAmount = 0;
    }


    public static MoorLoadingDialog create() {
        return new MoorLoadingDialog(MoorActivityHolder.requireCurrentActivity());
    }

    /**
     * 是否能通过返回键取消loading。默认为false
     *
     * @param isCancellable
     * @return
     */
    public MoorLoadingDialog setCancellable(boolean isCancellable) {
        mCancellable = isCancellable;
        return this;
    }

    public MoorLoadingDialog show() {
        if (!isShowing()) {
            mProgressDialog.show();
        }
        return this;
    }

    public boolean isShowing() {
        return mProgressDialog != null && mProgressDialog.isShowing();
    }

    public void dismissDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
//            mProgressDialog = null;
        }
    }

    private class ProgressDialog extends Dialog {
        public ProgressDialog(Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.moor_loading_dialog);

            Window window = getWindow();
            window.setBackgroundDrawable(new ColorDrawable(0));
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.dimAmount = mDimAmount;
            window.setAttributes(layoutParams);

            setCanceledOnTouchOutside(false);
            setCancelable(mCancellable);
        }
    }

}
