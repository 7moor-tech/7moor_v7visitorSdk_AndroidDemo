package com.moor.imkf.demo.view.imagepicker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.moor.imkf.demo.R;
import com.ypx.imagepicker.adapter.PickerItemAdapter;
import com.ypx.imagepicker.bean.ImageItem;
import com.ypx.imagepicker.bean.selectconfig.BaseSelectConfig;
import com.ypx.imagepicker.data.ICameraExecutor;
import com.ypx.imagepicker.data.IReloadExecutor;
import com.ypx.imagepicker.data.ProgressSceneEnum;
import com.ypx.imagepicker.presenter.IPickerPresenter;
import com.ypx.imagepicker.utils.PViewSizeUtils;
import com.ypx.imagepicker.views.PickerUiConfig;
import com.ypx.imagepicker.views.PickerUiProvider;
import com.ypx.imagepicker.views.base.PickerControllerView;
import com.ypx.imagepicker.views.base.PickerFolderItemView;
import com.ypx.imagepicker.views.base.PickerItemView;
import com.ypx.imagepicker.views.base.PreviewControllerView;
import com.ypx.imagepicker.views.wx.WXFolderItemView;

import java.util.ArrayList;

/**
 * Description: 自定义样式,目前mars就采用此样式
 * <p>
 * Author: peixing.yang
 * Date: 2019/2/21
 */
public class CustomImgPickerPresenter implements IPickerPresenter {

    @Override
    public void displayImage(View view, ImageItem item, int size, boolean isThumbnail) {
        Object object = item.getUri() != null ? item.getUri() : item.path;

        Glide.with(view.getContext()).load(object).apply(new RequestOptions()
                .format(isThumbnail ? DecodeFormat.PREFER_RGB_565 : DecodeFormat.PREFER_ARGB_8888))
                .override(isThumbnail ? size : Target.SIZE_ORIGINAL)
                .into((ImageView) view);
    }

    @NonNull
    @Override
    public PickerUiConfig getUiConfig(@Nullable Context context) {
        PickerUiConfig uiConfig = new PickerUiConfig();
        //设置主题色
        uiConfig.setThemeColor(context.getResources().getColor(R.color.moor_color_0081ff));
        //设置是否显示状态栏
        uiConfig.setShowStatusBar(true);
        //设置状态栏颜色
        uiConfig.setStatusBarColor(context.getResources().getColor(R.color.moor_color_f5f5f5));
        //设置选择器背景
        uiConfig.setPickerBackgroundColor(context.getResources().getColor(R.color.moor_color_f5f5f5));
        //设置预览页面背景色
        uiConfig.setPreviewBackgroundColor(context.getResources().getColor(R.color.moor_color_f5f5f5));
        //设置选择器文件夹打开方向
        uiConfig.setFolderListOpenDirection(PickerUiConfig.DIRECTION_TOP);
        //设置文件夹列表距离顶部/底部边距
        uiConfig.setFolderListOpenMaxMargin(0);

        //设置文件夹列表距离底部/顶部的最大间距。通俗点就是设置文件夹列表的高
        if (context != null) {
            uiConfig.setFolderListOpenMaxMargin(PViewSizeUtils.dp(context, 100));
        }


        uiConfig.setPickerUiProvider(new PickerUiProvider() {
            @Override
            public PickerControllerView getTitleBar(Context context) {
                CustomTitleBar titleBar = new CustomTitleBar(context);
                titleBar.setCompleteText(context.getString(R.string.moor_chat_send_picture));
                titleBar.setCompleteBackground(null, null);
                titleBar.setCompleteTextColor(context.getResources().getColor(R.color.moor_color_0081ff),
                        context.getResources().getColor(R.color.moor_color_151515));
                titleBar.centerTitle();
                titleBar.setShowArrow(true);
                titleBar.setCanToggleFolderList(true);
                titleBar.setBackIconID(R.drawable.moor_icon_back);
                return titleBar;
            }

            @Override
            public PickerItemView getItemView(Context context) {
                return new CustomPickerItem(context);
            }

            @Override
            public PickerControllerView getBottomBar(Context context) {
                return null;
            }

            @Override
            public PreviewControllerView getPreviewControllerView(Context context) {
                return new CustomPreviewControllerView(context);
            }

            @Override
            public PickerFolderItemView getFolderItemView(Context context) {
                WXFolderItemView itemView = (WXFolderItemView) super.getFolderItemView(context);
                itemView.setIndicatorColor(Color.RED);
                return itemView;
            }
        });
        return uiConfig;
    }

    @Override
    public void tip(Context context, String msg) {
        if (context == null) {
            return;
        }

        Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void overMaxCountTip(Context context, int maxCount) {
        tip(context, "最多选择" + maxCount + "张图片");
    }

    @Override
    public DialogInterface showProgressDialog(@Nullable Activity activity, ProgressSceneEnum progressSceneEnum) {
        return ProgressDialog.show(activity, null, progressSceneEnum == ProgressSceneEnum.crop ? "正在剪裁..." : "正在加载...");
    }

    @Override
    public boolean interceptPickerCompleteClick(@Nullable Activity activity, ArrayList<ImageItem> selectedList, BaseSelectConfig selectConfig) {
        return false;
    }

    @Override
    public boolean interceptPickerCancel(@Nullable Activity activity, ArrayList<ImageItem> selectedList) {
        return false;
    }

    /**
     * <p>
     * 图片点击事件拦截，如果返回true，则不会执行选中操纵，如果要拦截此事件并且要执行选中
     * 请调用如下代码：
     * <p>
     * adapter.preformCheckItem()
     * <p>
     * <p>
     * 此方法可以用来跳转到任意一个页面，比如自定义的预览
     *
     * @param activity        上下文
     * @param imageItem       当前图片
     * @param selectImageList 当前选中列表
     * @param allSetImageList 当前文件夹所有图片
     * @param selectConfig    选择器配置项，如果是微信样式，则selectConfig继承自MultiSelectConfig
     *                        如果是小红书剪裁样式，则继承自CropSelectConfig
     * @param adapter         当前列表适配器，用于刷新数据
     * @param isClickCheckBox 是否点击item右上角的选中框
     * @param reloadExecutor  刷新器
     * @return 是否拦截
     */
    @Override
    public boolean interceptItemClick(@Nullable Activity activity, ImageItem imageItem, ArrayList<ImageItem> selectImageList, ArrayList<ImageItem> allSetImageList, BaseSelectConfig selectConfig, PickerItemAdapter adapter, boolean isClickCheckBox, @Nullable IReloadExecutor reloadExecutor) {
        return false;
    }

    @Override
    public boolean interceptCameraClick(@Nullable Activity activity, ICameraExecutor takePhoto) {
        return false;
    }
}
