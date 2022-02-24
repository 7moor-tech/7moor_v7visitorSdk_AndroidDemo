package com.moor.imkf.demo.multichat.multirow;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moor.imkf.demo.R;
import com.moor.imkf.demo.bean.MoorEnumChatItemClickType;
import com.moor.imkf.demo.multichat.base.MoorBaseReceivedHolder;
import com.moor.imkf.demo.multichat.base.MoorBaseReceivedViewBinder;
import com.moor.imkf.demo.utils.MoorColorUtils;
import com.moor.imkf.moorsdk.bean.MoorMsgBean;
import com.moor.imkf.moorsdk.bean.MoorOptions;
import com.moor.imkf.moorsdk.utils.MoorUtils;

import java.util.List;

public class MoorCSRReceviedViewBinder extends MoorBaseReceivedViewBinder<MoorMsgBean, MoorCSRReceviedViewBinder.ViewHolder> {
    private final MoorOptions options;

    public MoorCSRReceviedViewBinder(MoorOptions options) {
        this.options = options;
    }

    @Override
    protected MoorBaseReceivedHolder onCreateContentViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.moor_chat_row_csr_recevied, parent, false);
        return new MoorCSRReceviedViewBinder.ViewHolder(root);
    }

    @Override
    protected void onBindContentViewHolder(@NonNull ViewHolder holder, @NonNull final MoorMsgBean item) {
        MoorBaseReceivedViewBinder.ViewHolder parent_holder = holder.getParent();
        gone_baseView(parent_holder);
        holder.setData(item);
    }

    @Override
    protected void onBindContentViewHolder(@NonNull ViewHolder holder, @NonNull MoorMsgBean item, @NonNull List<Object> payloads) {

    }

    @Override
    protected void onBaseViewRecycled(@NonNull ViewHolder holder) {

    }

    /*隐藏左侧头像，名称等ui */
    private void gone_baseView(MoorBaseReceivedViewBinder.ViewHolder parent_holder) {
        if (parent_holder != null) {
            if (parent_holder.ivChatAvatar != null) {
                parent_holder.ivChatAvatar.setVisibility(View.GONE);
            }
            if (parent_holder.chattingTvName != null) {
                parent_holder.chattingTvName.setVisibility(View.GONE);
            }
        }
    }

    class ViewHolder extends MoorBaseReceivedHolder {
        TextView tv_show_csr,tv_csrtitle;
        RelativeLayout rl_csr;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            rl_csr = itemView.findViewById(R.id.rl_csr);
            tv_show_csr = itemView.findViewById(R.id.tv_show_csr);
            tv_csrtitle=itemView.findViewById(R.id.tv_csrtitle);
        }

        void setData(final @NonNull MoorMsgBean item) {

            if (item.isSatisfactionInvite()) {
                rl_csr.setBackgroundColor(Color.TRANSPARENT);
                tv_csrtitle.setText(MoorUtils.getApp().getResources().getString(R.string.moor_csr_chatrow)
                        +"   "+
                        MoorUtils.getApp().getResources().getString(R.string.moor_csr_chatrow_status_evaluationed));
                tv_csrtitle.setTextColor(MoorUtils.getApp().getResources().getColor(R.color.moor_color_999999));
                tv_show_csr.setVisibility(View.GONE);
            } else {
                rl_csr.setBackground( MoorUtils.getApp().getResources().getDrawable(R.drawable.moor_bg_transant_csr_bg));
                tv_csrtitle.setText(MoorUtils.getApp().getResources().getString(R.string.moor_csr_chatrow));
                tv_csrtitle.setTextColor(MoorUtils.getApp().getResources().getColor(R.color.moor_color_ffffff));
                tv_show_csr.setVisibility(View.VISIBLE);
                    tv_show_csr.setTextColor(MoorColorUtils.getColorWithAlpha(1f,options.getSdkMainThemeColor()));
                tv_show_csr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getBinderClickListener().onClick(v, item, MoorEnumChatItemClickType.TYPE_CSR_CLICK.setObj(""));
                    }
                });
            }
        }
    }
}
