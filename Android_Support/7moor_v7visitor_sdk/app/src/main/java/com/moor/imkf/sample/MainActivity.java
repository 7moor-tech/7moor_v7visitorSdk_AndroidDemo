package com.moor.imkf.sample;

import android.app.Application;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.moor.imkf.demo.helper.MoorActivityHolder;
import com.moor.imkf.demo.helper.MoorOpenChatHelper;
import com.moor.imkf.demo.utils.MoorAntiShakeUtils;
import com.moor.imkf.demo.utils.MoorStatusBarUtil;
import com.moor.imkf.moorsdk.bean.MoorEnumServiceType;
import com.moor.imkf.moorsdk.bean.MoorFastBtnBean;
import com.moor.imkf.moorsdk.bean.MoorOptions;
import com.moor.imkf.moorsdk.manager.MoorConfiguration;
import com.moor.imkf.moorsdk.manager.MoorImageLoader;
import com.moor.imkf.moorsdk.utils.toast.MoorToastUtils;
import com.moor.imkf.sample.imageloader.GlideImageLoader;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/1/21
 *     @desc   : 示例Demo
 *     @version: 1.0
 * </pre>
 */
public class MainActivity extends AppCompatActivity {
    private EditText et_input_accessid;

    //在坐席后台创建的sdk渠道接入的Accessid
    private String accessid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MoorStatusBarUtil.setTranslucentStatus(this);
        MoorStatusBarUtil.setStatusBarDarkTheme(this, true);
        et_input_accessid = findViewById(R.id.et_input_accessid);
        et_input_accessid.setText(accessid);
    }


    /**
     * 初始化SDK
     */
    public void initSdk(View view) {
        //防止快速重复点击
        if (MoorAntiShakeUtils.getInstance().check()) {
            return;
        }

        if (!TextUtils.isEmpty(et_input_accessid.getText().toString())) {
            accessid = et_input_accessid.getText().toString();
        } else {
            MoorToastUtils.showShort("请输入accessid");
            return;
        }


        //UI配置参数
        MoorOptions options = setMoorOptions();
        MoorImageLoader loader = setMoorImageLoader();
        //配置SDK所需内容以及自定义配置
        MoorConfiguration configuration = new MoorConfiguration.MoorBuilder()
                //必填  设置初始化SDK所需
                // accessId
                .setAccessId(accessid)
                //必填 设置初始化访客的id
                .setUserId(Settings.System.getString(getContentResolver(), Settings.Secure.ANDROID_ID))
                //必填 设置初始化访客的昵称
                .setUserName(Settings.System.getString(getContentResolver(), Settings.Secure.ANDROID_ID))
                //选填 设置访客头像
                .setUserHeadImg("https://dpic.tiankong.com/b7/jm/QJ6327498134.jpg?x-oss-process=style/794ws")
                //必填 sdk的服务地址类型
                .setServiceType(MoorEnumServiceType.T_REQUEST)
                //必填 设置图片加载器
                .setBaseImageLoader(loader)
                //选配 设置sdk中一些错误code是否Toast提示 默认true
                .isShowSdkToast(true)
                //选配 设置是否打印Log 默认true
                .isLogOpen(true)
                //选配 设置是否将日志输出到文件 默认true
                .isLog2File(true)
                //选配 设置UI页面颜色、图片等
                .setOptions(options)
                //添加消息列表中横向滑动快速按钮，样式功能可参考文档
//                .setFastBtnBeans(getFastBtn())
                .build();
        //初始化SDK
        MoorOpenChatHelper.getInstance().initSdk(configuration);
    }

    /**
     * UI配置参数
     *
     * @return MoorOptions
     */
    private MoorOptions setMoorOptions() {
        MoorOptions options = new MoorOptions();
//        options.setSdkMainThemeColor(getResources().getColor(R.color.moor_color_f8624f))
//                .setTitleBarBackgroundColor(getResources().getColor(R.color.moor_color_eeeeee))
//                .setLeftTitleBtnColor(getResources().getColor(R.color.moor_color_eeeeee))
//                .setTransferSeatsBtnColor(getResources().getColor(R.color.moor_color_515151))
//                .setcloseSessionButtonColor(getResources().getColor(R.color.moor_color_515151))
//                .setTitleColor(getResources().getColor(R.color.moor_color_515151))
//                .setLogoutButtonResource(getResources().getDrawable(R.drawable.moor_icon_back))
//                .setArtificialButtonResource(getResources().getDrawable(R.drawable.moor_icon_artificial_black))
//                .setArtificialButtonText("转人工")
//                .setCloseSessionButtonResourse(getResources().getDrawable(R.drawable.moor_delete))
//                .setCloseSessionButtonText("结束会话")
//                .setChatInputHint("请输入文字")
//                .setTopTipText("工作时间为8：00-23：00")
//                .setAutoShowKeyboard(false)
//                .setShowEmojiButton(true)
//                .setShowVoiceButton(true)
//                .setShowHeadPortrait(true)
//                .setShowPanelButton(true)
//                .setHeadPortraitCircle(false)
//                .setHeadPortraitRadius(5)
//                .setUseSystemKeyboardSendAction(false)
//                .setChatBackgroundColor(getResources().getColor(R.color.moor_color_eeeeee))
////                .setChatBackgroundResource(getResources().getDrawable(R.drawable.moor_bg_chat_1))
//                .setTitleBarBackgroundResource(getResources().getDrawable(R.drawable.moor_bg_navigationbar));
        return options;
    }

    /**
     * 设置图片加载器
     *
     * @return
     */
    private MoorImageLoader setMoorImageLoader() {
        MoorImageLoader loader = new MoorImageLoader();
        //必填 设置图片加载器
        loader.setMoorImageLoader(new GlideImageLoader());
        return loader;
    }


    private String[] imgs = new String[]{"https://v7-fs-im.7moor.com/1100480/im/20210622144141/1624344101865/8e4883a206314d5da623e7dd4fe684b1/%E6%A0%87%E7%AD%BE.png?imageView2/2/w/200/h/200",
            "https://v7-fs-im.7moor.com/1100480/im/20210622144138/1624344098217/db7c3728bee04dae9ce89f8b73042231/%E7%A4%BC%E5%93%81.png?imageView2/2/w/200/h/200",
            "https://v7-fs-im.7moor.com/1100480/im/20210622144133/1624344093778/a06c300f36a345dcae5f5d74e720b9a9/%E4%BC%9A%E5%91%98%E5%8D%A1.png?imageView2/2/w/200/h/200",
            "https://v7-fs-im.7moor.com/1100480/im/20210622144125/1624344085919/a6e258cdc62e4b6197faa62759d66f16/%E4%BC%98%E6%83%A0%E5%88%B8.png?imageView2/2/w/200/h/200",
            "https://v7-fs-im.7moor.com/1100480/im/20210622144121/1624344081904/80820af657ba48b4a464dd13eb41d647/%E9%80%89%E9%A1%B9.png?imageView2/2/w/200/h/200",
            "https://v7-fs-im.7moor.com/1100480/im/20210622144117/1624344077200/9be7460d081b4a85ae4795bc8e207471/%E4%BB%BB%E5%8A%A1%E6%9F%A5%E8%AF%A2.png?imageView2/2/w/200/h/200"
    };

    /**
     * 模拟添加横向滑动按钮 数据
     *
     * @return
     */
    private ArrayList<MoorFastBtnBean> getFastBtn() {
        ArrayList<MoorFastBtnBean> btnBeanArrayList = new ArrayList<MoorFastBtnBean>();
        for (int i = 0; i < imgs.length; i++) {
            MoorFastBtnBean moorFastBtnBean = new MoorFastBtnBean();
            moorFastBtnBean.setImgUrl(imgs[i]);
            moorFastBtnBean.setShowName("订单查询" + i);
            moorFastBtnBean.setClickText("订单查询" + i);
            moorFastBtnBean.setBgColor("#EE6C09");
            btnBeanArrayList.add(moorFastBtnBean);
        }
        return btnBeanArrayList;
    }

}