package com.moor.imkf.demo.view.imagepicker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.moor.imkf.demo.R;
import com.ypx.imagepicker.bean.ImageItem;
import com.ypx.imagepicker.bean.PickerItemDisableCode;
import com.ypx.imagepicker.bean.selectconfig.BaseSelectConfig;
import com.ypx.imagepicker.presenter.IPickerPresenter;
import com.ypx.imagepicker.utils.PCornerUtils;
import com.ypx.imagepicker.views.base.PickerItemView;

/**
 * Time: 2019/8/8 16:47
 * Author:ypx
 * Description: 自定义选择器列表item
 */
public class CustomPickerItem extends PickerItemView {
    private ImageView mIvImage;
    private TextView mTvDuration;
    private View mVMask;
    private View mVSelect;
    private TextView mTvIndex;
    private BaseSelectConfig selectConfig;

    public CustomPickerItem(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.moor_picture_custom_item;
    }

    @Override
    protected void initView(View view) {
        mIvImage = view.findViewById(R.id.iv_image);
        mTvDuration = view.findViewById(R.id.mTvDuration);
        mVMask = view.findViewById(R.id.v_mask);
        mVSelect = view.findViewById(R.id.v_select);
        mTvIndex = view.findViewById(R.id.mTvIndex);
    }

    @Override
    public View getCameraView(BaseSelectConfig selectConfig, IPickerPresenter presenter) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(getContext()).inflate(com.ypx.imagepicker.R.layout.picker_item_camera, null);
        TextView mTvvCamera = view.findViewById(com.ypx.imagepicker.R.id.tv_camera);
        mTvvCamera.setText(selectConfig.isOnlyShowVideo() ?
                getContext().getString(R.string.picker_str_item_take_video) :
                getContext().getString(R.string.picker_str_item_take_photo));
        return view;
    }

    @Override
    public View getCheckBoxView() {
        return mVSelect;
    }

    @Override
    public void initItem(ImageItem imageItem, IPickerPresenter presenter, BaseSelectConfig selectConfig) {
        this.selectConfig = selectConfig;
        presenter.displayImage(mIvImage, imageItem, mIvImage.getWidth(), true);
    }

    @Override
    public void disableItem(ImageItem imageItem, int disableCode) {
        //默认开启校验是否超过最大数时item状态为不可选中,这里关闭它
        if (disableCode == PickerItemDisableCode.DISABLE_OVER_MAX_COUNT) {
            return;
        }
//        mVMask.setVisibility(View.VISIBLE);
//        mVMask.setBackgroundColor(Color.parseColor("#222222"));
        mTvIndex.setVisibility(View.GONE);
        mVSelect.setVisibility(View.GONE);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void enableItem(ImageItem imageItem, boolean isChecked, int indexOfSelectedList) {
        if (imageItem.isVideo()) {
            mTvDuration.setVisibility(View.VISIBLE);
            mTvDuration.setText(imageItem.getDurationFormat());
        } else {
            mTvDuration.setVisibility(View.GONE);
        }

        boolean isVideoSinglePickAndAutoComplete = imageItem.isVideo() && selectConfig.isVideoSinglePickAndAutoComplete();
        if (isVideoSinglePickAndAutoComplete || (selectConfig.isSinglePickAutoComplete() && selectConfig.getMaxCount() <= 1)) {
            mTvIndex.setVisibility(View.GONE);
            mVSelect.setVisibility(View.GONE);
        } else {
            mTvIndex.setVisibility(View.VISIBLE);
            mVSelect.setVisibility(View.VISIBLE);

            if (indexOfSelectedList >= 0) {
                mTvIndex.setText(String.format("%d", indexOfSelectedList + 1));
                mTvIndex.setBackground(PCornerUtils.cornerDrawableAndStroke(Color.parseColor("#0081FF"), dp(12), dp(1), Color.WHITE));
            } else {
                mTvIndex.setBackground(getResources().getDrawable(com.ypx.imagepicker.R.mipmap.picker_icon_unselect));
                mTvIndex.setText("");
            }
        }

        if (imageItem.isPress()) {
            mVMask.setVisibility(View.VISIBLE);
            int themeColor = Color.RED;
            int halfColor = Color.argb(100, Color.red(themeColor), Color.green(themeColor), Color.blue(themeColor));
            Drawable maskDrawable = PCornerUtils.cornerDrawableAndStroke(halfColor, 0, dp(2), themeColor);
            mVMask.setBackground(maskDrawable);
        } else {
            mVMask.setVisibility(View.GONE);
        }
    }
}
