package com.moor.imkf.demo.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.moor.imkf.demo.panel.MoorKeyBoardSharedPreferences;
import com.moor.imkf.demo.utils.MoorScreenUtils;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/1/21
 *     @desc   : BaseActivity
 *     @version: 1.0
 * </pre>
 */
public class MoorBaseActivity extends AppCompatActivity {
    public Context mContext;
    public final String TAG = this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        //存储屏幕高度
        int screenHeight = MoorScreenUtils.getFullActivityHeight(this);
        MoorKeyBoardSharedPreferences.saveScreenHeight(this, screenHeight);
//        if (MoorSdkVersionUtil.over_19()) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }
    }
}