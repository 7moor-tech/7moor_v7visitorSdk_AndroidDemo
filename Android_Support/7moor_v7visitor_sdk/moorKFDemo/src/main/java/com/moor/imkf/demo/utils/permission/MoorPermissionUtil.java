package com.moor.imkf.demo.utils.permission;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.moor.imkf.demo.utils.permission.callback.ExplainReasonCallbackWithBeforeParam;
import com.moor.imkf.demo.utils.permission.callback.ForwardToSettingsCallback;
import com.moor.imkf.demo.utils.permission.callback.OnRequestCallback;
import com.moor.imkf.demo.utils.permission.callback.RequestCallback;
import com.moor.imkf.demo.utils.permission.request.ExplainScope;
import com.moor.imkf.demo.utils.permission.request.ForwardScope;

import java.util.List;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/1/21
 *     @desc   : MoorPermissionX的封装工具类
 *     @version: 1.0
 * </pre>
 */
public class MoorPermissionUtil {

    public static void checkPermission(final FragmentActivity activity, final OnRequestCallback onRequestCallback, String... permission) {
        MoorPermissionX.init(activity)
                .permissions(permission)
                .onExplainRequestReason(new ExplainReasonCallbackWithBeforeParam() {
                    @Override
                    public void onExplainReason(ExplainScope scope, List<String> deniedList, boolean beforeRequest) {
                        scope.showRequestReasonDialog(deniedList, "为了保证程序正常工作，请您同意以下权限申请", "允许", "拒绝");
                    }
                })
                .onForwardToSettings(new ForwardToSettingsCallback() {
                    @Override
                    public void onForwardToSettings(ForwardScope scope, List<String> deniedList) {
                        scope.showForwardToSettingsDialog(deniedList, "您需要去应用程序设置当中手动开启以下权限", "确认", "取消");
                    }
                })
                .request(new RequestCallback() {
                    @Override
                    public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                        if (allGranted) {
                            if (onRequestCallback != null) {
                                onRequestCallback.requestSuccess();
                            }
                        } else {
//                            List<String> mList = new ArrayList<>();
//                            for (String item : deniedList) {
//                                mList.add(MoorPermissionConstants.getInstance().getPermissionName(item));
//                            }
//                            MoorToastUtils.showShort("您拒绝了如下权限：" + mList);
                        }
                    }
                });
    }
    public static void checkPermission(final Fragment fragment, final OnRequestCallback onRequestCallback, String... permission) {
        MoorPermissionX.init(fragment)
                .permissions(permission)
                .onExplainRequestReason(new ExplainReasonCallbackWithBeforeParam() {
                    @Override
                    public void onExplainReason(ExplainScope scope, List<String> deniedList, boolean beforeRequest) {
                        scope.showRequestReasonDialog(deniedList, "为了保证程序正常工作，请您同意以下权限申请", "允许", "拒绝");
                    }
                })
                .onForwardToSettings(new ForwardToSettingsCallback() {
                    @Override
                    public void onForwardToSettings(ForwardScope scope, List<String> deniedList) {
                        scope.showForwardToSettingsDialog(deniedList, "您需要去应用程序设置当中手动开启以下权限", "确认", "取消");
                    }
                })
                .request(new RequestCallback() {
                    @Override
                    public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                        if (allGranted) {
                            if (onRequestCallback != null) {
                                onRequestCallback.requestSuccess();
                            }
                        } else {
//                            List<String> mList = new ArrayList<>();
//                            for (String item : deniedList) {
//                                mList.add(MoorPermissionConstants.getInstance().getPermissionName(item));
//                            }
//                            MoorToastUtils.showShort("您拒绝了如下权限：" + mList);
                        }
                    }
                });
    }
}
