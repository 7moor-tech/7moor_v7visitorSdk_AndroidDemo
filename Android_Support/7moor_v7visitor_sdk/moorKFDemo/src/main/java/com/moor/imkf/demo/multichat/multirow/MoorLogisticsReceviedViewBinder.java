package com.moor.imkf.demo.multichat.multirow;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moor.imkf.demo.R;
import com.moor.imkf.demo.adapter.MoorLogisticsProgressListAdapter;
import com.moor.imkf.demo.bean.MoorEnumChatItemClickType;
import com.moor.imkf.demo.multichat.base.MoorBaseReceivedHolder;
import com.moor.imkf.demo.multichat.base.MoorBaseReceivedViewBinder;
import com.moor.imkf.demo.utils.MoorColorUtils;
import com.moor.imkf.demo.view.shadowlayout.MoorShadowLayout;
import com.moor.imkf.moorsdk.bean.MoorMsgBean;
import com.moor.imkf.moorsdk.bean.MoorOptions;
import com.moor.imkf.moorsdk.bean.MoorOrderBaseBean;
import com.moor.imkf.moorsdk.bean.MoorOrderInfoBean;
import com.moor.imkf.moorsdk.utils.MoorUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MoorLogisticsReceviedViewBinder extends MoorBaseReceivedViewBinder<MoorMsgBean, MoorLogisticsReceviedViewBinder.ViewHolder> {
    private final MoorOptions options;
    private int moreSize = 5;//默认大于5条订单显示查看更多

    public MoorLogisticsReceviedViewBinder(MoorOptions options) {
        this.options = options;
    }

    @Override
    protected MoorBaseReceivedHolder onCreateContentViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.moor_chat_row_logistics_recevied, parent, false);
        return new MoorLogisticsReceviedViewBinder.ViewHolder(root);
    }

    @Override
    protected void onBindContentViewHolder(@NonNull ViewHolder holder, @NonNull MoorMsgBean item) {
        holder.setData(item);
    }

    class ViewHolder extends MoorBaseReceivedHolder {
        public MoorShadowLayout msl_logistics;
        public RecyclerView rv_logistics_rx;
        public RelativeLayout rl_logistics;
        public RelativeLayout rl_progress_top;
        public TextView kf_chat_rich_title;
        public TextView kf_chat_rich_content;
        public TextView tv_no_data;
        public TextView tv_logistics_progress_name;
        public TextView tv_logistics_progress_num;
        public LinearLayout ll_order_content;
        public View view_top;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            msl_logistics = itemView.findViewById(R.id.msl_logistics);
            rv_logistics_rx = itemView.findViewById(R.id.rv_logistics_rx);
            rl_logistics = itemView.findViewById(R.id.rl_logistics);
            kf_chat_rich_title = itemView.findViewById(R.id.kf_chat_rich_title);
            kf_chat_rich_content = itemView.findViewById(R.id.kf_chat_rich_content);
            rl_progress_top = itemView.findViewById(R.id.rl_progress_top);
            tv_no_data = itemView.findViewById(R.id.tv_no_data);
            tv_logistics_progress_name = itemView.findViewById(R.id.tv_logistics_progress_name);
            tv_logistics_progress_num = itemView.findViewById(R.id.tv_logistics_progress_num);
            ll_order_content = itemView.findViewById(R.id.ll_order_content);
            view_top = itemView.findViewById(R.id.view_top);
        }

        void setData(@NonNull final MoorMsgBean detail) {
            if (detail.getMsgTask() != null && !"".equals(detail.getMsgTask())) {
                //颜色配置
                if (!TextUtils.isEmpty(options.getSdkMainThemeColor())) {
                    kf_chat_rich_content.setTextColor(Color.parseColor(options.getSdkMainThemeColor()));
                }
                String leftMsgBgColor = options.getLeftMsgBgColor();
                if (!TextUtils.isEmpty(leftMsgBgColor)) {
                    msl_logistics.setLayoutBackground(MoorColorUtils.getColorWithAlpha(1f, leftMsgBgColor));
                }
                Type token = new TypeToken<MoorOrderBaseBean>() {
                }.getType();
                final MoorOrderBaseBean orderBaseBean = new Gson().fromJson(detail.getMsgTask(), token);
                if (orderBaseBean.getData() != null) {

                    ArrayList<MoorOrderInfoBean> infoBeanList = orderBaseBean.getData().getShop_list();
                    if (infoBeanList == null) {
                        infoBeanList = orderBaseBean.getData().getItem_list();
                    }
                    if (infoBeanList == null) {
                        infoBeanList = new ArrayList<>();
                    }


                    //RecyclerView
                    rv_logistics_rx.setVisibility(infoBeanList.size() > 0 ? View.VISIBLE : View.GONE);
                    //展示无数据的文本，替换RecyclerView
                    tv_no_data.setVisibility(infoBeanList.size() > 0 ? View.GONE : View.VISIBLE);
                    //标题以下的部分
                    ll_order_content.setVisibility(infoBeanList.size() > 0 ? View.VISIBLE : View.GONE);

                    if (!TextUtils.isEmpty(orderBaseBean.getData().getEmpty_message())) {
                        tv_no_data.setText(orderBaseBean.getData().getEmpty_message());
                    }

                    //上部标题
                    kf_chat_rich_title.setText(infoBeanList.size() > 0 ? orderBaseBean.getData().getMessage()
                            : orderBaseBean.getData().getEmpty_message());
                    boolean isOrderList = "0".equals(orderBaseBean.getResp_type());

                    rl_logistics.setVisibility(View.GONE);

                    if (isOrderList) {
                        kf_chat_rich_title.setText(MoorUtils.getApp().getResources().getString(R.string.moor_not_suportxbottype));
                        rl_logistics.setVisibility(View.GONE);
                    } else {
                        kf_chat_rich_content.setText(MoorUtils.getApp().getResources().getString(R.string.moor_tips_lookmore));
                        rv_logistics_rx.setLayoutManager(new LinearLayoutManager(MoorUtils.getApp()));
                        rv_logistics_rx.setNestedScrollingEnabled(false);
                        if (TextUtils.isEmpty(orderBaseBean.getData().getList_num())) {
                            tv_logistics_progress_num.setVisibility(View.GONE);
                        } else {
                            tv_logistics_progress_num.setVisibility(View.VISIBLE);
                        }

                        if (TextUtils.isEmpty(orderBaseBean.getData().getList_title())) {
                            tv_logistics_progress_name.setVisibility(View.GONE);
                        } else {
                            tv_logistics_progress_name.setVisibility(View.VISIBLE);
                        }
                        //物流信息列表
                        if (infoBeanList.size() > 0) {
                            tv_logistics_progress_name.setText(orderBaseBean.getData().getList_title());
                            tv_logistics_progress_num.setText(orderBaseBean.getData().getList_num());
                            kf_chat_rich_title.setText(orderBaseBean.getData().getMessage());
                            //快递名称和运单号部分
                            rl_progress_top.setVisibility(View.VISIBLE);

                            MoorLogisticsProgressListAdapter adapter = new MoorLogisticsProgressListAdapter(infoBeanList, false);
                            rv_logistics_rx.setAdapter(adapter);

                            rl_logistics.setVisibility(infoBeanList.size() < 3 ? View.GONE : View.VISIBLE);
                        } else {
                            if (!TextUtils.isEmpty(orderBaseBean.getData().getList_num())) {
                                tv_logistics_progress_name.setText(orderBaseBean.getData().getList_title());
                                tv_logistics_progress_num.setText(orderBaseBean.getData().getList_num());
                                kf_chat_rich_title.setText(orderBaseBean.getData().getMessage());
                                ll_order_content.setVisibility(View.VISIBLE);
                                rl_progress_top.setVisibility(View.VISIBLE);
                                tv_no_data.setVisibility(View.VISIBLE);
                            } else {
                                ll_order_content.setVisibility(View.GONE);
                                tv_no_data.setVisibility(View.GONE);
                                kf_chat_rich_title.setText(orderBaseBean.getData().getEmpty_message());
                                rl_progress_top.setVisibility(View.GONE);
                                rv_logistics_rx.setVisibility(View.GONE);
                            }
                        }

                        final ArrayList<MoorOrderInfoBean> finalInfoBeanList = infoBeanList;
                        rl_logistics.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //物流查看更多
                                getBinderClickListener().onClick(v, detail, MoorEnumChatItemClickType.TYPE_LOGISTICS_MORE.
                                        setObj(finalInfoBeanList, orderBaseBean.getData().getList_title(), orderBaseBean.getData().getList_num(),orderBaseBean.getData().getMessage()));
                            }
                        });
                    }

//                    rl_logistics.setTag(holderTag);
//                    rl_logistics.setOnClickListener(listener);

                }
            }

        }
    }
}
