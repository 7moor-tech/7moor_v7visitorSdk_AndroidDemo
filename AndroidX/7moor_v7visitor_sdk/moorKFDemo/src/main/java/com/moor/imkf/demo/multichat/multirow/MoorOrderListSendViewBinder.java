package com.moor.imkf.demo.multichat.multirow;

import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.moor.imkf.demo.R;
import com.moor.imkf.demo.adapter.MoorOrderCardListAdapter;
import com.moor.imkf.demo.bean.MoorEnumChatItemClickType;
import com.moor.imkf.demo.multichat.base.MoorBaseSendHolder;
import com.moor.imkf.demo.multichat.base.MoorBaseSendViewBinder;
import com.moor.imkf.demo.utils.MoorJsonUtils;
import com.moor.imkf.demo.utils.MoorPixelUtil;
import com.moor.imkf.demo.utils.MoorScreenUtils;
import com.moor.imkf.demo.view.shadowlayout.MoorShadowLayout;
import com.moor.imkf.moorsdk.bean.MoorMsgBean;
import com.moor.imkf.moorsdk.bean.MoorOptions;
import com.moor.imkf.moorsdk.bean.MoorOrderCardBean;
import com.moor.imkf.moorsdk.utils.MoorUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class MoorOrderListSendViewBinder extends MoorBaseSendViewBinder<MoorMsgBean, MoorOrderListSendViewBinder.ViewHolder> {

    private final MoorOptions options;

    public MoorOrderListSendViewBinder(MoorOptions options) {
        this.options = options;
    }

    @Override
    protected MoorBaseSendHolder onCreateContentViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.moor_chat_row_orderinfolist_send, parent, false);
        return new MoorOrderListSendViewBinder.ViewHolder(root);
    }

    @Override
    protected void onBindContentViewHolder(@NonNull MoorOrderListSendViewBinder.ViewHolder holder, @NonNull final MoorMsgBean item) {
        holder.setData(item,options);
    }

     class ViewHolder extends MoorBaseSendHolder {
        TextView tv_order_list_title, tv_order_num_name, tv_order_num, tv_order_btn_l, tv_order_btn_r;
        RecyclerView rv_order_list;
        MoorShadowLayout sl_order_btn_l, sl_order_btn_r;
        LinearLayout ll_ordercard;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_order_list_title = itemView.findViewById(R.id.tv_order_list_title);
            tv_order_num_name = itemView.findViewById(R.id.tv_order_num_name);
            tv_order_num = itemView.findViewById(R.id.tv_order_num);
            tv_order_btn_l = itemView.findViewById(R.id.tv_order_btn_l);
            tv_order_btn_r = itemView.findViewById(R.id.tv_order_btn_r);
            rv_order_list = itemView.findViewById(R.id.rv_order_list);
            sl_order_btn_l = itemView.findViewById(R.id.sl_order_btn_l);
            sl_order_btn_r = itemView.findViewById(R.id.sl_order_btn_r);
            ll_ordercard = itemView.findViewById(R.id.ll_ordercard);
        }

        public void setData(final MoorMsgBean item, MoorOptions options) {
            if (item != null) {
                if (!TextUtils.isEmpty(item.getContent())) {
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(Boolean.class, MoorJsonUtils.booleanAsIntAdapter)
                            .registerTypeAdapter(boolean.class, MoorJsonUtils.booleanAsIntAdapter)
                            .create();

                    try {
                        String str = URLDecoder.decode(item.getContent(), "utf-8");
                        final MoorOrderCardBean cardBean = gson.fromJson(str, MoorOrderCardBean.class);
                        if (cardBean != null) {
                            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ll_ordercard.getLayoutParams();
                            params.width = MoorScreenUtils.getScreenWidth(MoorUtils.getApp()) - 2* (int)MoorUtils.getApp().getResources().getDimension(R.dimen.moor_chat_avatar_size) - 4 * MoorPixelUtil.dp2px(10);
                            ll_ordercard.setLayoutParams(params);
                            if (!TextUtils.isEmpty(cardBean.getOrderTitle())) {
                                tv_order_list_title.setVisibility(View.VISIBLE);
                                tv_order_list_title.setText(cardBean.getOrderTitle());
                            } else {
                                tv_order_list_title.setVisibility(View.GONE);
                            }


                            if (!TextUtils.isEmpty(cardBean.getOrderNumName())) {
                                tv_order_num_name.setVisibility(View.VISIBLE);
                                tv_order_num_name.setText(cardBean.getOrderNumName());
                            } else {
                                tv_order_num_name.setVisibility(View.GONE);
                            }

                            if (!TextUtils.isEmpty(cardBean.getOrderNum())) {
                                tv_order_num.setVisibility(View.VISIBLE);
                                tv_order_num.setText(cardBean.getOrderNum());
                            } else {
                                tv_order_num.setVisibility(View.GONE);
                            }

                            if (cardBean.getBtnLeftShow()) {
                                sl_order_btn_l.setVisibility(View.VISIBLE);
                                tv_order_btn_l.setText(cardBean.getBtnLeftText());

                                tv_order_btn_l.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        getBinderClickListener().onClick(v,item, MoorEnumChatItemClickType.TYPE_ORDER_CARD_CLICK.setObj(
                                                cardBean.getBtnLeftTarget(),cardBean.getBtnLeftUrl()));
                                    }
                                });
                            } else {
                                sl_order_btn_l.setVisibility(View.GONE);
                            }

                            if (cardBean.getBtnRightShow()) {
                                sl_order_btn_r.setVisibility(View.VISIBLE);
                                tv_order_btn_r.setText(cardBean.getBtnRightText());
                                if(options!=null){
                                    String textcolor=options.getSdkMainThemeColor();
                                    if(!TextUtils.isEmpty(textcolor)){
                                        tv_order_btn_r.setTextColor(Color.parseColor(textcolor));
                                        sl_order_btn_r.setStrokeColor(Color.parseColor(textcolor));
                                    }
                                }
                                tv_order_btn_r.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        getBinderClickListener().onClick(v,item, MoorEnumChatItemClickType.TYPE_ORDER_CARD_CLICK.setObj(
                                                cardBean.getBtnRightTarget(),cardBean.getBtnRightUrl()));
                                    }
                                });
                            } else {
                                sl_order_btn_r.setVisibility(View.GONE);
                            }


                            if (cardBean.getOrderList() != null && cardBean.getOrderList().size() > 0) {
                                rv_order_list.setLayoutManager(new LinearLayoutManager(MoorUtils.getApp()));
                                MoorOrderCardListAdapter adapter = new MoorOrderCardListAdapter(cardBean.getOrderList(),MoorOrderListSendViewBinder.this);
                                rv_order_list.setAdapter(adapter);
                            }
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
