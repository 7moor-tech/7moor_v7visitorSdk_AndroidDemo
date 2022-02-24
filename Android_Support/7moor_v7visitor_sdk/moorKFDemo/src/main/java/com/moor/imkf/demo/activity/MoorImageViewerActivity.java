package com.moor.imkf.demo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;

import com.moor.imkf.demo.R;
import com.moor.imkf.demo.utils.MoorStatusBarUtil;
import com.moor.imkf.demo.view.photoview.MoorPhotoView;
import com.moor.imkf.moorsdk.listener.IMoorImageLoaderListener;
import com.moor.imkf.moorsdk.manager.MoorManager;
import com.moor.imkf.moorsdk.utils.MoorUtils;

import java.io.File;


/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/3/21
 *     @desc   : 预览图片页面
 *     @version: 1.0
 * </pre>
 */
public class MoorImageViewerActivity extends MoorBaseActivity {

    private MoorPhotoView moorPhotoView;
    private boolean isSaveSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.moor_activity_image_look);
        MoorStatusBarUtil.setTranslucentStatus(this);
        moorPhotoView = (MoorPhotoView) findViewById(R.id.photoView);

        Intent intent = getIntent();

        final String imgPath = intent.getStringExtra("imagePath");

        if (imgPath != null && !"".equals(imgPath)) {
            MoorManager.getInstance().loadImage(imgPath,0,0, new IMoorImageLoaderListener() {
                @Override
                public void onLoadStarted(@Nullable Drawable placeholder) {
                    moorPhotoView.setImageDrawable(placeholder);
                }

                @Override
                public void onLoadComplete(@NonNull Bitmap bitmap) {
                    Drawable drawable = new BitmapDrawable(MoorUtils.getApp().getResources(), bitmap);
                    moorPhotoView.setImageDrawable(drawable);
                }

                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {

                }


                @Override
                public void onResourceReady(@NonNull File resource) {

                }
            });
        } else {
            finish();
        }

        moorPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        moorPhotoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                if (fromother == 0) {//
//                    //弹框
////                    openDialog(imgPath);
//                } else {
////                    ToastUtils.showShort("不保存本地的"+imgPath);
//                }
                return true;
            }


        });

    }

//    private void openDialog(final String imageUrl) {
//        new ActionSheetDialog(this)
//                .builder()
//                .setCancelable(true)
//                .setCanceledOnTouchOutside(true)
//                .addSheetItem(
////                        this.getResources().getString(R.string.restartchat),
//                        getString(R.string.ykf_save_pic),
//                        ActionSheetDialog.SheetItemColor.BLACK, new ActionSheetDialog.OnSheetItemClickListener() {
//                            @Override
//                            public void onClick(int which) {
//                                saveImage(imageUrl);
//                            }
//                        }).show();
//    }


    //保存图片
//    private void saveImage(String ImageUrl) {
//        Glide.with(ImageViewLookActivity.this)
//                .asBitmap()
//                .load(ImageUrl)
//                .into(new CustomTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
////                        得到bitmap
//                        isSaveSuccess = ImageUtils.saveImageToGallery(ImageViewLookActivity.this, resource);
//                        if (isSaveSuccess) {
//                            Toast.makeText(ImageViewLookActivity.this, getString(R.string.ykf_save_pic_ok), Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(ImageViewLookActivity.this, getString(R.string.ykf_save_pic_fail), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onLoadCleared(@Nullable Drawable placeholder) {
//
//                    }
//                });
//
//    }


}
