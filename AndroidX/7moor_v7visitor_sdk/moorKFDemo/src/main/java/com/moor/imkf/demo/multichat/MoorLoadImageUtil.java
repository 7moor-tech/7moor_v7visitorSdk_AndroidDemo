package com.moor.imkf.demo.multichat;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.moor.imkf.demo.R;
import com.moor.imkf.demo.utils.MoorImageUtil;
import com.moor.imkf.moorsdk.bean.MoorImageInfoBean;
import com.moor.imkf.moorsdk.bean.MoorImageType;
import com.moor.imkf.moorsdk.bean.MoorMsgBean;
import com.moor.imkf.moorsdk.db.MoorMsgDao;
import com.moor.imkf.moorsdk.listener.IMoorImageLoaderListener;
import com.moor.imkf.moorsdk.manager.MoorManager;

import java.io.File;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2021/03/04
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public class MoorLoadImageUtil {

    public static void loadImage(final MoorMsgBean item, final ImageView imageView) {
        final String url;
        if (TextUtils.isEmpty(item.getFileLocalPath())) {
            url = item.getContent();
        } else {
            url = item.getFileLocalPath();
        }
        int[] imageWidthHeight = MoorImageUtil.getImageWidthHeight(url);
        final MoorImageInfoBean imageInfoBean = MoorImageUtil.resizeImageView(imageWidthHeight[0], imageWidthHeight[1]);

        if (item.getImageHeight() != imageInfoBean.getLayoutH() || item.getImageWidth() != imageInfoBean.getLayoutW()) {
            //保存宽高
            item.setImageWidth(imageInfoBean.getLayoutW());
            item.setImageHeight(imageInfoBean.getLayoutH());
            MoorMsgDao.getInstance().updateMoorMsgBeanToDao(item);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
            layoutParams.width = imageInfoBean.getLayoutW();
            layoutParams.height = imageInfoBean.getLayoutH();
            imageView.setLayoutParams(layoutParams);
        }


        IMoorImageLoaderListener listener = new IMoorImageLoaderListener() {
            @Override
            public void onLoadStarted(@Nullable Drawable placeholder) {

            }

            @Override
            public void onLoadComplete(@NonNull Bitmap bitmap) {
                Bitmap imageBitmap = ThumbnailUtils.extractThumbnail(bitmap, imageInfoBean.getLayoutW(), imageInfoBean.getLayoutH());
//                Drawable drawable = new BitmapDrawable(MoorUtils.getApp().getResources(), imageBitmap);
                if (url == imageView.getTag(R.id.moor_list_image_tag)) {
                    imageView.setImageBitmap(imageBitmap);
                }
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {

            }

            @Override
            public void onResourceReady(@NonNull File resource) {

            }
        };

        int realW = 0;
        int realH = 0;
        if (imageInfoBean.getType() == MoorImageType.HORIZONTAL) {
            realW = imageInfoBean.getRealSize();
            realH = imageInfoBean.getLayoutH();
        } else if (imageInfoBean.getType() == MoorImageType.VERTICAL) {
            realW = imageInfoBean.getLayoutW();
            realH = imageInfoBean.getRealSize();
        } else if (imageInfoBean.getType() == MoorImageType.COMMON) {
            realW = imageInfoBean.getLayoutW();
            realH = imageInfoBean.getLayoutH();
        }
        MoorManager.getInstance().loadImage(url, realW, realH, listener);


    }
}
