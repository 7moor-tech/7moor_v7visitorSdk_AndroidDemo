package com.moor.imkf.demo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.moor.imkf.demo.R;
import com.moor.imkf.demo.adapter.MoorImagePreviewAdapter;
import com.moor.imkf.demo.helper.MoorDragCloseHelper;
import com.moor.imkf.demo.utils.MoorFileUtils;
import com.moor.imkf.demo.utils.MoorPathUtil;
import com.moor.imkf.demo.utils.permission.MoorPermissionConstants;
import com.moor.imkf.demo.utils.permission.MoorPermissionUtil;
import com.moor.imkf.demo.utils.permission.callback.OnRequestCallback;
import com.moor.imkf.demo.view.imageviewer.MoorHackyViewPager;
import com.moor.imkf.demo.view.imageviewer.MoorImagePreview;
import com.moor.imkf.demo.view.imageviewer.subscaleview.MoorSubsamplingScaleImageView;
import com.moor.imkf.demo.view.photoview.MoorPhotoView;
import com.moor.imkf.moorhttp.MoorHttpUtils;
import com.moor.imkf.moorsdk.bean.MoorImageInfoBean;
import com.moor.imkf.moorsdk.listener.IMoorImageLoaderListener;
import com.moor.imkf.moorsdk.manager.MoorManager;
import com.moor.imkf.moorsdk.utils.MoorLogUtils;
import com.moor.imkf.moorsdk.utils.MoorSdkVersionUtil;
import com.moor.imkf.moorsdk.utils.toast.MoorToastUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/4/21
 *     @desc   : 预览图片页面
 *     @version: 1.0
 * </pre>
 */
public class MoorImagePreviewActivity extends MoorBaseActivity implements View.OnClickListener {


    private List<MoorImageInfoBean> imageInfoList;
    /**
     * 当前显示的图片索引
     */
    private int currentItem;

    private MoorImagePreviewAdapter moorImagePreviewAdapter;
    private TextView tvIndicator;
    private FrameLayout fmCenterProgressContainer;
    private FrameLayout fl_download;
    private View rootView;
    /**
     * 当前显示的地址
     */
    private String currentItemPathUrl = "";
    private MoorDragCloseHelper dragCloseHelper;
    private boolean scrolling;

    public static void activityStart(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(context, MoorImagePreviewActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.moor_image_preview_fade_in, R.anim.moor_image_preview_fade_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moor_layout_image_preview);
        if (MoorSdkVersionUtil.over_21()) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (MoorSdkVersionUtil.over_19()) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        fl_download = findViewById(R.id.fl_download);

        // 下载图片按钮
        fl_download.setOnClickListener(this);
        imageInfoList = MoorImagePreview.getInstance().getImageInfoList();
        if (null == imageInfoList || imageInfoList.size() == 0) {
            onBackPressed();
            return;
        }
        currentItem = MoorImagePreview.getInstance().getIndex();
        boolean isShowIndicator = MoorImagePreview.getInstance().isShowIndicator();

        currentItemPathUrl = imageInfoList.get(currentItem).getPath();
        rootView = findViewById(R.id.rootView);
        final MoorHackyViewPager viewPager = findViewById(R.id.viewPager);
        tvIndicator = findViewById(R.id.tv_indicator);

        fmCenterProgressContainer = findViewById(R.id.fm_center_progress_container);

        fmCenterProgressContainer.setVisibility(View.GONE);


        if (!isShowIndicator) {
            tvIndicator.setVisibility(View.GONE);
        } else {
            if (imageInfoList.size() > 1) {
                tvIndicator.setVisibility(View.VISIBLE);
            } else {
                tvIndicator.setVisibility(View.GONE);
            }
        }
        MoorImageInfoBean infoBean = imageInfoList.get(currentItem);
        if (infoBean.getPath().startsWith("http")) {
            fl_download.setVisibility(View.VISIBLE);
        } else {
            fl_download.setVisibility(View.GONE);
        }

        tvIndicator.setText(String.format(getString(R.string.moor_indicator), currentItem + 1 + "", "" + imageInfoList.size()));


        moorImagePreviewAdapter = new MoorImagePreviewAdapter(this, imageInfoList);
        viewPager.setAdapter(moorImagePreviewAdapter);
        viewPager.setCurrentItem(currentItem);
        viewPager.setOffscreenPageLimit(4);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentItem = position;
                MoorImageInfoBean infoBean = imageInfoList.get(position);
                currentItemPathUrl = infoBean.getPath();

                // 更新进度指示器
                tvIndicator.setText(
                        String.format(getString(R.string.moor_indicator), currentItem + 1 + "", "" + imageInfoList.size()));

                if (infoBean.getPath().startsWith("http")) {
                    fl_download.setVisibility(View.VISIBLE);
                } else {
                    fl_download.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);


            }

