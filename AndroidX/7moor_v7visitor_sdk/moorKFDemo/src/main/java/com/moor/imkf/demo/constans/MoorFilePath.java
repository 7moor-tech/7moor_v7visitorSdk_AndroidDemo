package com.moor.imkf.demo.constans;

import android.os.Environment;
import android.text.TextUtils;

import com.moor.imkf.moorsdk.utils.MoorSdkVersionUtil;
import com.moor.imkf.moorsdk.utils.MoorUtils;

import java.io.File;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/1/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public class MoorFilePath {


    /**
     * 获取文件目录File对象
     * @param folderPath 存放的文件夹名称
     * @param childPath 创建子文件夹名称
     * @param fileName 要创建的文件名称
     */
    public static File getDownloadFile(String folderPath, String childPath, String fileName) {
        String fileDir = "";
        if (TextUtils.isEmpty(childPath)) {
            fileDir = folderPath;
        } else {
            fileDir = folderPath + childPath + File.separator;
        }
        File dir = new File(fileDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        return new File(dir, fileName);
    }



    public static String getDownloadFilePath(String path) {
        String fileDir = "";
        if (MoorSdkVersionUtil.over_29()) {
            if (MoorUtils.getApp() != null) {
                if (TextUtils.isEmpty(path)) {
                    fileDir = MoorUtils.getApp().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) +
                            File.separator + "moor_download_file" + File.separator;
                } else {
                    fileDir = MoorUtils.getApp().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) +
                            File.separator + "moor_download_file" + File.separator + path + File.separator;
                }
            }
        } else {
            if (TextUtils.isEmpty(path)) {
                fileDir = Environment.getExternalStorageDirectory() + File.separator
                        + "moor_download_file" + File.separator;
            } else {

                fileDir = Environment.getExternalStorageDirectory() + File.separator
                        + "moor_download_file" + File.separator + path;
            }
        }
//        File dir = new File(fileDir);
//        if (!dir.exists()) {
//            dir.mkdirs();
//        }

        return fileDir;
    }

}
