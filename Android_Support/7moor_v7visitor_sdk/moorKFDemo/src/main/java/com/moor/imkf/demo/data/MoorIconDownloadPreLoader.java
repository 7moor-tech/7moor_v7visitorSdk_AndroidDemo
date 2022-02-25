package com.moor.imkf.demo.data;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.moor.imkf.demo.bean.MoorIconUrlBean;
import com.moor.imkf.demo.constans.MoorDemoConstants;
import com.moor.imkf.demo.preloader.interfaces.IMoorGroupedDataLoader;
import com.moor.imkf.lib.constants.MoorPathConstants;
import com.moor.imkf.lib.http.donwload.IMoorFileDownLoadListener;
import com.moor.imkf.lib.http.donwload.MoorSdkFileUtils;
import com.moor.imkf.moorhttp.MoorHttpUtils;
import com.moor.imkf.moorhttp.MoorUrlManager;
import com.moor.imkf.moorsdk.bean.MoorOptions;
import com.moor.imkf.moorsdk.db.MoorOptionsDao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import okhttp3.Response;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 6/17/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public class MoorIconDownloadPreLoader implements IMoorGroupedDataLoader<MoorIconUrlBean> {

    public MoorIconDownloadPreLoader() {
    }

    @Override
    public MoorIconUrlBean loadData() {
        final MoorIconUrlBean iconUrlBean = new MoorIconUrlBean();
        MoorOptions options = MoorOptionsDao.getInstance().queryOptions();
        final String isEmojiBtnImgUrl = options.getIsEmojiBtnImgUrl();
        String moreFunctionImgUrl = options.getMoreFunctionImgUrl();
        String showKeyboardImgUrl = options.getShowKeyboardImgUrl();
        try {
            saveFile(isEmojiBtnImgUrl, "moor_icon_emoji.png", iconUrlBean);
            saveFile(moreFunctionImgUrl, "moor_icon_more.png", iconUrlBean);
            saveFile(showKeyboardImgUrl, "moor_icon_keyboard.png", iconUrlBean);

            return iconUrlBean;
        } catch (Exception e) {
            e.printStackTrace();
            return iconUrlBean;
        }
    }

    private void saveFile(String url, String fileName, final MoorIconUrlBean iconUrlBean) {
        try {
            if (!TextUtils.isEmpty(url)) {
                Response keyRes = MoorHttpUtils
                        .get()
                        .url(MoorUrlManager.BASE_IM_URL + url)
                        .build()
                        .execute();
                MoorSdkFileUtils.saveFileSys(keyRes
                        , MoorPathConstants.getStoragePath(MoorPathConstants.PATH_NAME_MOOR_DOWNLOAD_FILE)
                        , fileName, new IMoorFileDownLoadListener() {
                            @Override
                            public void onSuccess(File file) {
                                iconUrlBean.setKeyboardUrl(file.getAbsolutePath());
                            }

                            @Override
                            public void onFailed() {

                            }

                            @Override
                            public void onProgress(int progress) {

                            }
                        });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String keyInGroup() {
        return MoorDemoConstants.MOOR_ICON_PRE_LOADER_KEY;
    }

    /**
     * @param bitmap   要保存的图片
     * @param filePath 目标路径
     * @return 是否成功
     * @Description 保存图片到指定路径
     */
    public boolean saveBmpToPath(final Bitmap bitmap, final String filePath) {
        if (bitmap == null || filePath == null) {
            return false;
        }
        boolean result = false;
        File file = new File(filePath);
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            result = bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
