package com.moor.imkf.demo.multichat.multirow;

import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moor.imkf.demo.R;
import com.moor.imkf.demo.multichat.base.MoorBaseReceivedHolder;
import com.moor.imkf.demo.multichat.base.MoorBaseReceivedViewBinder;
import com.moor.imkf.moorsdk.bean.MoorMsgBean;
import com.moor.imkf.moorsdk.bean.MoorOptions;
import com.moor.imkf.moorsdk.constants.MoorChatMsgType;
import com.moor.imkf.moorsdk.utils.MoorUtils;

import java.util.List;

/**
 * 接收的系统类型消息
 */
public class MoorSystemReceivedViewBinder extends MoorBaseReceivedViewBinder<MoorMsgBean, MoorSystemReceivedViewBinder.ViewHolder> {

    private final MoorOptions options;

    public MoorSystemReceivedViewBinder(MoorOptions options) {
        this.options = options;
    }

    @Override
    protected MoorBaseReceivedHolder onCreateContentViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.moor_chat_row_sys_received, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindContentViewHolder(@NonNull ViewHolder holder, @NonNull MoorMsgBean item) {
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


    /**
     * 隐藏左侧头像，名称等ui
     */
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

    static class ViewHolder extends MoorBaseReceivedHolder {
        TextView tv_sys_msg;
        RelativeLayout rl_sys_msg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_sys_msg = itemView.findViewById(R.id.tv_sys_msg);
            rl_sys_msg = itemView.findViewById(R.id.rl_sys_msg);
        }

        void setData(@NonNull MoorMsgBean item) {
            if (MoorChatMsgType.MSG_TYPE_CLAIM.equals(item.getContentType())) {
                //坐席接入会话
                tv_sys_msg.setText(MoorUtils.getApp().getResources().getString(R.string.moor_seat) + item.getContent() + MoorUtils.getApp().getResources().getString(R.string.moor_claim_fouyou));
            } else if (MoorChatMsgType.MSG_TYPE_ROBOT_INIT.equals(item.getContentType())) {
                //机器人接入会话
                tv_sys_msg.setText(item.getContent() + MoorUtils.getApp().getResources().getString(R.string.moor_claim_fouyou));
            } else if (MoorChatMsgType.MSG_TYPE_REDIRECT.equals(item.getContentType())) {
                //坐席转接会话
                tv_sys_msg.setText(MoorUtils.getApp().getResources().getString(R.string.moor_chat_session_redirect) + item.getContent() + MoorUtils.getApp().getResources().getString(R.string.moor_claim_fouyou));
            } else {
                tv_sys_msg.setText(item.getContent());
            }

        }
    }
}
