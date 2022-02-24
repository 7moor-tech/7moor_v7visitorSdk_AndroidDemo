package com.moor.imkf.demo.helper;


import com.moor.imkf.demo.bean.MoorUploadBean;
import com.moor.imkf.demo.utils.MoorFileUtils;
import com.moor.imkf.moorhttp.MoorHttpParams;
import com.moor.imkf.moorhttp.MoorHttpUtils;
import com.moor.imkf.moorhttp.MoorUrlManager;
import com.moor.imkf.moorhttp.callback.MoorBaseCallBack;
import com.moor.imkf.moorsdk.bean.MoorNetBaseBean;
import com.moor.imkf.moorsdk.listener.IMoorUploadCallBackListener;
import com.moor.imkf.moorsdk.utils.MoorLogUtils;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONObject;

import okhttp3.Call;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 5/28/21
 *     @desc   : 上传文件
 *     @version: 1.0
 * </pre>
 */
public class MoorQiNiuUploadHelper {

    private MoorQiNiuUploadHelper() {
    }

    public static MoorQiNiuUploadHelper getInstance() {
        return MoorQiNiuUploadHelper.SingletonHolder.sInstance;
    }

    private static class SingletonHolder {
        private static final MoorQiNiuUploadHelper sInstance = new MoorQiNiuUploadHelper();
    }

    public void uploadFile(final String filePath, final IMoorUploadCallBackListener uploadCallBack) {
        String fileName = MoorFileUtils.getFileName(filePath);
        MoorHttpUtils.post()
                .url(MoorUrlManager.BASE_URL + MoorUrlManager.GET_TOKEN)
                .params(MoorHttpParams.getInstance().getToken(fileName))
                .build().execute(new MoorBaseCallBack<MoorNetBaseBean<MoorUploadBean>>() {
            @Override
            public void onSuccess(MoorNetBaseBean<MoorUploadBean> result, int id) {
                if (result.isSuccess()) {
                    dealUpload(filePath, result.getData(), uploadCallBack);
                } else {
                    if (uploadCallBack != null) {
                        uploadCallBack.onUploadFailed();
                    }
                }
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                if (uploadCallBack != null) {
                    uploadCallBack.onUploadFailed();
                }
            }
        });
    }

    private void dealUpload(String filePath, final MoorUploadBean data, final IMoorUploadCallBackListener uploadCallBack) {
        Configuration.Builder builder = new Configuration.Builder().putThreshhold(200 * 1024 * 1024);
        Configuration configuration = builder.build();
        UploadManager uploadManager = new UploadManager(configuration);
        String scope = data.getScope();
        final String subScope = scope.substring(scope.indexOf(":") + 1, scope.length());
        uploadManager.put(filePath, subScope, data.getUptoken(),
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        if (info.isOK()) {
                            if (uploadCallBack != null) {
                                uploadCallBack.onUploadSuccess(data.getDomain() + "/" + subScope);
                            }
                        } else {
                            if (uploadCallBack != null) {
                                uploadCallBack.onUploadFailed();
                            }
                        }
                    }
                }, new UploadOptions(null, null, false, new UpProgressHandler() {
                    @Override
                    public void progress(String s, double v) {
                        int progress = (int) (v * 100);
                        MoorLogUtils.i(progress);
                        if (progress % 2 == 0) {
                            if (uploadCallBack != null) {
                                uploadCallBack.onUploadProgress(progress);
                            }
                        }
                    }
                }, null));
    }


}
