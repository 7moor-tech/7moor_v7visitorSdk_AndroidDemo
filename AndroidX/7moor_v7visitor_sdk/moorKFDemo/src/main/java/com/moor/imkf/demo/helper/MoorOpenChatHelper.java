package com.moor.imkf.demo.helper;

import android.content.Intent;

import com.moor.imkf.demo.activity.MoorChatActivity;
import com.moor.imkf.demo.view.loading.MoorLoadingDialog;
import com.moor.imkf.moorsdk.bean.MoorEnumErrorCode;
import com.moor.imkf.moorsdk.events.MoorLoginOffEvent;
import com.moor.imkf.moorsdk.listener.IMoorInitListener;
import com.moor.imkf.moorsdk.manager.MoorConfiguration;
import com.moor.imkf.moorsdk.manager.MoorManager;
import com.moor.imkf.moorsdk.service.MoorSocketService;
import com.moor.imkf.moorsdk.utils.MoorEventBusUtil;
import com.moor.imkf.moorsdk.utils.MoorLogUtils;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2021/02/20
 *     @desc   : 初始化Helper
 *     @version: 1.0
 * </pre>
 */
public class MoorOpenChatHelper {

    private MoorLoadingDialog loadingDialog;


    private MoorOpenChatHelper() {
    }

    public static MoorOpenChatHelper getInstance() {
        return MoorOpenChatHelper.SingletonHolder.sInstance;
    }

    private static class SingletonHolder {
        private static final MoorOpenChatHelper sInstance = new MoorOpenChatHelper();
    }


    /**
     * 初始化sdk
     *
     * @param configuration
     */
    public void initSdk(MoorConfiguration configuration) {
        initSdk(configuration, null);
    }

    /**
     * 初始化sdk
     *
     * @param configuration
     * @param listener      可根据具体需求，参考{@link MoorOpenChatListener}自定义初始化回调
     */
    public void initSdk(final MoorConfiguration configuration, IMoorInitListener listener) {
        //        MoorPermissionUtil.checkPermission((AppCompatActivity) MoorActivityHolder.requireCurrentActivity(), new OnRequestCallback() {
//            @Override
//            public void requestSuccess() {
//        if (listener != null) {
//            MoorManager.getInstance().initMoorSdk(configuration, listener);
//        } else {
//            MoorOpenChatListener openChatListener = new MoorOpenChatListener();
//            MoorManager.getInstance().initMoorSdk(configuration, openChatListener);
//        }
//            }
//        }, MoorPermissionConstants.STORE);
        if (listener == null) {
            listener = new MoorOpenChatListener();
        }
        MoorManager.getInstance().initMoorSdk(configuration, listener);
    }

    /**
     * 初始化监听-示例
     */
    private class MoorOpenChatListener implements IMoorInitListener {

        @Override
        public void onInitStart() {
            //初始化开始，可以进行load弹窗等操作
            loadingDialog = MoorLoadingDialog.create()
                    .setCancellable(false);
            loadingDialog.show();
        }

        @Override
        public void onStartService() {
            MoorSocketService.startMoorSocketService(MoorActivityHolder.getCurrentActivity());
        }

        @Override
        public void onInitSuccess(String msg) {
            //初始化成功，进入咨询页面
            openChat();
            loadingDialog.dismissDialog();
        }

        @Override
        public void onInitFailed(MoorEnumErrorCode errorCode, String msg) {
            //初始化失败
            loadingDialog.dismissDialog();
            MoorEventBusUtil.post(new MoorLoginOffEvent());
            MoorLogUtils.e(errorCode.getMsg() + ":" + msg);
        }
    }

    /**
     * 开启会话页面
     */
    public void openChat() {
        Intent intent = new Intent();
        intent.setClass(MoorActivityHolder.requireCurrentActivity(), MoorChatActivity.class);
        MoorActivityHolder.requireCurrentActivity().startActivity(intent);

    }
}
