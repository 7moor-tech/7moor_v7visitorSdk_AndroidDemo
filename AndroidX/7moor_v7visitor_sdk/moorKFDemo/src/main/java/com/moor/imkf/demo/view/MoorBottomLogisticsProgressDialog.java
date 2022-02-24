package com.moor.imkf.demo.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moor.imkf.demo.R;
import com.moor.imkf.demo.adapter.MoorLogisticsProgressListAdapter;
import com.moor.imkf.demo.utils.MoorScreenUtils;
import com.moor.imkf.moorsdk.bean.MoorOrderInfoBean;

import java.util.ArrayList;

/**
 * @Description: 查看完整物流信息弹出框
 * @Author:R-D
 * @CreatDate: 2019-12-25 16:27
 */
@SuppressLint("ValidFragment")
public class MoorBottomLogisticsProgressDialog extends BottomSheetDialogFragment {
    private ArrayList<MoorOrderInfoBean> list;
    private String title;
    private String num;
    private String dialogTitle;
    protected Context mContext;
    protected View rootView;
    protected BottomSheetDialog dialog;
    protected BottomSheetBehavior mBehavior;


    public static  MoorBottomLogisticsProgressDialog init(String title, String num, ArrayList<MoorOrderInfoBean> list,String dialogTitle) {
        MoorBottomLogisticsProgressDialog dialog=new MoorBottomLogisticsProgressDialog();
        Bundle args = new Bundle();
        args.putSerializable("list", list);
        args.putString("num", num);
        args.putString("title", title);
        args.putString("dialogTitle", dialogTitle);
        dialog.setArguments(args);
        return dialog;
    }


    public MoorBottomLogisticsProgressDialog() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        Bundle arguments = getArguments();
        assert arguments != null;
        list = (ArrayList<MoorOrderInfoBean>) arguments.getSerializable("list");
        title = arguments.getString("title");
        num = arguments.getString("num");
        dialogTitle=arguments.getString("dialogTitle");

        if (rootView == null) {
            rootView = View.inflate(mContext, R.layout.moor_layout_bottomsheet_progress, null);
            TextView tv_no_data = rootView.findViewById(R.id.tv_no_data);
            TextView tv_express_name = rootView.findViewById(R.id.tv_express_name);
            TextView tv_express_num = rootView.findViewById(R.id.tv_express_num);
            TextView tv_dialog_title=rootView.findViewById(R.id.tv_dialog_title);
            ImageView ivBottomClose = rootView.findViewById(R.id.iv_bottom_close);
            ivBottomClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            RecyclerView rv_switch = rootView.findViewById(R.id.rv_switch);
            rv_switch.setLayoutManager(new LinearLayoutManager(mContext));

            MoorLogisticsProgressListAdapter adapter = new MoorLogisticsProgressListAdapter(list, true);
            rv_switch.setAdapter(adapter);

            if (list != null && list.size() == 0) {
                tv_no_data.setVisibility(View.VISIBLE);
                rv_switch.setVisibility(View.GONE);
            } else {
                tv_no_data.setVisibility(View.GONE);
                rv_switch.setVisibility(View.VISIBLE);
            }
            tv_express_name.setMaxWidth(MoorScreenUtils.getScreenHeight(mContext) / 5 * 2);
            if (!TextUtils.isEmpty(title)) {
                tv_express_name.setText(title);
            }
            tv_express_name.setVisibility(TextUtils.isEmpty(title) ? View.GONE : View.VISIBLE);
            tv_express_num.setMaxWidth(MoorScreenUtils.getScreenHeight(mContext) / 5 * 3);
            if (!TextUtils.isEmpty(num)) {
                tv_express_num.setText(num);
            }
            tv_express_num.setVisibility(TextUtils.isEmpty(num) ? View.GONE : View.VISIBLE);
            //设置顶部标题
            if(!TextUtils.isEmpty(dialogTitle)){
                tv_dialog_title.setText(dialogTitle);
            }

        }

        dialog.setContentView(rootView);
        mBehavior = BottomSheetBehavior.from((View) rootView.getParent());
        mBehavior.setSkipCollapsed(true);
        mBehavior.setHideable(true);
        //重置高度
        if (dialog != null) {
            View bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
            bottomSheet.getLayoutParams().height = MoorScreenUtils.getScreenHeight(mContext) * 4 / 5;
        }
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

    public void close(boolean isAnimation) {
        if (isAnimation) {
            if (mBehavior != null) {
                mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        } else {
            dismiss();
        }
    }

}

