package com.moor.imkf.demo.view.imageviewer.listener;

import android.app.Activity;
import android.view.View;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/23/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public interface OnBigImageClickListener {

    /**
     * 点击事件
     */
    void onClick(Activity activity, View view, int position);
}