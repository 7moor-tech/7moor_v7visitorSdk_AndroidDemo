package com.moor.imkf.demo.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moor.imkf.demo.R;
import com.moor.imkf.demo.utils.MoorColorUtils;
import com.moor.imkf.demo.view.shadowlayout.MoorShadowLayout;
import com.moor.imkf.moorsdk.bean.MoorOptions;
import com.moor.imkf.moorsdk.manager.MoorManager;


/**
 * <pre>
 *     @author : Trial
 *     @time   : 2021/5/15
 *     @desc   : 通用的底部弹框
 *     @version: 1.0
 * </pre>
 */
@SuppressLint("ValidFragment")
public class MoorCommonBottomDialog extends BottomSheetDialogFragment {

    protected Context mContext;
    protected View rootView;
    protected BottomSheetDialog dialog;
    protected BottomSheetBehavior mBehavior;
    protected MoorOptions options;

    public static MoorCommonBottomDialog init(String title, String positive, String negative) {
        MoorCommonBottomDialog dialog = new MoorCommonBottomDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("positive", positive);
        args.putString("negative", negative);
        dialog.setArguments(args);
        return dialog;
    }

    private MoorCommonBottomDialog() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        Bundle arguments = getArguments();
        assert arguments != null;
        String title = arguments.getString("title");
        String positive = arguments.getString("positive");
        String negative = arguments.getString("negative");
        options = (MoorOptions) MoorManager.getInstance().getOptions();
        if (options == null) {
            options = new MoorOptions();
        }
        if (rootView == null) {
            rootView = View.inflate(mContext, R.layout.moor_layout_common_bottom_dialog, null);
            ImageView ivBottomClose = rootView.findViewById(R.id.iv_bottom_close);
            ivBottomClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    close();
                }
            });
            TextView tvCommonTitle = rootView.findViewById(R.id.tv_common_title);
            MoorShadowLayout sl_common_left = rootView.findViewById(R.id.sl_common_left);
            MoorShadowLayout sl_common_right = rootView.findViewById(R.id.sl_common_right);
            TextView tvCommonLeft = rootView.findViewById(R.id.tv_common_left);
            TextView tvCommonRight = rootView.findViewById(R.id.tv_common_right);
            if (TextUtils.isEmpty(positive)) {
                tvCommonLeft.setVisibility(View.GONE);
            } else {
                tvCommonLeft.setVisibility(View.VISIBLE);
            }
            if (TextUtils.isEmpty(negative)) {
                tvCommonRight.setVisibility(View.GONE);
            } else {
                tvCommonRight.setVisibility(View.VISIBLE);
            }
            tvCommonTitle.setText(title);
            tvCommonLeft.setText(positive);
            tvCommonRight.setText(negative);

            sl_common_left.setLayoutBackground(MoorColorUtils.getColorWithAlpha(1f, options.getSdkMainThemeColor()));
            sl_common_left.setLayoutBackgroundTrue(MoorColorUtils.getColorWithAlpha(0.5f, options.getSdkMainThemeColor()));

            sl_common_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onClickPositive();
                    }
                }
            });
            sl_common_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onClickNegative();
                    }
                }
            });
        }

        dialog.setContentView(rootView);
        dialog.setCanceledOnTouchOutside(false);
        mBehavior = BottomSheetBehavior.from((View) rootView.getParent());
        mBehavior.setSkipCollapsed(true);
        mBehavior.setHideable(true);

        View bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
        bottomSheet.setBackgroundColor(mContext.getResources().getColor(R.color.moor_color_Translucent));

        mBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                //禁止拖拽
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    //设置为收缩状态
                    mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
        rootView.post(new Runnable() {
            @Override
            public void run() {
                mBehavior.setPeekHeight(rootView.getHeight());
            }
        });
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onStart() {
        super.onStart();
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((ViewGroup) (rootView.getParent())).removeView(rootView);
    }


    public boolean isShowing() {
        return dialog != null && dialog.isShowing();
    }

    public void close() {
        dismiss();
    }

    private OnClickListener listener;

    public interface OnClickListener {
        /**
         * 确定
         */
        void onClickPositive();

        /**
         * 取消
         */
        void onClickNegative();
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

}

