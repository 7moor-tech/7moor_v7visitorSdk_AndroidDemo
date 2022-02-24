package com.moor.imkf.demo.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moor.imkf.demo.R;
import com.moor.imkf.demo.bean.MoorEnumChatItemClickType;
import com.moor.imkf.demo.multichat.multirow.MoorOrderListSendViewBinder;
import com.moor.imkf.demo.utils.MoorPixelUtil;
import com.moor.imkf.moorsdk.bean.MoorOptions;
import com.moor.imkf.moorsdk.bean.MoorOrderCardBean;
import com.moor.imkf.moorsdk.manager.MoorManager;

import java.util.ArrayList;

public class MoorOrderCardListAdapter extends RecyclerView.Adapter<MoorOrderCardListAdapter.CardViewHolder> {
    private ArrayList<MoorOrderCardBean.OrderListBean> orderListBeans = new ArrayList<MoorOrderCardBean.OrderListBean>();
    private MoorOptions options;
    private MoorOrderListSendViewBinder viewBinder;

    public MoorOrderCardListAdapter(ArrayList<MoorOrderCardBean.OrderListBean> orderListBeans, MoorOrderListSendViewBinder viewBinder) {
        this.orderListBeans = orderListBeans;
        this.options = MoorManager.getInstance().getOptions();
        this.viewBinder = viewBinder;
    }

    @NonNull
    @Override
    public MoorOrderCardListAdapter.CardViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.moor_item_ordercard, parent, false);
        return new MoorOrderCardListAdapter.CardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MoorOrderCardListAdapter.CardViewHolder tagViewHolder, int i) {
        if (orderListBeans.get(i) != null) {
            final MoorOrderCardBean.OrderListBean bean = orderListBeans.get(i);

            if (!TextUtils.isEmpty(bean.getImgUrl())) {
                MoorManager.getInstance().loadImage(bean.getImgUrl(), tagViewHolder.iv_orderimg, MoorPixelUtil.dp2px(80f), MoorPixelUtil.dp2px(80f));
            } else {
                tagViewHolder.iv_orderimg.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(bean.getContent())) {
                tagViewHolder.tv_order_content.setVisibility(View.VISIBLE);
                tagViewHolder.tv_order_content.setText(bean.getContent());
            } else {
                tagViewHolder.tv_order_content.setVisibility(View.GONE);
            }


            if (!TextUtils.isEmpty(bean.getPrice())) {
                tagViewHolder.tv_order_price.setVisibility(View.VISIBLE);
                tagViewHolder.tv_order_price.setText(bean.getPrice());
                if (options != null) {
                    String textcolor = options.getSdkMainThemeColor();
                    if (!TextUtils.isEmpty(textcolor)) {
                        tagViewHolder.tv_order_price.setTextColor(Color.parseColor(textcolor));
                    }
                }

            } else {
                tagViewHolder.tv_order_price.setVisibility(View.GONE);
            }

            tagViewHolder.rl_order_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewBinder.getBinderClickListener().onClick(v, null, MoorEnumChatItemClickType.TYPE_ORDER_CARD_CLICK.setObj(
                            bean.getClickTarget(), bean.getClickUrl()));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
//        return orderListBeans == null ? 0 : orderListBeans.size();
        return orderListBeans == null ? 0 : 1;
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {
        TextView tv_order_content, tv_order_price;
        ImageView iv_orderimg;
        RelativeLayout rl_order_item;

        public CardViewHolder(View itemView) {
            super(itemView);
            tv_order_content = itemView.findViewById(R.id.tv_order_content);
            tv_order_price = itemView.findViewById(R.id.tv_order_price);
            iv_orderimg = itemView.findViewById(R.id.iv_orderimg);
            rl_order_item = itemView.findViewById(R.id.rl_order_item);
        }
    }
}
