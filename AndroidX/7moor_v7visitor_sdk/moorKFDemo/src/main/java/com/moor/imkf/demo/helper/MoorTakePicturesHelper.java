package com.moor.imkf.demo.helper;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.fragment.app.Fragment;
import androidx.core.content.FileProvider;
import androidx.core.os.EnvironmentCompat;

import com.moor.imkf.demo.constans.MoorDemoConstants;
import com.moor.imkf.demo.utils.MoorFileUtils;
import com.moor.imkf.moorsdk.utils.MoorSdkVersionUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2021/03/18
 *     @desc   : 拍照辅助类
 *     @version: 1.0
 * </pre>
 */
public class MoorTakePicturesHelper {

    private static final String DATE_FORMAT = "yyyyMMdd_HHmmss";
    private static final String CONTENT = "content://";
    /**
     * 用于保存拍照图片的uri
     */
    private Uri mCameraUri;

    /**
     * 用于保存图片的文件路径，Android 10以下使用图片路径访问图片
     */
    private String mCameraImagePath = "";

    private MoorTakePicturesHelper() {
    }

    public static MoorTakePicturesHelper getInstance() {
        return MoorTakePicturesHelper.SingletonHolder.sInstance;
    }

    private static class SingletonHolder {
        private static final MoorTakePicturesHelper sInstance = new MoorTakePicturesHelper();
    }

    public Uri getCameraUri() {
        return mCameraUri;
    }

    public String getCameraImagePath() {
        return mCameraImagePath;
    }

    /**
     * 调起相机拍照
     */
    public void openCamera(Fragment fragment, int requestCode) {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断是否有相机
        if (captureIntent.resolveActivity(fragment.getContext().getPackageManager()) != null) {
            File photoFile = null;
            Uri photoUri = null;

            if (MoorSdkVersionUtil.over_29()) {
                photoUri = createImageUri(fragment.getContext());
            } else {
                try {
                    photoFile = createImageFile(fragment.getContext());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (photoFile != null) {
                    mCameraImagePath = photoFile.getAbsolutePath();
                    if (MoorSdkVersionUtil.over_24()) {
                        //适配Android 7.0文件权限，通过FileProvider创建一个content类型的Uri
                        photoUri = FileProvider.getUriForFile(fragment.getContext(), fragment.getContext().getPackageName() + MoorDemoConstants.MOOR_FILE_PROVIDER, photoFile);
                    } else {
                        photoUri = Uri.fromFile(photoFile);
                    }
                }
            }

            mCameraUri = photoUri;
            if (photoUri != null) {
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                fragment.startActivityForResult(captureIntent, requestCode);
            }
        }
    }

    /**
     * 创建图片地址uri,用于保存拍照后的照片
     *
     * @param context
     */
    private Uri createImageUri(Context context) {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        } else {
            return context.getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, new ContentValues());
        }
    }

    /**
     * 创建保存图片的文件
     *
     * @param context
     */
    private File createImageFile(Context context) {
        String imageName = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(new Date()) + ".jpg";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        File tempFile = new File(storageDir, imageName);
        if (!Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(tempFile))) {
            return null;
        }
        return tempFile;
    }

    /**
     * 删除uri
     *
     * @param context
     */
    public void deleteUri(Context context) {
        if (getCameraUri() != null) {
            Uri uri = getCameraUri();
            if (uri.toString().startsWith(CONTENT)) {
                context.getContentResolver().delete(uri, null, null);
            } else {
                File file = new File(MoorFileUtils.getRealPathFromUri(context, uri));
                if (file.exists() && file.isFile()) {
                    file.delete();
                }
            }
        }
    }
}