            @Override
            public void onPageScrollStateChanged(int state) {
                scrolling = state != 0;
            }
        });
        //初始化拖拽返回
        dragCloseHelper = new MoorDragCloseHelper(this);
        dragCloseHelper.setShareElementMode(true);
        dragCloseHelper.setDragCloseViews(rootView, viewPager);
        dragCloseHelper.setDragCloseListener(new MoorDragCloseHelper.DragCloseListener() {
            @Override
            public boolean intercept() {
                //默认false 不拦截 如果图片是放大状态，或者处于滑动返回状态，需要拦截
                HashMap<Integer, View> imageViewHashMap = moorImagePreviewAdapter.getImageViewHashMap();
                for (Map.Entry<Integer, View> entry : imageViewHashMap.entrySet()) {
                    if (entry.getKey() == viewPager.getCurrentItem()) {
                        View value = entry.getValue();
                        if (value instanceof MoorPhotoView) {
                            float scale = ((MoorPhotoView) value).getScale();
                            float scaleStart = imageInfoList.get(viewPager.getCurrentItem()).getStartScale();
                            return scrolling || scale > scaleStart;
                        } else if (value instanceof MoorSubsamplingScaleImageView) {
                            float scale = ((MoorSubsamplingScaleImageView) value).getScale();
                            float scaleStart = imageInfoList.get(viewPager.getCurrentItem()).getStartScale();
                            return scrolling || scale > scaleStart;
                        }
                    }
                }

                return false;
            }

            @Override
            public void dragStart() {
            }

            @Override
            public void dragging(float percent) {
            }

            @Override
            public void dragCancel() {
            }

            @Override
            public void dragClose(boolean isShareElementMode) {
                onBackPressed();
            }
        });

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (dragCloseHelper.handleEvent(event)) {
            return true;
        } else {
            return super.dispatchTouchEvent(event);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.moor_image_preview_fade_in, R.anim.moor_image_preview_fade_out);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.fl_download) {
            // 检查权限
            MoorPermissionUtil.checkPermission(MoorImagePreviewActivity.this, new OnRequestCallback() {
                @Override
                public void requestSuccess() {
                    MoorLogUtils.d("开始下载图片");
                    fl_download.setVisibility(View.GONE);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            downloadCurrentImg();
                        }
                    }).start();
                }
            }, MoorPermissionConstants.STORE);

        }
    }

    /**
     * 下载当前图片
     */
    private void downloadCurrentImg() {
        if (currentItemPathUrl.startsWith("http")) {
            MoorManager.getInstance().downloadImage(currentItemPathUrl, new IMoorImageLoaderListener() {
                @Override
                public void onLoadStarted(@Nullable Drawable placeholder) {

                }

                @Override
                public void onLoadComplete(@NonNull Bitmap bitmap) {

                }

                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {

                }

                @Override
                public void onResourceReady(@NonNull final File resource) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (MoorFileUtils.saveImage(MoorImagePreviewActivity.this, resource)) {
                                MoorLogUtils.i(resource.getAbsoluteFile());
                                MoorToastUtils.showShort(getResources().getString(R.string.moor_toast_save_success)
                                        + MoorPathUtil.getImageDownLoadPath());
                                fl_download.setVisibility(View.GONE);
                            }
                        }
                    });

                }
            });
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MoorImagePreview.getInstance().reset();
        MoorHttpUtils.getInstance().cancelTag(this);
        if (moorImagePreviewAdapter != null) {
            moorImagePreviewAdapter.closePage();
        }
    }


}