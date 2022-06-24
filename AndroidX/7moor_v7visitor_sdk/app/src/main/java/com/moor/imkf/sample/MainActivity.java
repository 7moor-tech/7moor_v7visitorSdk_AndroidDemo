package com.moor.imkf.sample;

import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.moor.imkf.demo.helper.MoorOpenChatHelper;
import com.moor.imkf.demo.utils.statusbar.MoorStatusBarUtil;
import com.moor.imkf.lib.utils.MoorAntiShakeUtils;
import com.moor.imkf.moorsdk.bean.MoorEnumServiceType;
import com.moor.imkf.moorsdk.bean.MoorFastBtnBean;
import com.moor.imkf.moorsdk.manager.MoorConfiguration;
import com.moor.imkf.moorsdk.utils.toast.MoorToastUtils;
import com.moor.imkf.sample.imageloader.GlideImageLoader;

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
    private EditText etInputAccessid;

    /**
     * 在坐席后台创建的sdk渠道接入的accessid
     */
    private String accessid = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MoorStatusBarUtil.setTranslucentStatus(this);
        MoorStatusBarUtil.setStatusBarDarkTheme(this, true);
        etInputAccessid = findViewById(R.id.et_input_accessid);
        etInputAccessid.setText(accessid);
    }

    /**
     * 初始化SDK
     */
    public void initSdk(View view) {
        //防止快速重复点击
        if (MoorAntiShakeUtils.getInstance().check()) {
            return;
        }

        if (TextUtils.isEmpty(etInputAccessid.getText().toString())) {
            MoorToastUtils.showShort("请输入accessid");
            return;
        } else {
            accessid = etInputAccessid.getText().toString();
        }

        //配置SDK所需内容以及自定义配置
        MoorConfiguration configuration = new MoorConfiguration.MoorBuilder()
                //必填  设置初始化SDK所需
                // accessId
                .setAccessId(accessid)
                //必填 设置初始化访客的id (自定义，下方配置build.id为演示)
                .setUserId(Build.ID)
                //必填 设置初始化访客的昵称 (自定义，下方配置Build.MODEL为演示)
                .setUserName(Build.MODEL)
                //选填 接入别名
                .setUserAlias("别名Alias")
                //选填 设置访客头像
                .setUserHeadImg("https://dpic.tiankong.com/b7/jm/QJ6327498134.jpg?x-oss-process=style/794ws")
                //必填 sdk的服务地址类型
                .setServiceType(MoorEnumServiceType.T_REQUEST)
                //必填 设置图片加载器
                .setBaseImageLoader(new GlideImageLoader())
                //选配 设置sdk中一些错误code是否Toast提示 默认true
                .isShowSdkToast(true)
                //选配 设置是否打印Log 默认true
                .isLogOpen(true)
                //选配 设置是否将日志输出到文件 默认true
                .isLog2File(true)
                //添加消息列表中横向滑动快速按钮，样式功能可参考文档
//                .setFastBtnBeans(getFastBtn())
                .build();
        //初始化SDK
        MoorOpenChatHelper.getInstance().initSdk(configuration);
    }

    private final String[] imgs = new String[]{"https://v7-fs-im.7moor.com/1100480/im/20210622144141/1624344101865/8e4883a206314d5da623e7dd4fe684b1/%E6%A0%87%E7%AD%BE.png?imageView2/2/w/200/h/200",
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
        ArrayList<MoorFastBtnBean> btnBeanArrayList = new ArrayList<>();
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