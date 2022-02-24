package com.moor.imkf.demo.utils;

import android.os.Environment;

import com.moor.imkf.moorsdk.utils.MoorSdkVersionUtil;

import java.io.File;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2021/08/05
 *     @desc   : 获取存储路径
 *     @version: 1.0
 * </pre>
 */
public class MoorPathUtil {

    public static String getImageDownLoadPath() {
        String path = "";
        if (MoorSdkVersionUtil.over_29()) {
            path = Environment.DIRECTORY_PICTURES + File.separator + "moor" + File.separator;
        } else {
            path = Environment.getExternalStorageDirectory().getPath() + File.separator + Environment.DIRECTORY_PICTURES + File.separator + "moor" + File.separator;
            if (!MoorFileUtils.isFileExists(path)) {
                MoorFileUtils.createOrExistsDir(new File(path));
            }
        }
        return path;
    }

    public static String getFileDownLoadPath() {
        String path = "";
        if (MoorSdkVersionUtil.over_29()) {
            path = Environment.DIRECTORY_DOWNLOADS + File.separator + "moor" + File.separator;
        } else {
            path = Environment.getExternalStorageDirectory().getPath() + File.separator + Environment.DIRECTORY_DOWNLOADS + File.separator + "moor" + File.separator;
            if (!MoorFileUtils.isFileExists(path)) {
                MoorFileUtils.createOrExistsDir(new File(path));
            }
        }
        return path;
    }
}
