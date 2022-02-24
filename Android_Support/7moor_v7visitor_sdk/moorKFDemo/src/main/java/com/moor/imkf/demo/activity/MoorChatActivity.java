package com.moor.imkf.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.moor.imkf.demo.R;
import com.moor.imkf.demo.constans.MoorDemoConstants;
import com.moor.imkf.demo.data.MoorEmojiPreLoader;
import com.moor.imkf.demo.data.MoorEvaluationsPreLoader;
import com.moor.imkf.demo.data.MoorIconDownloadPreLoader;
import com.moor.imkf.demo.data.MoorMsgPreLoader;
import com.moor.imkf.demo.fragment.MoorChatFragment;
import com.moor.imkf.demo.preloader.MoorPreLoader;
import com.moor.imkf.demo.utils.MoorStatusBarUtil;
import com.moor.imkf.moorsdk.utils.MoorSdkVersionUtil;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/1/21
 *     @desc   : 会话页面
 *     @version: 1.0
 * </pre>
 */
public class MoorChatActivity extends MoorBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moor_fragment_chat);
        MoorStatusBarUtil.setTranslucentStatus(this);
        if (!MoorStatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格,设置状态栏颜色为半透明
            MoorStatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.moor_color_449E9E9E));
        }
        initView();
    }

    private void initView() {
        //添加 Chat聊天页面Fragment
        MoorChatFragment moorChatFragment = new MoorChatFragment();
        //预加载数据
        int preLoaderId = MoorPreLoader.preLoad(
                new MoorEmojiPreLoader()
                , new MoorMsgPreLoader()
                , new MoorIconDownloadPreLoader()
                , new MoorEvaluationsPreLoader());

        Bundle bundle = new Bundle();
        bundle.putInt(MoorDemoConstants.MOOR_PRE_LOADER_ID, preLoaderId);
        moorChatFragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.moor_framechat, moorChatFragment)
                .commit();
    }


    @Override
    public void onBackPressed() {
        if (MoorSdkVersionUtil.over_29()) {
            finishAfterTransition();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


}
