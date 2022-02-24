package com.moor.imkf.sample.imageloader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.moor.imkf.moorsdk.listener.IMoorImageLoader;
import com.moor.imkf.moorsdk.listener.IMoorImageLoaderListener;
import com.moor.imkf.moorsdk.utils.MoorUtils;
import com.moor.imkf.sample.R;

import java.io.File;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 1/25/21
 *     @desc   : 指定sdk中图片加载方式，此处使用Glide方式为例；
 *     @version: 1.0
 * </pre>
 */
public class GlideImageLoader implements IMoorImageLoader {
    private final Context context;

    public GlideImageLoader() {
        this.context = MoorUtils.getApp();
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadImage(boolean isGif, boolean isDownload, final String uri, final ImageView imageView, int width, int height, final IMoorImageLoaderListener listener) {
        if (context == null) {
            return;
        }
        if (isDownload) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    FutureTarget<File> target = Glide.with(context)
                            .asFile()
                            .load(uri)
                            .submit();

                    try {
                        File file = target.get();
                        if (listener != null) {
                            listener.onResourceReady(file);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            return;
        }

        final RequestOptions options = new RequestOptions()
                .fitCenter()
                .error(R.drawable.moor_pic_thumb_bg);
        if (isGif) {
            options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
            Glide.with(context)
                    .asGif()
                    .load(uri)
                    .apply(options)
                    .into(imageView);
        } else {
            options.placeholder(R.drawable.moor_pic_thumb_bg)
                    .dontAnimate();
            if (width != 0 && height != 0) {
                options.override(width, height);
            }
            Glide.with(context)
                    .asBitmap()
                    .load(uri)
                    .apply(options)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull final Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            if (imageView != null) {
                                imageView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        imageView.setImageBitmap(resource);
                                    }
                                });
                            } else if (listener != null) {
                                listener.onLoadComplete(resource);
                            }

                        }

                        @Override
                        public void onLoadStarted(@Nullable Drawable placeholder) {
                            if (listener != null) {
                                listener.onLoadStarted(placeholder);
                            }
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            if (listener != null) {
                                listener.onLoadFailed(errorDrawable);
                            }
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });
        }
    }

    @Override
    public void clear(ImageView imageView) {
        Glide.with(context).clear(imageView);
    }
}
