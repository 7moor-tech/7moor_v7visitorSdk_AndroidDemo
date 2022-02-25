package com.moor.imkf.demo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.moor.imkf.demo.R;
import com.moor.imkf.demo.data.MoorEmojiPreLoader;
import com.moor.imkf.demo.data.MoorEvaluationsPreLoader;
import com.moor.imkf.demo.data.MoorIconDownloadPreLoader;
import com.moor.imkf.demo.data.MoorMsgPreLoader;
import com.moor.imkf.demo.fragment.MoorChatFragment;
import com.moor.imkf.demo.panel.MoorKeyBoardSharedPreferences;
import com.moor.imkf.demo.preloader.MoorPreLoader;
import com.moor.imkf.demo.utils.MoorScreenUtils;
import com.moor.imkf.demo.utils.statusbar.MoorStatusBarUtil;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/1/21
 *     @desc   : 会话页面
 *     @version: 1.0
 * </pre>
 */
public class MoorChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moor_fragment_chat);
        //存储屏幕高度
        int screenHeight = MoorScreenUtils.getFullActivityHeight(this);
        MoorKeyBoardSharedPreferences.saveScreenHeight(this, screenHeight);
        //设置状态栏
        MoorStatusBarUtil.setTranslucentStatus(this);
        if (!MoorStatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格,设置状态栏颜色为半透明
            MoorStatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.moor_color_449E9E9E));
        }
        initView();
    }

    private void initView() {
        //预加载数据
        int preLoaderId = MoorPreLoader.preLoad(
                new MoorEmojiPreLoader()
                , new MoorMsgPreLoader()
                , new MoorIconDownloadPreLoader()
                , new MoorEvaluationsPreLoader());

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.moor_chat, MoorChatFragment.newInstance(preLoaderId))
                .commit();
    }
}
